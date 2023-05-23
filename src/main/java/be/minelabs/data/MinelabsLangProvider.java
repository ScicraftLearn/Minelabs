package be.minelabs.data;

import be.minelabs.block.Blocks;
import be.minelabs.entity.Entities;
import be.minelabs.item.ItemGroups;
import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public abstract class MinelabsLangProvider extends FabricLanguageProvider {

    private final String languageCode;

    public MinelabsLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
        this.languageCode = "en_us";
    }

    public MinelabsLangProvider(FabricDataOutput dataOutput, String languageCode) {
        super(dataOutput, languageCode);
        this.languageCode = languageCode;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        TreeMap<String, String> translationEntries = new TreeMap<>();

        generateTranslations((String key, String value) -> {
            Objects.requireNonNull(key);
            Objects.requireNonNull(value);

            if (translationEntries.containsKey(key) && !translationEntries.get(key).isEmpty()) {
                //THIS IS THE ONLY REASON WE OVERRIDE THE METHOD
                // we want Value replacements IF EMPTY
                throw new RuntimeException("Existing translation key found - " + key + " - Duplicate will be ignored.");
            }

            translationEntries.put(key, value);

        });

        JsonObject langEntryJson = new JsonObject();

        for (Map.Entry<String, String> entry : translationEntries.entrySet()) {
            langEntryJson.addProperty(entry.getKey(), entry.getValue());
        }

        return DataProvider.writeToPath(writer, langEntryJson, getLangFilePath(languageCode));
    }

    private Path getLangFilePath(String code) {
        return dataOutput
                .getResolver(DataOutput.OutputType.RESOURCE_PACK, "lang")
                .resolveJson(new Identifier(dataOutput.getModId(), code));
    }

    /**
     * Generate Empty keys for translation files
     * @param translationBuilder : Map/TreeMap for addition
     */
    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        // item groups
        translationBuilder.add(ItemGroups.ATOMS, "");
        translationBuilder.add(ItemGroups.MINELABS, "");
        translationBuilder.add(ItemGroups.QUANTUM_FIELDS, "");
        translationBuilder.add(ItemGroups.CHEMICALS, "");
        translationBuilder.add(ItemGroups.ELEMENTARY_PARTICLES, "");

        // blocks
        translationBuilder.add(Blocks.ANTI_NEUTRON, "");
        translationBuilder.add(Blocks.ANTI_PROTON, "");
        translationBuilder.add(Blocks.ANTINEUTRINO, "");
        translationBuilder.add(Blocks.ATOM_FLOOR, "");
        translationBuilder.add(Blocks.BUDDING_SALT_BLOCK, "");
        translationBuilder.add(Blocks.BOHR_BLUEPRINT, "");
        translationBuilder.add(Blocks.BURNER, "");
        translationBuilder.add(Blocks.CHARGED_POINT_BLOCK, "");
        translationBuilder.add(Blocks.DEEPSLATE_SALT_ORE, "");
        translationBuilder.add(Blocks.DOWNQUARK_QUANTUMFIELD, "");
        translationBuilder.add(Blocks.ELECTRON, "");
        translationBuilder.add(Blocks.ELECTRIC_FIELD_SENSOR_BLOCK, "");
        translationBuilder.add(Blocks.ELECTRON_QUANTUMFIELD, "");
        translationBuilder.add(Blocks.ERLENMEYER_STAND, "");
        translationBuilder.add(Blocks.GLUON_QUANTUMFIELD, "");
        translationBuilder.add(Blocks.GREEN_FIRE, "");
        translationBuilder.add(Blocks.IONIC_BLOCK, "");
        translationBuilder.add(Blocks.LAB_CABIN, "");
        translationBuilder.add(Blocks.LAB_CENTER, "");
        translationBuilder.add(Blocks.LAB_CORNER, "");
        translationBuilder.add(Blocks.LAB_DRAWER, "");
        translationBuilder.add(Blocks.LAB_SINK, "");
        translationBuilder.add(Blocks.LARGE_SALT_CRYSTAL, "");
        translationBuilder.add(Blocks.LEWIS_BLOCK, "");
        translationBuilder.add(Blocks.MEDIUM_SALT_CRYSTAL, "");
        translationBuilder.add(Blocks.MOLOGRAM_BLOCK, "");
        translationBuilder.add(Blocks.MICROSCOPE, "");
        translationBuilder.add(Blocks.NEUTRINO, "");
        translationBuilder.add(Blocks.NEUTRINO_QUANTUMFIELD, "");
        translationBuilder.add(Blocks.NEUTRON, "");
        translationBuilder.add(Blocks.PHOTON_QUANTUMFIELD, "");
        translationBuilder.add(Blocks.PION_MINUS, "");
        translationBuilder.add(Blocks.PION_NUL, "");
        translationBuilder.add(Blocks.PION_PLUS, "");
        translationBuilder.add(Blocks.POSITRON, "");
        translationBuilder.add(Blocks.PORTAL_BLOCK, "");
        translationBuilder.add(Blocks.PROTON, "");
        translationBuilder.add(Blocks.SALT_CRYSTAL, "");
        translationBuilder.add(Blocks.SALT_ORE, "");
        translationBuilder.add(Blocks.SALT_WIRE, "");
        translationBuilder.add(Blocks.SALT_BLOCK, "");
        translationBuilder.add(Blocks.SMALL_SALT_CRYSTAL, "");
        translationBuilder.add(Blocks.TIME_FREEZE_BLOCK, "");
        translationBuilder.add(Blocks.TUBERACK, "");
        translationBuilder.add(Blocks.UPQUARK_QUANTUMFIELD, "");
        translationBuilder.add(Blocks.WEAK_BOSON, "");
        translationBuilder.add(Blocks.WEAK_BOSON_QUANTUMFIELD, "");

        // items (not BlockItems)
        Items.ATOMS.forEach(atomItem -> translationBuilder.add(atomItem, ""));
        translationBuilder.add(Items.MAGNET, "");
        translationBuilder.add(Items.POCKET_HOLE, "");
        translationBuilder.add(Items.LASERTOOL_IRON, "");
        translationBuilder.add(Items.LASERTOOL_GOLD, "");
        translationBuilder.add(Items.LASERTOOL_DIAMOND, "");

        // entities (may include manual)
        translationBuilder.add(Entities.ENTROPY_CREEPER, "");
        translationBuilder.add(Entities.BALLOON, "");
        translationBuilder.add("entity.minecraft.villager.sciencevillager", "");


        // manual : text
        translationBuilder.add("text.minelabs.valid", "");
        translationBuilder.add("text.minelabs.invalid", "");
        translationBuilder.add("text.minelabs.not_implemented", "");
        translationBuilder.add("text.minelabs.multiple_molecules", "");
        translationBuilder.add("text.minelabs.active", "");
        translationBuilder.add("text.minelabs.inactive", "");
        translationBuilder.add("text.minelabs.toggle_instruction", "");


    }
}
