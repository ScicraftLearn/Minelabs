package be.minelabs.inventory;

import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class AtomicInventory extends SimpleInventory {

    // Allows stacks with more then 64 inside of the inventory
    private final int MAX_SIZE;

    public AtomicInventory(int stack_size) {
        super(Items.ATOMS.size());
        MAX_SIZE = stack_size;
    }

    public AtomicInventory(NbtCompound nbt){
        this(256);
        this.readNbt(nbt);
    }

    // Save inventory to NBT if it's the Atom Pack
    @Override
    public void onClose(PlayerEntity player) {
        super.onClose(player);
        if (player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.ATOM_PACK && MAX_SIZE == 256){
            NbtCompound nbt = player.getStackInHand(Hand.MAIN_HAND).getOrCreateNbt();
            this.writeNbt(nbt);
        }
    }

    // Load inventory from NBT if it's the Atom Pack
    @Override
    public void onOpen(PlayerEntity player) {
        if (player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.ATOM_PACK && MAX_SIZE == 256){
            readNbt(player.getStackInHand(Hand.MAIN_HAND).getOrCreateNbt());
        }
        super.onOpen(player);
    }

    /**
     * Try to fill THIS inventory with the origin
     *
     * @param origin : Inventory to TAKE from
     */
    public void tryToFill(AtomicInventory origin){
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i).getCount() == getMaxCountPerStack() || origin.getStack(i).isEmpty()){
                // Slot if FULL || Nothing to fill with
                continue;
            } else {
                // Try to fill
                if (stacks.get(i).isEmpty()){
                    stacks.set(i, origin.getStack(i).copy());
                    origin.stacks.set(i, ItemStack.EMPTY);
                } else {
                    transfer(origin.stacks.get(i), stacks.get(i));
                }
            }
        }
    }

    /**
     * Try to add the given stack to the inv
     *
     * @param stack : Stack to add (can only be of AtomItem)
     * @return ItemStack.EMPTY if fully inserted OR remainder of the stack
     */
    @Override
    public ItemStack addStack(ItemStack stack) {
        if (stack.getItem() instanceof AtomItem atom) {
            ItemStack inv_stack = stacks.get(atom.getAtom().getAtomNumber() - 1);
            if (inv_stack.isEmpty()) {
                // Empty stack
                setStack(atom.getAtom().getAtomNumber() - 1, stack);
                stack.setCount(0);
                return ItemStack.EMPTY;
            } else {
                // Not an empty stack
                if (ItemStack.canCombine(inv_stack, stack)) {
                    this.transfer(stack, inv_stack);
                    if (stack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        return stack;
    }

    /**
     * Transfer source stack into target stack
     *
     * @param source : ItemStack to take from
     * @param target : ItemStack to increase
     */
    private void transfer(ItemStack source, ItemStack target) {
        int j = Math.min(source.getCount(), getMaxCountPerStack() - target.getCount());
        if (j > 0) {
            target.increment(j);
            source.decrement(j);
            this.markDirty();
        }
    }

    @Override
    public int getMaxCountPerStack() {
        return MAX_SIZE;
    }

    public void readNbt(NbtCompound nbt){
        NbtList nbtList = nbt.getList("Items", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 0xFF;
            if (j < 0 || j >= stacks.size()) continue;

            ItemStack stack = new ItemStack(
                    Registries.ITEM.get(new Identifier(nbtCompound.getString("id"))),
                    nbtCompound.getShort("Count"));
            stacks.set(j, stack);
        }
    }

    public NbtCompound writeNbt(NbtCompound nbt){
        NbtList nbtList = new NbtList();
        for (int i = 0; i < stacks.size(); ++i) {
            ItemStack itemStack = stacks.get(i);
            if (itemStack.isEmpty()) continue;
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)i);

            Identifier identifier = Registries.ITEM.getId(itemStack.getItem());
            nbtCompound.putString("id", identifier == null ? "minecraft:air" : identifier.toString());
            nbtCompound.putShort("Count", (short) itemStack.getCount()); // Ensure Count is a short instead of BYTE (max 127)

            nbtList.add(nbtCompound);
        }
        nbt.put("Items", nbtList);
        return nbt;
    }
}
