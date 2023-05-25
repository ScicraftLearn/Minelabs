package be.minelabs.data;

import be.minelabs.block.Blocks;
import be.minelabs.item.Items;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.io.IOException;

public class LangProvider extends MinelabsLangProvider{

    public LangProvider(FabricDataOutput dataOutput) {
        // DEFAULT LANG: en_us
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        super.generateTranslations(builder);
        try {
            builder.add(dataOutput.getModContainer()
                    .findPath("assets/minelabs/lang/en_us.static.json").get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        builder.add(Items.SALT, "Salt");
        builder.add(Items.SALT_SHARD, "Salt Shard");
        builder.add(Items.SALT_BLOCK, "Block of Salt");
        builder.add(Items.SMALL_SALT_CRYSTAL, "Small Salt Crystal");
        builder.add(Items.MEDIUM_SALT_CRYSTAL, "Medium Salt Crystal");
        builder.add(Items.LARGE_SALT_CRYSTAL, "Large Salt Crystal");
        builder.add(Items.SALT_CRYSTAL, "Salt Crystal");
        builder.add(Items.SALT_ORE, "Salt Ore");
        builder.add(Items.DEEPSLATE_SALT_ORE, "Deepslate Salt Ore");
        builder.add(Items.BUDDING_SALT_BLOCK, "Budding Salt");

        builder.add(Items.LAB_COAT, "Lab Coat");
        builder.add(Items.SAFETY_GLASSES, "Safety Glasses");
        builder.add(Items.LENS, "Microscope Lens");
        builder.add(Items.BIG_LENS, "Microscope Lens x2");
        builder.add(Blocks.LAB_DRAWER, "Lab Drawers");
        builder.add(Blocks.LAB_SINK, "Lab Sink");
        builder.add(Blocks.LAB_CABIN, "Lab Cabin");
        builder.add(Blocks.LAB_CENTER, "Lab Center");
        builder.add(Blocks.LAB_CORNER, "Lab Corner");
        builder.add(Blocks.MICROSCOPE, "Microscope");
        builder.add(Blocks.TUBERACK, "Tube rack");
        builder.add(Blocks.BURNER, "Bunsen Burner");
        builder.add(Blocks.ERLENMEYER_STAND, "Erlenmeyer Stand");

        builder.add(Items.LASERTOOL_IRON, "Iron Laser Tool");
        builder.add(Items.LASERTOOL_GOLD, "Gold Laser Tool");
        builder.add(Items.LASERTOOL_DIAMOND, "Diamond Laser Tool");
        builder.add(Items.MAGNET, "Magnet");
        builder.add(Items.POCKET_HOLE, "Pocket Black Hole");

        builder.add(Blocks.WEAK_BOSON_QUANTUMFIELD, "Weak Boson Quantumfield");
        builder.add(Blocks.UPQUARK_QUANTUMFIELD, "Upquark Quantumfield");
        builder.add(Blocks.DOWNQUARK_QUANTUMFIELD, "Downquark Quantumfield");
        builder.add(Blocks.PHOTON_QUANTUMFIELD, "Photon Quantumfield");
        builder.add(Blocks.NEUTRINO_QUANTUMFIELD, "Neutrino Quantumfield");
        builder.add(Blocks.ELECTRON_QUANTUMFIELD, "Electron Quantumfield");
        builder.add(Blocks.GLUON_QUANTUMFIELD, "Gluon Quantumfield");


    }
}
