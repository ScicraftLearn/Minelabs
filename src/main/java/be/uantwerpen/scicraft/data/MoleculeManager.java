package be.uantwerpen.scicraft.data;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

//Made after the loot/recipe system
public class MoleculeManager extends JsonDataLoader implements IdentifiableResourceReloadListener{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private Map<Item, MolecularMaterial> molecules = ImmutableMap.of();

	public MoleculeManager() {
		super(GSON, "molecules");
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> map, ResourceManager var2, Profiler var3) {
		ImmutableMap.Builder<Item, MolecularMaterial> builder = ImmutableMap.builder();
		map.forEach((id, json) -> {
			try { //Parse the json file
				JsonArray blocks = JsonHelper.getArray(json.getAsJsonObject(), "blocks");
				JsonArray molecules = JsonHelper.getArray(json.getAsJsonObject(), "molecules");
				JsonArray distribution = JsonHelper.getArray(json.getAsJsonObject(), "distribution");
				if (molecules.size() != distribution.size()) {
					throw new IllegalStateException("Couldn't parse molecule in " + id + " molecule count and distribution does not match");
				}
				for (JsonElement element: blocks) {
					Item item = JsonHelper.asItem(element, "item");
					MolecularMaterial moleculematerial = new MolecularMaterial(molecules, distribution); 
					builder.put(item, moleculematerial);
				}
				/**
				 * Some quick remarks: 
				 * - Maybe 1 file for each block, or one file for each block "type". Having one big file is not "bad", but it is difficult to search in.
				 * - Store the results in a map block/item -> MolecularMaterial, so it can be found easily by searching for the block/item.
				 * - Make use of datagen to make the json files? Once it's setup, changes are really simple.
				 */
			}
			catch (Exception e) {
				LOGGER.error("Couldn't parse molecule {}", id, (Object)e);
			}
		});
		this.molecules = builder.build();
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(Scicraft.MOD_ID, "molecule");
	}
	
	public Map<Item, MolecularMaterial> getMolecules() {
		return molecules;
	}
	
	public MolecularMaterial getMoleculeMaterialFor(Item item) {
		if (!this.molecules.containsKey(item)) {
			return null;
		}
		return this.molecules.get(item);
	}
	
	/**
	 * Gets a random molecule as a String, based on the distribution.
	 * @param item
	 * @return The molecule, or an empty String when no molecule could be found
	 */
	public String getMoleculeFor(Item item) {
		MolecularMaterial m = getMoleculeMaterialFor(item);
		if (m == null) {
			return "";
		}
		return m.getMolecule();
	}
}
