package be.minelabs.data;

import be.minelabs.block.Blocks;
import be.minelabs.entity.Entities;
import be.minelabs.item.ItemGroups;
import be.minelabs.item.Items;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.io.IOException;

public class DutchLangProvider extends FabricLanguageProvider {
    public DutchLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "nl_nl");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        try {
            translationBuilder.add(dataOutput.getModContainer()
                    .findPath("assets/minelabs/lang/nl_nl.json").get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // item groups
        translationBuilder.add(ItemGroups.ATOMS, "Atoms");
        translationBuilder.add(ItemGroups.MINELABS, "Minelabs");
        translationBuilder.add(ItemGroups.QUANTUM_FIELDS, "Quantum Fields");
        translationBuilder.add(ItemGroups.CHEMICALS, "Chemicals");
        translationBuilder.add(ItemGroups.ELEMENTARY_PARTICLES, "Elementary Particles");

        // blocks
        translationBuilder.add(Blocks.ATOM_FLOOR, "");
        translationBuilder.add(Blocks.MICROSCOPE, "Microscope");

        // items (not BlockItems)
        translationBuilder.add(Items.MAGNET, "Magnet");
        translationBuilder.add(Items.POCKET_HOLE, "Pocket Black Hole");
        translationBuilder.add(Items.LASERTOOL_IRON, "Iron Laser Tool");
        translationBuilder.add(Items.LASERTOOL_GOLD, "Gold Laser Tool");
        translationBuilder.add(Items.LASERTOOL_DIAMOND, "Diamond Laser Tool");

        // entities (may include manual)
        translationBuilder.add(Entities.ENTROPY_CREEPER, "Entropy Creeper");
        translationBuilder.add(Entities.BALLOON, "Balloon");
        translationBuilder.add("entity.minecraft.villager.sciencevillager", "Science Villager");


        // manual : text
        translationBuilder.add("text.minelabs.valid", "Correct Molecule");
        translationBuilder.add("text.minelabs.invalid", "Incorrect Molecule");
        translationBuilder.add("text.minelabs.not_implemented", "Molecule is not implemented");

    }
}
