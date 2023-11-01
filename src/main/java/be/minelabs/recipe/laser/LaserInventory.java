package be.minelabs.recipe.laser;

import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LaserInventory extends SimpleInventory implements SidedInventory {
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


    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            return new int[]{1, 2, 3, 4, 5};
        }

        return new int[]{0};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot == 0 && dir != Direction.DOWN;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        if (dir == Direction.DOWN) {
            return slot >= 1 && slot <= 5;
        }
        return false;
    }
}
