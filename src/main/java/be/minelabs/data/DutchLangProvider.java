package be.minelabs.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.io.IOException;

public class DutchLangProvider extends MinelabsLangProvider {
    public DutchLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "nl_nl");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        try {
            translationBuilder.add(getLangFilePath("nl_be"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            // ADD OVERRIDE Translations here


        } catch (RuntimeException e) {
            // Silently catch the error, because we are override some lines
        }
    }
}
