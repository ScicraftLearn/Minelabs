package be.minelabs.client.integration.emi.recipes;

import be.minelabs.Minelabs;
import be.minelabs.client.integration.emi.MinelabsEmiPlugin;
import be.minelabs.science.coulomb.CoulombData;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class CoulombInfoEmiRecipe extends BasicEmiRecipe {
    private final CoulombData data;

    public CoulombInfoEmiRecipe(Identifier id, CoulombData data) {
        super(MinelabsEmiPlugin.ANNIHILATION_CATEGORY, id, 100, 160);
        this.data = data;
        String[] split = id.getPath().split("/");
        String itemName = split[split.length - 1].split("\\.")[0];

        Item item = Registries.ITEM.get(new Identifier(Minelabs.MOD_ID, "subatomic/" + itemName));
        inputs.add(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(item))));

        outputs.add(EmiStack.of(data.getAnnihilationDrop()));
        if (!data.stable) {
            outputs.add(EmiStack.of(data.getDecayDrop()));
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        float chance = data.decay_chance;
        // TODO FANCY SHIT

    }
}
