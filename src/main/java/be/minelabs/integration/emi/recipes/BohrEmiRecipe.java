package be.minelabs.integration.emi.recipes;

import be.minelabs.Minelabs;
import be.minelabs.integration.emi.MinelabsEMIPlugin;
import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import be.minelabs.science.Atom;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class BohrEmiRecipe extends BasicEmiRecipe {
    public BohrEmiRecipe(AtomItem atomItem) {
        super(MinelabsEMIPlugin.BOHR_CATEGORY, new Identifier(Minelabs.MOD_ID, ""), 160, 100);
        Atom atom = atomItem.getAtom();
        inputs.add(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(Items.ELECTRON)), atom.getAtomNumber()));
        inputs.add(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(Items.NEUTRON)), atom.getInitialNeutrons()));
        inputs.add(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(Items.PROTON)), atom.getAtomNumber()));
        outputs.add(EmiStack.of(atomItem));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {

    }
}
