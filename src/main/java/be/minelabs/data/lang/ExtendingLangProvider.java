package be.minelabs.data.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.io.IOException;

public class ExtendingLangProvider extends BaseLangProvider {
    protected final String parentLanguageCode;
    protected final boolean logMissing;

    public ExtendingLangProvider(FabricDataOutput dataOutput, String languageCode, String parentLanguageCode, boolean logMissing) {
        super(dataOutput, languageCode);
        this.parentLanguageCode = parentLanguageCode;
        this.logMissing = logMissing;
    }

    public static FabricDataGenerator.Pack.Factory<ExtendingLangProvider> factory(String languageCode, String parentLanguageCode) {
        return factory(languageCode, parentLanguageCode, true);
    }

    public static FabricDataGenerator.Pack.Factory<ExtendingLangProvider> factory(String languageCode, String parentLanguageCode, boolean logMissing) {
        return output -> new ExtendingLangProvider(output, languageCode, parentLanguageCode, logMissing);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        try {
            translationBuilder.add(getStaticLangFilePath());

            translationBuilder = getFillMissingTranslationBuilder(translationBuilder, logMissing);
            translationBuilder.add(getLangFilePath(parentLanguageCode));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
