package be.uantwerpen.scicraft.inventory.slot;

import be.uantwerpen.scicraft.item.Items;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

public class LewisErlenmeyerSlot extends Slot {

    public LewisErlenmeyerSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(@NotNull ItemStack stack) {
        return stack.getItem().equals(Items.ERLENMEYER);
    }

}
