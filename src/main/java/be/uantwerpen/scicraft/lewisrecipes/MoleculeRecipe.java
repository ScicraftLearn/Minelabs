package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.Scicraft;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MoleculeRecipe implements Recipe<CraftingInventory> {

    private final MoleculeGraph structure;
    private final ItemStack outputStack;
    private final Identifier id;
    private final JsonObject json;

    public MoleculeRecipe(MoleculeGraph structure, ItemStack outputStack, Identifier id, JsonObject json) {
        this.structure = structure;
        this.outputStack = outputStack;
        this.id = id;
        this.json = json;
        Scicraft.LOGGER.info("Recipe made: " + id.toString());
        Scicraft.LOGGER.info(getJson().toString());
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        return false;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        return getOutput().copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        // Do not modify the output as this is a direct reference not a copy. Use craft instead if you want a copy.
        return outputStack;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public JsonObject getJson(){
        return json;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MoleculeRecipeSerializer.INSTANCE;
    }

    // Should get loaded if at least one crafting station supports the recipe type and imports it.
    public static RecipeType<MoleculeRecipe> MOLECULE_CRAFTING;

    public static void register(){
        MOLECULE_CRAFTING = Registry.register(Registry.RECIPE_TYPE, new Identifier(Scicraft.MOD_ID, "molecule_crafting"), new RecipeType<MoleculeRecipe>(){});
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Scicraft.MOD_ID, "molecule_crafting"), MoleculeRecipeSerializer.INSTANCE);
    }

    @Override
    public RecipeType<?> getType() {
        return MOLECULE_CRAFTING;
    }

    public static class MoleculeRecipeSerializer implements RecipeSerializer<MoleculeRecipe> {
        private MoleculeRecipeSerializer() {}

        public static final MoleculeRecipeSerializer INSTANCE = new MoleculeRecipeSerializer();

        @Override
        public MoleculeRecipe read(Identifier id, JsonObject json) {
            MoleculeRecipeJsonFormat recipeJson = new Gson().fromJson(json, MoleculeRecipeJsonFormat.class);
            // Validate all fields are there
            if (recipeJson.result == null)
                throw new JsonSyntaxException("Attribute 'result' is missing");
            if (recipeJson.structure == null)
                throw new JsonSyntaxException("Attribute 'structure' is missing");

            Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.result.item))
                    // Validate the entered item actually exists
                    .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.result.item));
            ItemStack output = new ItemStack(outputItem, 1);

            return new MoleculeRecipe(recipeJson.structure.get(), output, id, json);
        }

        @Override
        public MoleculeRecipe read(Identifier id, PacketByteBuf buf) {
            // TODO: untested code
            // Writing graph optimized to bytes is hard -> just send json over.
            String jsonStr = buf.readString();
            JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
            return read(id, json);
        }

        @Override
        public void write(PacketByteBuf buf, MoleculeRecipe recipe) {
            // Writing graph optimized to bytes is hard -> just send json over.
            Scicraft.LOGGER.info(recipe.getJson().toString());
            buf.writeString(recipe.getJson().toString());
        }
    }

}
