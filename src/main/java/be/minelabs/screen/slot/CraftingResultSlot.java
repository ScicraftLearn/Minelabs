package be.minelabs.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

/**
 * General result slot.
 * Only allows taking items
 */
public class CraftingResultSlot extends Slot {

    public CraftingResultSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }
}
