package be.uantwerpen.scicraft.gui;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class LewisInputSlot extends Slot {
    private Item allowedItem;

    public LewisInputSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        allowedItem = null;
    }

    public boolean canInsert(ItemStack stack) {
        return allowedItem != null && allowedItem.equals(stack.getItem());
    }

    public int canInsertCount(ItemStack stack) {
        if (stack == null || stack.getItem().equals(Items.AIR)) return 0;
        if (!stack.isItemEqual(this.getStack())) return 0;
        return this.getStack().getCount() >= this.getMaxItemCount(stack)
                ? 0 : this.getMaxItemCount(stack) - this.getStack().getCount();
    }

    public Item getAllowedItem() {
        return allowedItem;
    }

    public void setAllowedItem(Item allowedItem) {
        this.allowedItem = allowedItem;
    }

    @Override
    public int getMaxItemCount() {
        return 10;
    }
}
