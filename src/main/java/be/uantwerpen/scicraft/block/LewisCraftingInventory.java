package be.uantwerpen.scicraft.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class LewisCraftingInventory implements Inventory {

    private ArrayList<ItemStack> items;

    public LewisCraftingInventory(ArrayList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int size() {
        return 81; // todo should this be a constant of the size of a 9 x 9 grid?
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (this.items.get(slot).getCount() < amount) return null; // Bc can't remove what we don;t have
        this.items.get(slot).setCount(this.items.get(slot).getCount() - amount);
        if (this.items.get(slot).getCount() == 0) {
            this.removeStack(slot);
        }
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot) {
        this.items.get(slot).setCount(0);
        ItemStack stack = this.items.get(slot).copy();
        this.items.set(slot, null);
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.items.set(slot, stack);
    }

    @Override
    public void markDirty() {
        // todo
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false; // todo
    }

    @Override
    public void clear() {
    // todo
    }
}
