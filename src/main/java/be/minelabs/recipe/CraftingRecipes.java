package be.minelabs.recipe;

import be.minelabs.Minelabs;
import be.minelabs.recipe.ionic.IonicRecipe;
import be.minelabs.recipe.laser.LaserRecipe;
import be.minelabs.recipe.lewis.MoleculeRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class CraftingRecipes {

    public static void onInitialize() {
        // Ionic Recipe
        Registry.register(Registries.RECIPE_SERIALIZER,
                new Identifier(Minelabs.MOD_ID, IonicRecipe.IonicRecipeSerializer.ID),
                IonicRecipe.IonicRecipeSerializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE,
                new Identifier(Minelabs.MOD_ID, IonicRecipe.IonicRecipeType.ID),
                IonicRecipe.IonicRecipeType.INSTANCE);

        //Lewis Recipe
        Registry.register(Registries.RECIPE_SERIALIZER,
                new Identifier(Minelabs.MOD_ID, MoleculeRecipe.MoleculeRecipeSerializer.ID),
                MoleculeRecipe.MoleculeRecipeSerializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE,
                new Identifier(Minelabs.MOD_ID, MoleculeRecipe.MoleculeRecipeType.ID),
                MoleculeRecipe.MoleculeRecipeType.INSTANCE);


        Registry.register(Registries.RECIPE_SERIALIZER,
                new Identifier(Minelabs.MOD_ID, LaserRecipe.Serializer.ID),
                LaserRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE,
                new Identifier(Minelabs.MOD_ID, LaserRecipe.Type.ID),
                LaserRecipe.Type.INSTANCE);
    }
}
