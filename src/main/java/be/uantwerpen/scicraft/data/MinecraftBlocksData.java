package be.uantwerpen.scicraft.data;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.crafting.molecules.Atom;
import be.uantwerpen.scicraft.util.ChemicalFormulaParser;
import com.google.gson.*;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;

public class MinecraftBlocksData {
    private static final Logger LOGGER = LogManager.getLogger();

    private final String name;
    private final ArrayList<String> molecules;
    private final ArrayList<Integer> distribution;

    public MinecraftBlocksData(String name, JsonArray molecules, JsonArray distribution) {
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

            JsonObject jsonObject = new JsonObject();
            // Expand the block name if possible
            // For example convert concrete to list of all colors of concrete and take the first item of that
            if (LaserToolDataProvider.isExpandable(convertedMoleculeName)) {
                convertedMoleculeName = LaserToolDataProvider.expandBlock(convertedMoleculeName).get(0);
            }
            if(!Registry.BLOCK.get(new Identifier("minecraft", convertedMoleculeName)).equals(Registry.BLOCK.get(Registry.BLOCK.getDefaultId()))) {
                jsonObject.add("type", new JsonPrimitive(new Identifier("minecraft", "loot_table").toString()));
                jsonObject.add("name", new JsonPrimitive(new Identifier(Scicraft.MOD_ID, "blocks/" + convertedMoleculeName).toString()));
            } else if(!Registry.ITEM.get(new Identifier("minecraft", convertedMoleculeName)).equals(Registry.ITEM.get(Registry.ITEM.getDefaultId()))) {
                jsonObject.add("type", new JsonPrimitive(new Identifier("minecraft", "item").toString()));
                jsonObject.add("name", new JsonPrimitive(new Identifier("minecraft", convertedMoleculeName).toString()));
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
                jsonObject.add("name", new JsonPrimitive(new Identifier(Scicraft.MOD_ID, "molecules/" + convertedMoleculeName).toString()));

                if (!LaserToolDataProvider.moleculeNames.containsKey(convertedMoleculeName)) {
                    LaserToolDataProvider.moleculeNames.put(convertedMoleculeName, map);
                }
            }
            jsonObject.add("weight", new JsonPrimitive(distribution.get(i)));
            entries.add(jsonObject);
        }
        // Build JSON object for the loot table
        JsonObject group = new JsonObject();
        group.add("type", new JsonPrimitive("minecraft:group"));
        group.add("conditions", JsonParser.parseString("[{\"condition\": \"minecraft:alternative\",\"terms\": [{\"condition\": \"minecraft:match_tool\",\"predicate\": {\"items\": [\"scicraft:lasertool_iron\"]}},{\"condition\": \"minecraft:match_tool\",\"predicate\": {\"items\": [\"scicraft:lasertool_gold\"]}},{\"condition\": \"minecraft:match_tool\",\"predicate\": {\"items\": [\"scicraft:lasertool_diamond\"]}}]}]").getAsJsonArray());
        group.add("children", entries);
        JsonObject lootTable = new JsonObject();
        lootTable.add("type", new JsonPrimitive("minecraft:loot_table"));
        lootTable.add("name", new JsonPrimitive(new Identifier("minecraft", "blocks/" + this.name).toString()));
        JsonObject alternatives = new JsonObject();
        alternatives.add("type", new JsonPrimitive("minecraft:alternatives"));
        JsonArray children = new JsonArray();
        children.add(group);
        children.add(lootTable);
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
        return new Identifier(Scicraft.MOD_ID, name);
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


