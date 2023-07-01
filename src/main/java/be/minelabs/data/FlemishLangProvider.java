package be.minelabs.data;

import be.minelabs.block.Blocks;
import be.minelabs.item.Items;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.io.IOException;

public class FlemishLangProvider extends MinelabsLangProvider {
    public FlemishLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "nl_be");
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        super.generateTranslations(builder);
        try {
            builder.add(dataOutput.getModContainer()
                    .findPath("assets/minelabs/lang/nl_be.static.json").get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        builder.add(Items.SALT, "Zout");
        builder.add(Items.SALT_SHARD, "Zoutscherf");
        builder.add(Items.SALT_BLOCK, "Zouten Blok");
        builder.add(Items.SMALL_SALT_CRYSTAL, "Klein Zoutkristal");
        builder.add(Items.MEDIUM_SALT_CRYSTAL, "Gemiddeld Zoutkristal");
        builder.add(Items.LARGE_SALT_CRYSTAL, "Groot Zoutkristal");
        builder.add(Items.SALT_CRYSTAL, "Zoutkristallen");
        builder.add(Items.SALT_ORE, "Zouterts");
        builder.add(Items.DEEPSLATE_SALT_ORE, "Deepslate Zouterts");
        builder.add(Items.BUDDING_SALT_BLOCK, "Uitlopende Zout");

        builder.add(Items.LAB_COAT, "Labojas");
        builder.add(Items.SAFETY_GLASSES, "Veiligheidsbril");
        builder.add(Items.LENS, "Microscooplens");
        builder.add(Items.BIG_LENS, "Microscooplens x2");
        builder.add(Blocks.LAB_DRAWER, "Labo Ladenkast");
        builder.add(Blocks.LAB_SINK, "Labo Wastafel");
        builder.add(Blocks.LAB_CABIN, "Labo Kast");
        builder.add(Blocks.LAB_CENTER, "Labotafel (Midden)");
        builder.add(Blocks.LAB_CORNER, "Labotafel (Hoek)");
        builder.add(Blocks.MICROSCOPE, "Microscoop");
        builder.add(Blocks.TUBERACK, "Proefbuisrekje");
        builder.add(Blocks.BURNER, "Bunsen Brander");
        builder.add(Blocks.ERLENMEYER_STAND, "Erlenmeyer Stand");

        builder.add(Items.LASERTOOL_IRON, "Ijzeren Laser Tool");
        builder.add(Items.LASERTOOL_GOLD, "Gouden Laser Tool");
        builder.add(Items.LASERTOOL_DIAMOND, "Diamanten Laser Tool");
        builder.add(Items.MAGNET, "Magneet");
        builder.add(Items.POCKET_HOLE, "Miniatuur Zwart Gat");

        builder.add(Blocks.WEAK_BOSON_QUANTUMFIELD, "Zwak Boson Kwantumveld");
        builder.add(Blocks.UPQUARK_QUANTUMFIELD, "Upquark Kwantumveld");
        builder.add(Blocks.DOWNQUARK_QUANTUMFIELD, "Downquark Kwantumveld");
        builder.add(Blocks.PHOTON_QUANTUMFIELD, "Foton Kwantumveld");
        builder.add(Blocks.NEUTRINO_QUANTUMFIELD, "Neutrino Kwantumveld");
        builder.add(Blocks.ELECTRON_QUANTUMFIELD, "Elektron Kwantumveld");
        builder.add(Blocks.GLUON_QUANTUMFIELD, "Gluon Kwantumveld");
    }
}
