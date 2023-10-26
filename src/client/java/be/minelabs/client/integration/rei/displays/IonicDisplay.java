package be.minelabs.client.integration.rei.displays;

import be.minelabs.client.integration.rei.categories.IonicCategory;
import be.minelabs.client.integration.rei.util.Util;
import be.minelabs.recipe.ionic.IonicRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.registry.DynamicRegistryManager;

import java.util.List;

public class IonicDisplay extends BasicDisplay {


    public IonicDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    public IonicDisplay(IonicRecipe recipe) {
        super(Util.getInputList(recipe.getIngredients()),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.getOutput(DynamicRegistryManager.EMPTY)))));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return IonicCategory.IONIC_CRAFTING;
    }
}
