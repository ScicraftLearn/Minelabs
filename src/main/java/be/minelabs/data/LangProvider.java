package be.minelabs.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.io.IOException;

public class LangProvider extends MinelabsLangProvider{

    public LangProvider(FabricDataOutput dataOutput) {
        // DEFAULT LANG: en_us
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        super.generateTranslations(translationBuilder);
        try {
            translationBuilder.add(dataOutput.getModContainer()
                    .findPath("assets/minelabs/lang/en_us.static.json").get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
