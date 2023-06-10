package be.minelabs.data.lang;

import be.minelabs.Minelabs;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.util.Identifier;

import java.nio.file.Path;

public abstract class BaseLangProvider extends FabricLanguageProvider {
    // Because fabric chose to make this private for some reason
    protected final String languageCode;

    protected BaseLangProvider(FabricDataOutput dataOutput, String languageCode) {
        super(dataOutput, languageCode);
        this.languageCode = languageCode;
    }

    protected TranslationBuilder getFillMissingTranslationBuilder(TranslationBuilder translationBuilder){
        return getFillMissingTranslationBuilder(translationBuilder, true);
    }

    protected TranslationBuilder getFillMissingTranslationBuilder(TranslationBuilder translationBuilder, boolean warn){
        return (key, value) -> {
            try{
                translationBuilder.add(key, value);
                if (warn)
                    Minelabs.LOGGER.warn("[" + languageCode + "] Added missing translation for " + key);
            } catch (RuntimeException ignored){}
        };
    }

    protected Path getLangFilePath(String code) {
        return dataOutput
                .getResolver(DataOutput.OutputType.RESOURCE_PACK, "lang")
                .resolveJson(new Identifier(dataOutput.getModId(), code));
    }

    protected Path getLangFilePath() {
        return getLangFilePath(languageCode);
    }

    protected Path getStaticLangFilePath(String code) {
        return dataOutput.getModContainer().findPath(String.format("assets/%s/lang/%s.static.json", dataOutput.getModId(), code)).get();
    }

    protected Path getStaticLangFilePath() {
        return getStaticLangFilePath(languageCode);
    }
}
