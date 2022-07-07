package be.uantwerpen.scicraft.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class LewisCraftingResultSlot extends Slot {
    private int amount;
    private boolean isReady;

    public LewisCraftingResultSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.isReady = false;
    }

    public boolean canInsert(ItemStack stack) {
        return false;
    }

    public ItemStack takeStack(int amount) {
        if (this.hasStack()) {
            this.amount += Math.min(amount, this.getStack().getCount());
        }
        return super.takeStack(amount);
    }

    protected void onCrafted(ItemStack stack, int amount) {
        this.amount += amount;
        this.onCrafted(stack);
    }

    protected void onTake(int amount) {
        this.amount += amount;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return isReady;
    }

//    @Override
//    public boolean isEnabled() {
//        return true;
//    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
