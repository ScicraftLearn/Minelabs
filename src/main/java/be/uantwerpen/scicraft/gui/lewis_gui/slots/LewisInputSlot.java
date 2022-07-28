package be.uantwerpen.scicraft.gui.lewis_gui.slots;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class LewisInputSlot extends Slot {
    private Item allowedItem;
    private int density = 1;

    public LewisInputSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        allowedItem = null;
    }

    public LewisInputSlot(Inventory inventory, int index, int x, int y, int density) {
        super(inventory, index, x, y);
        allowedItem = null;
        this.density = density;
    }

    public boolean canInsert(ItemStack stack) {
        return allowedItem != null && allowedItem.equals(stack.getItem());
    }

    public Item getAllowedItem() {
        return allowedItem;
    }

    public void setAllowedItem(Item allowedItem) {
        this.allowedItem = allowedItem;
    }

    @Override
    public int getMaxItemCount() {
        return density;
    }

    public void setDensity(int density) {
        this.density = density;
    }
}
