package be.minelabs.recipe.laser;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class LaserInventory extends SimpleInventory {
    public LaserInventory() {
        super(6);
    }

    public ItemStack getInputStack() {
        return getStack(0);
    }

    public List<ItemStack> getOuputStacks() {
        return List.of(getStack(1), getStack(2), getStack(3), getStack(4), getStack(5));
    }

    public ItemStack getOutputStack(int index) {
        if (index > 5 || index < 1) {
            throw new IllegalArgumentException("Output index index ranges from 1-5, not: " + index);
        }
        return getStack(index);
    }

    public boolean isOutputEmpty() {
        return getOutputStack(1).isEmpty() && getOutputStack(2).isEmpty()
                && getOutputStack(3).isEmpty() && getOutputStack(4).isEmpty()
                && getOutputStack(5).isEmpty();
    }
}
