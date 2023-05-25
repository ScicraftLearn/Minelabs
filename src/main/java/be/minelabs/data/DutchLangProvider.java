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
    }
}
