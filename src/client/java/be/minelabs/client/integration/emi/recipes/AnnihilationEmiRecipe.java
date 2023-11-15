package be.minelabs.client.integration.emi.recipes;

import be.minelabs.client.integration.emi.MinelabsEmiPlugin;
import be.minelabs.science.coulomb.CoulombData;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class AnnihilationEmiRecipe extends BasicEmiRecipe {
    private final CoulombData data;

    public AnnihilationEmiRecipe(Identifier id, CoulombData data) {
        super(MinelabsEmiPlugin.ANNIHILATION_CATEGORY, id, 100, 160);
        this.data = data;

        inputs.add(EmiIngredient.of(Ingredient.ofStacks(new ItemStack())));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

    }
}
