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

//Made after the recipe system
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
			if (!set.add(provider.getId())) {
				throw new IllegalStateException("Duplicate block " + provider.getId());
			}
			LaserToolDataProvider.saveMoleculeData(writer, provider.toJson(), path.resolve("data/" + provider.getId().getNamespace() + "/loot_tables/blocks/" + provider.getId().getPath() + ".json"));
		}, path);
	}

	private static void saveMoleculeData(DataWriter cache, JsonObject json, Path path) {
		try {
			DataProvider.writeToPath(cache, json, path);
		}
		catch (IOException string) {
			LOGGER.error("Couldn't save molecule data {}", path, string);
		}
	}
	
	public static void generate(Consumer<MoleculeData> data, Path path) {
		try {
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
					defaultMoleculeData(element.getAsString(), molecules, distribution, data);
//					defaultMoleculeData(element.getAsString(), JsonHelper.asArray(element, "item"););
				}
			}

		} catch (IllegalArgumentException | IOException | JsonParseException exception) {
			LOGGER.error("Couldn't parse data file {}", "lasertool.json", exception);
		}
		// TODO write the set to lasertool_mineable.json
	}

	@Override
	public String getName() {
		return "Molecules";
	}
	
	public static void defaultMoleculeData(String name, JsonArray molecules, JsonArray distribution, Consumer<MoleculeData> exporter) { //Quick and dirty method. Can and should be made better
		MoleculeData data = new MoleculeData() {

			@Override
			public void serialize(JsonObject json) {
				json.add("molecules", molecules);
				json.add("distribution", distribution);
				if (molecules.size() != distribution.size()) {
					throw new IllegalStateException("Couldn't make molecule in " + new Identifier(Scicraft.MOD_ID, name) + " molecule count and distribution does not match");
				}
			}
			
			@Override
			public Identifier getId() {
				return new Identifier(Scicraft.MOD_ID, name);
			}
		};
		exporter.accept(data);
	}
}
