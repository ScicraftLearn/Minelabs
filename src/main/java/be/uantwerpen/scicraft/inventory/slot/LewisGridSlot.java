package be.uantwerpen.scicraft.inventory.slot;

import be.uantwerpen.scicraft.item.AtomItem;
import be.uantwerpen.scicraft.lewisrecipes.Atom;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class LewisGridSlot extends Slot {

    public LewisGridSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (this.isLocked()) return false;
        if (stack == null || stack.getItem().equals(Items.AIR))
            this.setStack(stack);
        else if (stack.getItem() instanceof AtomItem) {
            Atom atom = ((AtomItem) stack.getItem()).getAtom();
            if (atom != null) {
                this.setStack(atom.getItem().getDefaultStack());
                this.getStack().getOrCreateNbt().putBoolean("ScicraftItemInLCT", true);
            }
        }
        return false;
    }

    public boolean isLocked() {
        return false;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        if (this.isLocked()) return false;
        return super.canTakeItems(playerEntity);
    }

    @Override
    public ItemStack takeStack(int amount) {
        this.inventory.removeStack(this.getIndex(), amount);
        return ItemStack.EMPTY;
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }
}