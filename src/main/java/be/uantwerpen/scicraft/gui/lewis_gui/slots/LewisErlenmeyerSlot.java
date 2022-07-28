package be.uantwerpen.scicraft.gui.lewis_gui.slots;

import be.uantwerpen.scicraft.item.Items;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

public class LewisErlenmeyerSlot extends Slot {

    public LewisErlenmeyerSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public boolean canInsert(@NotNull ItemStack stack) {
        return stack.getItem().equals(Items.ERLENMEYER);
    }
}
