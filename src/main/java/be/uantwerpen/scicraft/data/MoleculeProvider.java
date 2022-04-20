package be.uantwerpen.scicraft.data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import be.uantwerpen.scicraft.Scicraft;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

//Made after the recipe system
public class MoleculeProvider implements DataProvider{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private DataGenerator root;
	
	public MoleculeProvider(DataGenerator root) {
		this.root = root;
	}
	
	@Override
	public void run(DataCache cache) throws IOException {
		Path path = this.root.getOutput();
		HashSet<Identifier> set = Sets.newHashSet();
		MoleculeProvider.generate(provider -> {
			if (!set.add(provider.getId())) {
				throw new IllegalStateException("Duplicate recipe " + provider.getId());
			}
			MoleculeProvider.saveMoleculeData(cache, provider.toJson(), path.resolve("data/" + provider.getId().getNamespace() + "/molecules/" + provider.getId().getPath() + ".json"));
		});
		
	}
	
	private static void saveMoleculeData(DataCache cache, JsonObject json, Path path) {
		try {
			String string = GSON.toJson(json);
			String string2 = SHA1.hashUnencodedChars(string).toString();
			if (!Objects.equals(cache.getOldSha1(path), string2) || !Files.exists(path, new LinkOption[0])) {
				Files.createDirectories(path.getParent(), new FileAttribute[0]);
				try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, new OpenOption[0]);){
					bufferedWriter.write(string);
				}
			}
			cache.updateSha1(path, string2);
		}
		catch (IOException string) {
			LOGGER.error("Couldn't save molecule data {}", (Object)path, (Object)string);
		}
	}
	
	public static void generate(Consumer<MoleculeData> data) {
		defaultMoleculeData("stone", new Item[] { Items.STONE}, new String[] {"SiO_2"}, new Integer[] {100}, data); //creates the data file
		
	}
	
	@Override
	public String getName() {
		return "Molecules";
	}
	
	public static void defaultMoleculeData(String name, Item[] items, String[] molecules, Integer[] distribution, Consumer<MoleculeData> exporter) { //Quick and dirty method. Can and should be made better
		MoleculeData data = new MoleculeData() {
			
			@Override
			public void serialize(JsonObject json) {
				JsonArray itemarray = new JsonArray();
				JsonArray moleculearray = new JsonArray();
				JsonArray distributionarray = new JsonArray();
				for (Item item: items) {
					itemarray.add(Registry.ITEM.getId(item).toString());
				}
				for (String string: molecules) {
					moleculearray.add(string);
				}
				for (Integer i: distribution) {
					distributionarray.add(i);
				}
				json.add("blocks", itemarray);
				json.add("molecules", moleculearray);
				json.add("distribution", distributionarray);
				if (moleculearray.size() != distributionarray.size()) {
					throw new IllegalStateException("Couldn't make molecule in " + new Identifier(Scicraft.MOD_ID,name) + " molecule count and distribution does not match");
				}
			}
			
			@Override
			public Identifier getId() {
				return new Identifier(Scicraft.MOD_ID,name);
			}
		};
		exporter.accept(data);
	}
}
