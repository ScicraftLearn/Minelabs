package be.minelabs.data;

import be.minelabs.Minelabs;
import be.minelabs.item.ItemGroups;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class MinelabsLangProvider extends FabricLanguageProvider {

    private static final String EMPTY_TRANSLATION = "missing_translation";

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

            if (translationEntries.containsKey(key) &&
                    !translationEntries.get(key).isEmpty() && !translationEntries.get(key).equals(EMPTY_TRANSLATION)) {
                //THIS IS THE ONLY REASON WE OVERRIDE THE METHOD
                // we want Value replacements IF EMPTY
                throw new RuntimeException("Translation key - " + key + " - Already has a value.");
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
        // automatic Block/item/entity(type) key gen
        addEmptyKeys(Registries.ITEM, (k, v) -> translationBuilder.add(v, EMPTY_TRANSLATION));
        addEmptyKeys(Registries.BLOCK, (k, v) -> translationBuilder.add(v, EMPTY_TRANSLATION));
        addEmptyKeys(Registries.ENTITY_TYPE, (k, v) -> translationBuilder.add(v, EMPTY_TRANSLATION));

        // MANUALLY DOING GROUPS
        translationBuilder.add(ItemGroups.ATOMS, EMPTY_TRANSLATION);
        translationBuilder.add(ItemGroups.MINELABS, EMPTY_TRANSLATION);
        translationBuilder.add(ItemGroups.QUANTUM_FIELDS, EMPTY_TRANSLATION);
        translationBuilder.add(ItemGroups.CHEMICALS, EMPTY_TRANSLATION);
        translationBuilder.add(ItemGroups.ELEMENTARY_PARTICLES, EMPTY_TRANSLATION);

        translationBuilder.add("entity.minecraft.villager.sciencevillager", EMPTY_TRANSLATION);
        // DO NOT USE
        //addEmptyKeys(Registries.VILLAGER_PROFESSION, translationBuilder);

        // manual : text
        translationBuilder.add("text.minelabs.valid", EMPTY_TRANSLATION);
        translationBuilder.add("text.minelabs.invalid", EMPTY_TRANSLATION);
        translationBuilder.add("text.minelabs.not_implemented", EMPTY_TRANSLATION);
        translationBuilder.add("text.minelabs.multiple_molecules", EMPTY_TRANSLATION);
        translationBuilder.add("text.minelabs.active", EMPTY_TRANSLATION);
        translationBuilder.add("text.minelabs.inactive", EMPTY_TRANSLATION);
        translationBuilder.add("text.minelabs.toggle_instruction", EMPTY_TRANSLATION);
    }

    private <T> void addEmptyKeys(DefaultedRegistry<T> registry, BiConsumer<Identifier, T> consumer) {
        for (Map.Entry<? extends RegistryKey<T>, T> entry : registry.getEntrySet()) {
            if (entry.getKey().getValue().getNamespace().equals(Minelabs.MOD_ID)) {
                consumer.accept(entry.getKey().getValue(), entry.getValue());
            }
        }
    }
}
