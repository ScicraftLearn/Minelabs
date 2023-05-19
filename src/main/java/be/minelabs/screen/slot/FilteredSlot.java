package be.minelabs.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Filtered slot
 * Takes an {@link ItemStack} {@link Predicate} to determine if the item is valid
 */
public class FilteredSlot extends Slot {

    private final Predicate<ItemStack> filter;

    public FilteredSlot(Inventory inventory, int index, int x, int y, Predicate<ItemStack> filter) {
        super(inventory, index, x, y);
        this.filter = filter;
    }

    @Override
    public boolean canInsert(@NotNull ItemStack stack) {
        return filter.test(stack);
    }

}
