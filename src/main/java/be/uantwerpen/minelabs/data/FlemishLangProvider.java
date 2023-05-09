package be.uantwerpen.minelabs.data;

import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.entity.Entities;
import be.uantwerpen.minelabs.item.ItemGroups;
import be.uantwerpen.minelabs.item.Items;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.io.IOException;

public class FlemishLangProvider extends FabricLanguageProvider {
    public FlemishLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "nl_be");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        try {
            // JUST COPY NL_NL ?
            translationBuilder.add(dataOutput.getModContainer()
                    .findPath("assets/minelabs/lang/nl_be.json").get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
