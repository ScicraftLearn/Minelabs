package be.uantwerpen.scicraft.data;

import be.uantwerpen.scicraft.Scicraft;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.function.Consumer;

public class LaserToolDataProvider implements DataProvider {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private final DataGenerator root;
	
	public LaserToolDataProvider(DataGenerator root) {
		this.root = root;
	}

	@Override
	public void run(DataWriter writer) throws IOException {
		Path path = this.root.getOutput();
		HashSet<Identifier> set = Sets.newHashSet();
		LaserToolDataProvider.generate(provider -> {
			// TODO check if block exist ingame and otherwhise try to expand the list
			if (!set.add(provider.getId())) {
				throw new IllegalStateException("Duplicate block " + provider.getId());
			}
			LaserToolDataProvider.saveMoleculeData(writer, provider.toJson(), path.resolve("data/" + provider.getId().getNamespace() + "/loot_tables/blocks/" + provider.getId().getPath() + ".json"));
		}, path);

		saveLasertoolMineable(writer, set);
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
	
	public static void generate(Consumer<MoleculeData> data, Path path) {
		try {
			// Input JSON can't be in generated directory bc this one get reset everytime datagen runs
			path = path.getParent();
			JsonReader reader = new JsonReader(new FileReader(path.resolve("lasertool.json").toFile()));
			JsonArray json = GSON.fromJson(reader, JsonArray.class);

			for (JsonElement jsonElement: json) {
				JsonArray blocks = JsonHelper.getArray(jsonElement.getAsJsonObject(), "blocks");
				JsonArray molecules = JsonHelper.getArray(jsonElement.getAsJsonObject(), "molecules");
				JsonArray distribution = JsonHelper.getArray(jsonElement.getAsJsonObject(), "distribution");
				if (molecules.size() != distribution.size()) {
					throw new IllegalStateException("Couldn't parse molecule in " + blocks.toString() + " molecule count and distribution does not match");
				}
				for (JsonElement element: blocks) {
					generateMoleculeData(element.getAsString(), molecules, distribution, data);
				}
			}

		} catch (IllegalArgumentException | IOException | JsonParseException exception) {
			LOGGER.error("Couldn't parse data file {}", "lasertool.json", exception);
		}
	}

	@Override
	public String getName() {
		return "Molecules";
	}
	
	public static void generateMoleculeData(String name, JsonArray molecules, JsonArray distribution, Consumer<MoleculeData> exporter) { //Quick and dirty method. Can and should be made better
		MoleculeData data = new MoleculeData(name, molecules, distribution);
		exporter.accept(data);
	}
}
