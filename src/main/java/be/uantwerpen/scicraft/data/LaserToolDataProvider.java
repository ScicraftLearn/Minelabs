package be.uantwerpen.scicraft.data;

import be.uantwerpen.scicraft.Scicraft;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class LaserToolDataProvider implements DataProvider {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private final DataGenerator root;

	public static HashMap<String, Map<String, Integer>> moleculeNames = new HashMap<>();

	private static List<String> expand = new ArrayList<>();
	private static List<String> expandColor = new ArrayList<>();
	private static List<String> expandWood = new ArrayList<>();
	private static List<String> expandLeaves = new ArrayList<>();
	private static List<String> expandCoral = new ArrayList<>();
	
	public LaserToolDataProvider(DataGenerator root) {
		this.root = root;
	}

	@Override
	public void run(DataWriter writer) throws IOException {
		Path path = this.root.getOutput();
		HashSet<Identifier> set = Sets.newHashSet();
		LaserToolDataProvider.generateMinecraftBlocksData(provider -> {
			if (!set.add(provider.getId())) {
				throw new IllegalStateException("Duplicate block " + provider.getId());
			}
			LaserToolDataProvider.saveMinecraftBlocksData(writer, provider.toJson(), path.resolve("data/" + provider.getId().getNamespace() + "/loot_tables/blocks/" + provider.getId().getPath() + ".json"));
		}, path);

		LaserToolDataProvider.generateMoleculeData(provider -> LaserToolDataProvider.saveMoleculeData(writer, provider.toJson(), path.resolve("data/" + provider.getId().getNamespace() + "/loot_tables/molecules/" + provider.getId().getPath() + ".json")));

		saveLasertoolMineable(writer, set);
	}

	private static void saveMinecraftBlocksData(DataWriter cache, JsonObject json, Path path) {
		try {
			DataProvider.writeToPath(cache, json, path);
		}
		catch (IOException string) {
			LOGGER.error("Couldn't save minecraft block data {}", path, string);
		}
	}

	private static void saveMoleculeData(DataWriter cache, JsonObject json, Path path) {
		try {
			DataProvider.writeToPath(cache, json, path);
		}
		catch (IOException string) {
			LOGGER.error("Couldn't save molecule data {}", path, string);
		}
	}

	private void saveLasertoolMineable(DataWriter cache, HashSet<Identifier> set) throws IOException {
		Path path = this.root.getOutput();

		JsonObject root = new JsonObject();
		root.add("replace", new JsonPrimitive(false));
		JsonArray values = new JsonArray();
		for (Identifier id : set) {
			values.add(new JsonPrimitive(new Identifier("minecraft", id.getPath()).toString()));
		}
		root.add("values", values);
		DataProvider.writeToPath(cache, root, path.resolve("data/" + Scicraft.MOD_ID + "/tags/blocks/lasertool_mineable.json"));
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
					List<String> expandedBlocks = expandBlock(element.getAsString());
					for (String block: expandedBlocks) {
						data.accept(new MinecraftBlocksData(block, molecules, distribution));
					}
				}
			}

		} catch (IllegalArgumentException | IOException | JsonParseException exception) {
			LOGGER.error("Couldn't parse data file {}", "lasertool.json", exception);
		}
	}

	public static List<String> expandBlock(String name) {
		List<String> result = new ArrayList<>();
		// Add the standard block to the list if it exists ingame
		boolean notABlock = false;
		if (!Registry.BLOCK.get(new Identifier("minecraft", name)).equals(Registry.BLOCK.get(Registry.BLOCK.getDefaultId()))) {
			result.add(name);
		} else {
			notABlock = true;
		}
		// For bricks and tiles expansion we need to cutoff the s at the end before we expand them
		if (name.endsWith("_bricks") || name.endsWith("_tiles")) {
			name = name.substring(0, name.length() - 1);
		}
		List<String> blocks = new ArrayList<>();
		if (expand.contains(name)) {
			for (String fix: new String[] {"_slab", "_stairs", "_wall", "_block", "_pillar"}) {
				blocks.add(name + fix);
			}
		} else if (expandColor.contains(name)) {
			for (String fix: new String[] {"white_", "orange_", "magenta_", "light_blue_", "yellow_", "lime_", "pink_", "gray_", "light_gray_", "cyan_", "purple_", "blue_", "brown_", "green_", "red_", "black_"}) {
				blocks.add(fix + name);
			}
		} else if (expandWood.contains(name)) {
			for (String fix: new String[] {"_log", "_planks", "_slab", "_stairs", "_wood", "_stem", "_hyphae"}) {
				blocks.add(name + fix);
			}
			blocks.add("stripped_" + name + "_log");
			blocks.add("stripped_" + name + "_wood");
			blocks.add("stripped_" + name + "_stem");
			blocks.add("stripped_" + name + "_hyphae");
		} else if (expandLeaves.contains(name)) {
			for (String fix: new String[] {"oak_", "spruce_", "birch_", "jungle_", "acacia_", "dark_oak_", "mangrove_", "azalea_", "flowering_azalea_"}) {
				blocks.add(fix + name);
			}
		} else if (expandCoral.contains(name)) {
			String[] expansion = new String[] {"tube_", "brain_", "bubble_", "fire_", "horn_"};
			if (name.equals("dead_coral")) {
				for (String fix: expansion) {
					blocks.add("dead_" + fix + "coral");
				}
			} else {
				for (String fix: expansion) {
					blocks.add(fix + name);
				}
			}
		} else if (notABlock) {
			LOGGER.error("Block {} in lasertool.json is not an ingame block", name);
		}
		for (String block: blocks) {
			if (!Registry.BLOCK.get(new Identifier("minecraft", block)).equals(Registry.BLOCK.get(Registry.BLOCK.getDefaultId()))) {
				result.add(block);
			}
		}
		return result;
	}

	public static boolean isExpandable(String block) {
		return expand.contains(block) || expandColor.contains(block) || expandWood.contains(block) || expandLeaves.contains(block) || expandCoral.contains(block);
	}

	private static void readExpandArrays(JsonObject json) {
		Type listType = new TypeToken<List<String>>() {}.getType();
		expand = GSON.fromJson(json.get("expand"), listType);
		expandColor = GSON.fromJson(json.get("expand_color"), listType);
		expandWood = GSON.fromJson(json.get("expand_wood"), listType);
		expandLeaves = GSON.fromJson(json.get("expand_leaves"), listType);
		expandCoral = GSON.fromJson(json.get("expand_coral"), listType);
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
