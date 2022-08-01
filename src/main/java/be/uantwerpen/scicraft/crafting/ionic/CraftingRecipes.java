package be.uantwerpen.scicraft.crafting.ionic;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.lewisrecipes.MoleculeRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CraftingRecipes {

    public static RecipeType<IonicRecipe> IONIC_CRAFTING = RecipeType.register("ionic_crating");

    public static void register() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Scicraft.MOD_ID, "ionic_crafting"), IonicRecipe.IonicRecipeSerializer.INSTANCE);
    }


}
