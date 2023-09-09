package be.minelabs.data;

import be.minelabs.Minelabs;
import be.minelabs.science.Atom;
import be.minelabs.util.ChemicalFormulaParser;
import com.google.gson.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;

public class MinecraftBlocksData {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Identifier name;
    private final ArrayList<String> molecules;
    private final ArrayList<Integer> distribution;

    public MinecraftBlocksData(Identifier name, JsonArray molecules, JsonArray distribution) {
        this.name = name;
        this.molecules = new ArrayList<>(molecules.size());
        for (JsonElement molecule : molecules) {
            this.molecules.add(molecule.getAsString());
        }
        this.distribution = new ArrayList<>(distribution.size());
        for (JsonElement distributionElement : distribution) {
            this.distribution.add(distributionElement.getAsInt());
        }
    }

    public void serialize(JsonObject json) {
        JsonArray entries = new JsonArray();
        for (int i = 0; i < molecules.size(); i++) {
            Map<String, Integer> map = ChemicalFormulaParser.parseFormula(molecules.get(i));
            String convertedMoleculeName = MinecraftBlocksData.convertMoleculeName(molecules.get(i));

            Identifier convertedMoleculeId = new Identifier(convertedMoleculeName);
            convertedMoleculeName = convertedMoleculeId.getPath();

            JsonObject jsonObject = new JsonObject();
            // Expand the block name if possible
            // For example convert concrete to list of all colors of concrete and take the first item of that
            if (LaserToolDataProvider.isExpandable(convertedMoleculeId)) {
                convertedMoleculeId = LaserToolDataProvider.expandBlock(convertedMoleculeName).get(0);
                convertedMoleculeName = convertedMoleculeId.getPath();
            }
            if(!Registries.BLOCK.get(convertedMoleculeId).equals(Registries.BLOCK.get(Registries.BLOCK.getDefaultId()))) {
                jsonObject.add("type", new JsonPrimitive(new Identifier("minecraft", "loot_table").toString()));
                jsonObject.add("name", new JsonPrimitive(new Identifier(Minelabs.MOD_ID, "lasertool/blocks/" + convertedMoleculeName).toString()));
            } else if(!Registries.ITEM.get(convertedMoleculeId).equals(Registries.ITEM.get(Registries.ITEM.getDefaultId()))) {
                jsonObject.add("type", new JsonPrimitive(new Identifier("minecraft", "item").toString()));
                jsonObject.add("name", new JsonPrimitive(convertedMoleculeId.toString()));
            } else if(map.size() == 1) {
                map.keySet().forEach(key -> {
                    jsonObject.add("type", new JsonPrimitive(new Identifier("minecraft", "item").toString()));
                    Atom atom = Atom.getBySymbol(key);
                    if (atom != null) {
                        jsonObject.add("name", new JsonPrimitive(atom.getItemId().toString()));
                    } else {
                        throw new IllegalStateException("Atom not found for symbol " + key);
                    }
                });
            } else {
                jsonObject.add("type", new JsonPrimitive(new Identifier("minecraft", "loot_table").toString()));
                jsonObject.add("name", new JsonPrimitive(new Identifier(Minelabs.MOD_ID, "molecules/" + convertedMoleculeName).toString()));

                if (!LaserToolDataProvider.moleculeNames.containsKey(convertedMoleculeName)) {
                    LaserToolDataProvider.moleculeNames.put(convertedMoleculeName, map);
                }
            }
            jsonObject.add("weight", new JsonPrimitive(distribution.get(i)));

            JsonObject function = new JsonObject();
            function.add("enchantment", new JsonPrimitive("minecraft:fortune"));
            // Apply the bonus the same way as ore drops scale with fortune
            function.add("formula", new JsonPrimitive("minecraft:ore_drops"));
            function.add("function", new JsonPrimitive("minecraft:apply_bonus"));
            JsonArray functions = new JsonArray();
            functions.add(function);

            jsonObject.add("functions", functions);
            entries.add(jsonObject);
        }
        // Build JSON object for the loot table
        JsonObject group = new JsonObject();
        group.add("type", new JsonPrimitive("minecraft:group"));
        group.add("conditions", JsonParser.parseString("[{\"condition\": \"minecraft:match_tool\",\"predicate\": {\"items\": [\"minelabs:lasertool_diamond\",\"minelabs:lasertool_gold\",\"minelabs:lasertool_iron\"]}}]"));
        group.add("children", entries);
        JsonObject lootTable = new JsonObject();
        lootTable.add("type", new JsonPrimitive("minecraft:loot_table"));
        lootTable.add("name", new JsonPrimitive(new Identifier(name.getNamespace(), "blocks/" + this.name.getPath()).toString()));
        JsonObject alternatives = new JsonObject();
        alternatives.add("type", new JsonPrimitive("minecraft:alternatives"));
        JsonArray children = new JsonArray();
        children.add(group);
        // No vanilla loot table for these blocks in vanilla minecraft
        if (!this.name.getPath().matches("lava|water|bedrock")) {
            children.add(lootTable);
        }
        alternatives.add("children", children);
        JsonObject rolls = new JsonObject();
        rolls.add("rolls", new JsonPrimitive(1));
        JsonArray entriesArray = new JsonArray();
        entriesArray.add(alternatives);
        rolls.add("entries", entriesArray);
        JsonArray pools = new JsonArray();
        pools.add(rolls);
        json.add("pools", pools);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        this.serialize(jsonObject);
        return jsonObject;
    }

    public Identifier getId() {
        return name;
    }

    public static String convertMoleculeName(String moleculeName) {
        StringBuilder minecraftReadableName = new StringBuilder();
        moleculeName = moleculeName.replaceAll("[()]", "-");
        for (int i = 0; i < moleculeName.length(); i++) {
            char character = moleculeName.charAt(i);
            minecraftReadableName.append(character);
            if (i + 1 != moleculeName.length()) {
                char nextChar = moleculeName.charAt(i + 1);
                if (Character.isLetterOrDigit(character) && Character.isUpperCase(nextChar)) {
                    minecraftReadableName.append('_');
                }
            }
        }
        return minecraftReadableName.toString().toLowerCase();
    }
}


