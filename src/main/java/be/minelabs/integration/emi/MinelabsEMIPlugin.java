package be.minelabs.integration.emi;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.integration.emi.recipes.LewisEmiRecipe;
import be.minelabs.recipe.lewis.MoleculeRecipe;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

public class MinelabsEMIPlugin implements EmiPlugin {
    private static EmiStack LEWIS_STACK = EmiStack.of(Blocks.LEWIS_BLOCK);
    public static EmiRecipeCategory LEWIS_CATEGORY = new EmiRecipeCategory(
            new Identifier(Minelabs.MOD_ID, "molecule_crafting"), LEWIS_STACK);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(LEWIS_CATEGORY);

        registry.addWorkstation(LEWIS_CATEGORY, LEWIS_STACK);
        RecipeManager manager = registry.getRecipeManager();

        // Use vanilla's concept of your recipes and pass them to your EmiRecipe representation
        for (MoleculeRecipe recipe : manager.listAllOfType(MoleculeRecipe.MoleculeRecipeType.INSTANCE)) {
            registry.addRecipe(new LewisEmiRecipe(recipe));
        }
    }
}
