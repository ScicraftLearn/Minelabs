package be.minelabs.inventory;

import be.minelabs.item.Items;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;

public class AtomicInventory extends OrderedInventory {

    // Allows stacks with more then 64 inside of the inventory
    private final int MAX_SIZE;

    public AtomicInventory(int stack_size) {
        super(Items.ATOMS.size());
        MAX_SIZE = stack_size;
    }

    // Save inventory to NBT if it's the Atom Pack
    @Override
    public void onClose(PlayerEntity player) {
        super.onClose(player);
        if (player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.ATOM_PACK){
            NbtCompound nbt = player.getStackInHand(Hand.MAIN_HAND).getOrCreateNbt();
            Inventories.writeNbt(nbt, this.stacks);
        }
    }

    // Load inventory from NBT if it's the Atom Pack
    @Override
    public void onOpen(PlayerEntity player) {
        if (player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.ATOM_PACK){
            Inventories.readNbt(player.getStackInHand(Hand.MAIN_HAND).getOrCreateNbt(), this.stacks);
        }
        super.onOpen(player);

    }

    @Override
    public int getMaxCountPerStack() {
        // TODO OVERRIDE ITEM MAX STACK SIZE
        return MAX_SIZE;
    }
}
