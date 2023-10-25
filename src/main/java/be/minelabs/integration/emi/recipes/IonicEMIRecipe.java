package be.minelabs.integration.emi.recipes;

import be.minelabs.integration.emi.MinelabsEMIPlugin;
import be.minelabs.item.Items;
import be.minelabs.recipe.ionic.IonicRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

public class IonicEMIRecipe extends BasicEmiRecipe {

    private final EmiIngredient container;

    public IonicEMIRecipe(IonicRecipe recipe) {
        super(MinelabsEMIPlugin.IONIC_CATEGORY, recipe.getId(), 160, 100);
        if (recipe.needsContainer()) {
            container = EmiStack.of(Items.ERLENMEYER);
        } else {
            container = EmiStack.EMPTY;
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 105, 44);

        if (!container.isEmpty()) {
            widgets.addSlot(container, 120, 74);
        } else {
            widgets.addSlot(120, 74);
        }

        widgets.addSlot(outputs.get(0), 140, 44);
    }
}
