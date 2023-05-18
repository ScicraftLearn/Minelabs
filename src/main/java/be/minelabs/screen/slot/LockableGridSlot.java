package be.minelabs.screen.slot;

import be.minelabs.item.items.AtomItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

/**
 * Lockable slot
 * Only allows changes if unlocked
 */
public class LockableGridSlot extends Slot {

    public LockableGridSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (this.isLocked()) return false;
        if (stack == null || stack.getItem().equals(Items.AIR))
            this.setStack(stack);
        else if (stack.getItem() instanceof AtomItem atom) {
            switch (atom.getAtom().getType()) {
                case POST_TRANSITION_METAL:
                case NON_METAL:
                case NOBLE_GAS:
                    this.setStack(atom.getDefaultStack());
                    this.getStack().getOrCreateNbt().putBoolean("MinelabsItemInLCT", true);
                default:
                    return false;
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
