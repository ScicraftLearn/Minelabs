package be.minelabs.client.integration.emi.recipes;

import be.minelabs.client.integration.emi.MinelabsEmiPlugin;
import be.minelabs.item.Items;
import be.minelabs.recipe.lewis.MoleculeRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.registry.DynamicRegistryManager;

public class LewisEmiRecipe extends BasicEmiRecipe {

    private final EmiIngredient container;
    private final int time;

    public LewisEmiRecipe(MoleculeRecipe recipe) {
        super(MinelabsEmiPlugin.LEWIS_CATEGORY, recipe.getId(), 160, 110);
        recipe.getIngredients().forEach(ingredient -> inputs.add(EmiIngredient.of(ingredient)));
        if (recipe.needsContainer()) {
            container = EmiStack.of(Items.ERLENMEYER);
        } else {
            container = EmiStack.EMPTY;
        }
        time = recipe.getTime();
        outputs.add(EmiStack.of(recipe.getOutput(DynamicRegistryManager.EMPTY)));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 105, 44);
        widgets.addFillingArrow(105, 44, time * 50);

        // TODO GRAPH POS
        for (int m = 0; m < 5; ++m) {
            for (int l = 0; l < 5; ++l) {
                widgets.addSlot(8 + l * 18, 8 + m * 18);
            }
        }

        if (!container.isEmpty()) {
            widgets.addSlot(container, 120, 74);
        } else {
            widgets.addSlot(120, 74);
        }

        widgets.addSlot(outputs.get(0), 140, 44);
    }
}
