package be.minelabs.inventory;

import com.mojang.datafixers.DataFixUtils;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

/**
 * Extension of {@link net.minecraft.inventory.SimpleInventory} that keeps track of position of items in inventory.
 */
public class OrderedInventory extends SimpleInventory {

    public OrderedInventory(int size) {
        super(size);
    }

    public OrderedInventory(ItemStack ... items) {
        super(items);
    }

    @Override
    public void readNbtList(NbtList nbtList) {
        Inventories.readNbt(DataFixUtils.make(new NbtCompound(), c -> c.put("Items", nbtList)), stacks);
    }

    @Override
    public NbtList toNbtList() {
        return Inventories.writeNbt(new NbtCompound(), stacks, true).getList("Items", NbtElement.COMPOUND_TYPE);
    }

}
