package be.uantwerpen.scicraft.gui.ionic_gui.slots;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class IonicGridSlot extends Slot {


    public IonicGridSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }


    //TODO change with recipes
    @Override
    public boolean canInsert(ItemStack stack) {
        return AbstractFurnaceBlockEntity.canUseAsFuel(stack);
    }

    @Override
    public int getMaxItemCount() {
        return super.getMaxItemCount();
    }
}
