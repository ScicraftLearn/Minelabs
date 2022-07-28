package be.uantwerpen.scicraft.gui.lewis_gui.slots;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class LewisCraftingResultSlot extends Slot {

    public LewisCraftingResultSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public boolean canInsert(ItemStack stack) {
        return false;
    }
}
