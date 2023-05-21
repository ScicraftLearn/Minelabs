package be.minelabs.data;

import be.minelabs.Minelabs;
import be.minelabs.item.ItemGroups;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
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
        // MANUALLY DOING GROUPS
        translationBuilder.add(ItemGroups.ATOMS, "");
        translationBuilder.add(ItemGroups.MINELABS, "");
        translationBuilder.add(ItemGroups.QUANTUM_FIELDS, "");
        translationBuilder.add(ItemGroups.CHEMICALS, "");
        translationBuilder.add(ItemGroups.ELEMENTARY_PARTICLES, "");

        for (Map.Entry<RegistryKey<Item>, Item> entry : Registries.ITEM.getEntrySet()) {
            if (entry.getKey().getValue().getNamespace().equals(Minelabs.MOD_ID)) {
                translationBuilder.add(entry.getValue(), "");
            }
        }

        for (Map.Entry<RegistryKey<Block>, Block> entry : Registries.BLOCK.getEntrySet()) {
            if (entry.getKey().getValue().getNamespace().equals(Minelabs.MOD_ID)) {
                translationBuilder.add(entry.getValue(), "");
            }
        }

        for (Map.Entry<RegistryKey<EntityType<?>>, EntityType<?>> entry : Registries.ENTITY_TYPE.getEntrySet()) {
            if (entry.getKey().getValue().getNamespace().equals(Minelabs.MOD_ID)) {
                translationBuilder.add(entry.getValue(), "");
            }
        }

        //addEmptyKeys(Registries.ITEM, translationBuilder);
        //addEmptyKeys(Registries.BLOCK, translationBuilder);
        //addEmptyKeys(Registries.ENTITY_TYPE, translationBuilder);


        translationBuilder.add("entity.minecraft.villager.sciencevillager", "");
        // DO NOT USE
        //addEmptyKeys(Registries.VILLAGER_PROFESSION, translationBuilder);

        // manual : text
        translationBuilder.add("text.minelabs.valid", "");
        translationBuilder.add("text.minelabs.invalid", "");
        translationBuilder.add("text.minelabs.not_implemented", "");
        translationBuilder.add("text.minelabs.multiple_molecules", "");
        translationBuilder.add("text.minelabs.active", "");
        translationBuilder.add("text.minelabs.inactive", "");
        translationBuilder.add("text.minelabs.toggle_instruction", "");
    }

    private <T> void addEmptyKeys(DefaultedRegistry<T> registry, TranslationBuilder builder) {
        // TODO WIP
        for (Map.Entry<? extends RegistryKey<T>, T> entry : registry.getEntrySet()) {
            if (entry.getKey().getValue().getNamespace().equals(Minelabs.MOD_ID)) {
                builder.add((Identifier) entry.getValue(), "");
            }
        }
    }
}
