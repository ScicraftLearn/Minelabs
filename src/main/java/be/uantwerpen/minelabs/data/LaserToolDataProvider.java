package be.uantwerpen.minelabs.data;

import be.uantwerpen.minelabs.Minelabs;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class LaserToolDataProvider implements DataProvider {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private final FabricDataOutput dataOutput;

	public static HashMap<String, Map<String, Integer>> moleculeNames = new HashMap<>();

	private static List<Identifier> expand = new ArrayList<>();
	private static List<Identifier> expandColor = new ArrayList<>();
	private static List<Identifier> expandWood = new ArrayList<>();
	private static List<Identifier> expandLeaves = new ArrayList<>();
	private static List<Identifier> expandCoral = new ArrayList<>();
	
	public LaserToolDataProvider(FabricDataOutput dataOutput) {
		// TODO CHECK
		this.dataOutput = dataOutput;
	}

	@Override
	public CompletableFuture<?> run(DataWriter writer) {
		Path path = dataOutput.getPath();
		Set<Identifier> set = Sets.newLinkedHashSet();
		LaserToolDataProvider.generateMinecraftBlocksData(provider -> {
			if (!set.add(provider.getId())) {
				throw new IllegalStateException("Duplicate block " + provider.getId());
			}
			LaserToolDataProvider.saveMinecraftBlocksData(writer, provider.toJson(), path.resolve("data/" + Minelabs.MOD_ID + "/loot_tables/lasertool/blocks/" + provider.getId().getPath() + ".json"));
		}, path);

		LaserToolDataProvider.generateMoleculeData(provider -> LaserToolDataProvider.saveMoleculeData(writer, provider.toJson(), path.resolve("data/" + provider.getId().getNamespace() + "/loot_tables/molecules/" + provider.getId().getPath() + ".json")));

		return saveLasertoolMineable(writer, set);
	}

	private static void saveMinecraftBlocksData(DataWriter cache, JsonObject json, Path path) {
		DataProvider.writeToPath(cache, json, path);
	}

	private static void saveMoleculeData(DataWriter cache, JsonObject json, Path path) {
		DataProvider.writeToPath(cache, json, path);
	}

	private CompletableFuture<?> saveLasertoolMineable(DataWriter cache, Set<Identifier> set) {
		Path path = dataOutput.getPath();

		JsonObject root = new JsonObject();
		root.add("replace", new JsonPrimitive(false));
		JsonArray values = new JsonArray();
		for (Identifier id : set) {
			values.add(new JsonPrimitive(id.toString()));
		}
		root.add("values", values);
		return DataProvider.writeToPath(cache, root, path.resolve("data/" + Minelabs.MOD_ID + "/tags/blocks/lasertool_mineable.json"));
	}
	
	public static void generateMinecraftBlocksData(Consumer<MinecraftBlocksData> data, Path input) {
		try {
			// Input JSON can't be in generated directory bc this one get reset everytime datagen runs
			input = input.getParent();
			JsonReader reader = new JsonReader(new FileReader(input.resolve("lasertool.json").toFile()));
			JsonObject json = GSON.fromJson(reader, JsonObject.class);

			readExpandArrays(json);
			JsonArray dataArray = JsonHelper.getArray(json, "data");
			for (JsonElement jsonElement: dataArray) {
				JsonArray blocks = JsonHelper.getArray(jsonElement.getAsJsonObject(), "blocks");
				JsonArray molecules = JsonHelper.getArray(jsonElement.getAsJsonObject(), "molecules");
				JsonArray distribution = JsonHelper.getArray(jsonElement.getAsJsonObject(), "distribution");
				if (molecules.size() != distribution.size()) {
					throw new IllegalStateException("Couldn't parse molecule in " + blocks.toString() + " molecule count and distribution does not match");
				}
				for (JsonElement element: blocks) {
					List<Identifier> expandedBlocks = expandBlock(element.getAsString());
					for (Identifier block: expandedBlocks) {
						data.accept(new MinecraftBlocksData(block, molecules, distribution));
					}
				}
			}

		} catch (IllegalArgumentException | IOException | JsonParseException exception) {
			LOGGER.error("Couldn't parse data file {}", "lasertool.json", exception);
		}
	}

	public static List<Identifier> expandBlock(String name) {
		List<Identifier> result = new ArrayList<>();
		// Add the standard block to the list if it exists ingame
		boolean notABlock = false;
		Identifier id = new Identifier(name);
		if (!Registries.BLOCK.get(id).equals(Registries.BLOCK.get(Registries.BLOCK.getDefaultId()))) {
			result.add(id);
		} else {
			notABlock = true;
		}
		// For bricks and tiles expansion we need to cutoff the s at the end before we expand them
		if (name.endsWith("_bricks") || name.endsWith("_tiles")) {
			name = name.substring(0, name.length() - 1);
		}
		List<Identifier> blocks = new ArrayList<>();
		if (expand.contains(id)) {
			for (String fix: new String[] {"_slab", "_stairs", "_wall", "_block", "_pillar"}) {
				blocks.add(new Identifier(id.getNamespace(), id.getPath() + fix));
			}
		} else if (expandColor.contains(id)) {
			for (String fix: new String[] {"white_", "orange_", "magenta_", "light_blue_", "yellow_", "lime_", "pink_", "gray_", "light_gray_", "cyan_", "purple_", "blue_", "brown_", "green_", "red_", "black_"}) {
				blocks.add(new Identifier(id.getNamespace(), fix + id.getPath()));
			}
		} else if (expandWood.contains(id)) {
			for (String fix: new String[] {"_log", "_planks", "_slab", "_stairs", "_wood", "_stem", "_hyphae"}) {
				blocks.add(new Identifier(id.getNamespace(), id.getPath() + fix));
			}
			blocks.add(new Identifier(id.getNamespace(), "stripped_" +  id.getPath() + "_log"));
			blocks.add(new Identifier(id.getNamespace(), "stripped_" +  id.getPath() + "_wood"));
			blocks.add(new Identifier(id.getNamespace(), "stripped_" +  id.getPath() + "_stem"));
			blocks.add(new Identifier(id.getNamespace(), "stripped_" +  id.getPath() + "_hyphae"));
		} else if (expandLeaves.contains(id)) {
			for (String fix: new String[] {"oak_", "spruce_", "birch_", "jungle_", "acacia_", "dark_oak_", "mangrove_", "azalea_", "flowering_azalea_"}) {
				blocks.add(new Identifier(id.getNamespace(), fix + id.getPath()));
			}
		} else if (expandCoral.contains(id)) {
			String[] expansion = new String[] {"tube_", "brain_", "bubble_", "fire_", "horn_"};
			if (name.equals("dead_coral")) {
				for (String fix: expansion) {
					blocks.add(new Identifier(id.getNamespace(), "dead_" + fix + "coral"));
				}
			} else {
				for (String fix: expansion) {
					blocks.add(new Identifier(id.getNamespace(), fix + id.getPath()));
				}
			}
		} else if (notABlock) {
			LOGGER.error("Block {} in lasertool.json is not an ingame block", name);
		}
		for (Identifier block: blocks) {
			if (!Registries.BLOCK.get(block).equals(Registries.BLOCK.get(Registries.BLOCK.getDefaultId()))) {
				result.add(block);
			}
		}
		return result;
	}

	public static boolean isExpandable(Identifier block) {
		return expand.contains(block) || expandColor.contains(block) || expandWood.contains(block) || expandLeaves.contains(block) || expandCoral.contains(block);
	}

	private static List<Identifier> readListOfIds(JsonElement json){
		Type listType = new TypeToken<List<String>>() {}.getType();
		List<String> strings = GSON.fromJson(json, listType);
		return strings.stream().map(Identifier::new).toList();
	}

	private static void readExpandArrays(JsonObject json) {
		expand = readListOfIds(json.get("expand"));
		expandColor = readListOfIds(json.get("expand_color"));
		expandWood = readListOfIds(json.get("expand_wood"));
		expandLeaves = readListOfIds(json.get("expand_leaves"));
		expandCoral = readListOfIds(json.get("expand_coral"));
	}

	private static void generateMoleculeData(Consumer<MoleculeData> data) {
		for (Map.Entry<String, Map<String, Integer>> entry : moleculeNames.entrySet()) {
			data.accept(new MoleculeData(entry.getKey(), entry.getValue()));
		}
	}

	@Override
	public String getName() {
		return "Molecules";
	}
}
