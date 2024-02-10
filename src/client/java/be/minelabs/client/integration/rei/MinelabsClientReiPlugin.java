package be.minelabs.client.integration.rei;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.client.gui.screen.IonicScreen;
import be.minelabs.client.gui.screen.LewisScreen;
import be.minelabs.client.integration.rei.categories.IonicCategory;
import be.minelabs.client.integration.rei.categories.LewisCategory;
import be.minelabs.client.integration.rei.displays.IonicDisplay;
import be.minelabs.client.integration.rei.displays.LewisDisplay;
import be.minelabs.recipe.ionic.IonicRecipe;
import be.minelabs.recipe.lewis.MoleculeRecipe;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ClickArea;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class MinelabsClientReiPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new LewisCategory());
        registry.addWorkstations(LewisCategory.MOLECULE_CRAFTING,
                EntryStacks.of(Blocks.LEWIS_BLOCK), EntryStacks.of(Blocks.LAB_LEWIS));

        registry.add(new IonicCategory());
        registry.addWorkstations(IonicCategory.IONIC_CRAFTING, EntryStacks.of(Blocks.IONIC_BLOCK));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(MoleculeRecipe.class, MoleculeRecipe.MoleculeRecipeType.INSTANCE, LewisDisplay::new);
        registry.registerRecipeFiller(IonicRecipe.class, IonicRecipe.IonicRecipeType.INSTANCE, IonicDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(screen -> new Rectangle(100, 50, 25, 20),
                LewisScreen.class, LewisCategory.MOLECULE_CRAFTING);
        registry.registerContainerClickArea(screen -> new Rectangle(146, 38, 23, 19),
                IonicScreen.class, IonicCategory.IONIC_CRAFTING);
    }
}
