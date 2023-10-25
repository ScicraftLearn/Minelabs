package be.minelabs.integration.rei;

import be.minelabs.block.Blocks;
import be.minelabs.integration.rei.categories.LewisCategory;
import be.minelabs.integration.rei.displays.LewisDisplay;
import be.minelabs.recipe.lewis.MoleculeRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class MinelabsReiPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new LewisCategory());
        registry.addWorkstations(LewisCategory.MOLECULE_CRAFTING, EntryStacks.of(Blocks.LEWIS_BLOCK));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(MoleculeRecipe.class, MoleculeRecipe.MoleculeRecipeType.INSTANCE, LewisDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        // todo click arrow for possible recipes
    }
}
