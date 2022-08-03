package be.uantwerpen.scicraft.crafting;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.crafting.ionic.IonicRecipe;
import be.uantwerpen.scicraft.crafting.lewis.MoleculeRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CraftingRecipes {

    public static RecipeType<IonicRecipe> IONIC_CRAFTING = RecipeType.register("ionic_crating");
    public static RecipeType<MoleculeRecipe> MOLECULE_CRAFTING = RecipeType.register("molecule_crafting");

    public static void register() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Scicraft.MOD_ID, "ionic_crafting"), IonicRecipe.IonicRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Scicraft.MOD_ID, "molecule_crafting"), MoleculeRecipe.MoleculeRecipeSerializer.INSTANCE);
    }


}
