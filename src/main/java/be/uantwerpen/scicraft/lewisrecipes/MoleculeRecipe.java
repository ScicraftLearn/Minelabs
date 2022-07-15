package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.Scicraft;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

public class MoleculeRecipe implements Recipe<CraftingInventory> {
    private final ItemStack outputStack;
    private final Identifier id;

    public MoleculeRecipe(ItemStack outputStack, Identifier id) {
        this.outputStack = outputStack;
        this.id = id;
        Scicraft.LOGGER.debug("Recipe made: " + id.toString());
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
            if (recipeJson.result == null) {
                throw new JsonSyntaxException("A required attribute is missing!");
            }
            // We'll allow to not specify the output, and default it to 1.
            if (recipeJson.result.count == 0) recipeJson.result.count = 1;

            Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.result.item))
                    // Validate the entered item actually exists
                    .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.result.item));
            ItemStack output = new ItemStack(outputItem, recipeJson.result.count);

            return new MoleculeRecipe(output, id);
        }

        @Override
        public MoleculeRecipe read(Identifier id, PacketByteBuf buf) {
            ItemStack output = buf.readItemStack();
            return new MoleculeRecipe(output, id);
        }

        @Override
        public void write(PacketByteBuf buf, MoleculeRecipe recipe) {
            buf.writeItemStack(recipe.getOutput());
        }
    }

}
