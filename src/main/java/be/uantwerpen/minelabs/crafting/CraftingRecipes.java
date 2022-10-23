package be.uantwerpen.minelabs.crafting;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.crafting.ionic.IonicRecipe;
import be.uantwerpen.minelabs.crafting.lewis.MoleculeRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CraftingRecipes {

    public static RecipeType<IonicRecipe> IONIC_CRAFTING = RecipeType.register("ionic_crating");
    public static RecipeType<MoleculeRecipe> MOLECULE_CRAFTING = RecipeType.register("molecule_crafting");

    public static void register() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Minelabs.MOD_ID, "ionic_crafting"), IonicRecipe.IonicRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Minelabs.MOD_ID, "molecule_crafting"), MoleculeRecipe.MoleculeRecipeSerializer.INSTANCE);
    }


}
