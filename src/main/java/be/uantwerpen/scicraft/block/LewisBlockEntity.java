package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.inventory.LewisCraftingInventory;
import be.uantwerpen.scicraft.item.LewisCraftingItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import static be.uantwerpen.scicraft.block.Blocks.LEWIS_BLOCK_ENTITY;

public class LewisBlockEntity extends BlockEntity {

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public LewisBlockEntity(BlockPos pos, BlockState state) {
        super(LEWIS_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        super.writeNbt(nbt);
    }

}
