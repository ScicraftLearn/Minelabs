package be.minelabs.integration.rei.displays;

import be.minelabs.integration.rei.categories.LewisCategory;
import be.minelabs.recipe.lewis.MoleculeRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LewisDisplay extends BasicDisplay {

    public LewisDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    public LewisDisplay(MoleculeRecipe recipe) {
        super(getInputList(recipe.getIngredients()),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.getOutput(DynamicRegistryManager.EMPTY)))));
    }


    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return LewisCategory.MOLECULE_CRAFTING;
    }

    private static List<EntryIngredient> getInputList(DefaultedList<Ingredient> ingredients) {
        if (ingredients.isEmpty()) return Collections.emptyList();
        List<EntryIngredient> list = new ArrayList<>();
        ingredients.forEach(EntryIngredients::ofIngredient);
        return list;
    }
}
