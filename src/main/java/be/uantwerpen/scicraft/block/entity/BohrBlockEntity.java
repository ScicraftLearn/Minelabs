package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.inventory.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class BohrBlockEntity extends BlockEntity implements ImplementedInventory {

    private final DefaultedList<ItemStack> protonInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private final DefaultedList<ItemStack> neutronInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private final DefaultedList<ItemStack> electronInventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    public BohrBlockEntity( BlockPos pos, BlockState state) {
        super(BlockEntities.BOHR_BLOCK_ENTITY, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return null;
    }

    public DefaultedList<ItemStack> getProtonInventory() {
        return protonInventory;
    }

    public DefaultedList<ItemStack> getNeutronInventory() {
        return neutronInventory;
    }

    public DefaultedList<ItemStack> getElectronInventory() {
        return electronInventory;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        //super.readNbt(nbt);
        Inventories.readNbt(nbt, this.protonInventory);
        Inventories.readNbt(nbt, this.neutronInventory);
        Inventories.readNbt(nbt, this.electronInventory);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.protonInventory);
        Inventories.writeNbt(nbt, this.neutronInventory);
        Inventories.writeNbt(nbt, this.electronInventory);
    }

}
