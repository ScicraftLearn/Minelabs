package be.uantwerpen.scicraft.block.entity;


import be.uantwerpen.scicraft.inventory.ImplementedInventory;
import be.uantwerpen.scicraft.item.MoleculeItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;

public class MologramBlockEntity extends BlockEntity implements ImplementedInventory {
    private static final DefaultedList<ItemStack> INVENTORY = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public MologramBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.MOLOGRAM_BLOCK_ENTITY, pos, state);
    }
    // Store the current value of the number

    // Serialize the BlockEntity
    @Override
    public void writeNbt(NbtCompound tag) {
        Inventories.writeNbt(tag, INVENTORY);
        super.writeNbt(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        Inventories.readNbt(tag, INVENTORY);
    }

    //sync data server client:
    //Warning: Need to call world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS); to trigger the update. //
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return INVENTORY;
    }

    /**
     * Returns true if the stack can be inserted from the slot at the side.
     *
     * <p> side can be null in case of MANUAL PLAYER insertion
     * <p> ATM only allows Chemicals (Molecules) as Input ItemStack
     *
     * @param slot  the slot (0 in this case)
     * @param stack the stack
     * @param side  the side (UP, DOWN, EAST, WEST, NORTH, SOUTH)
     * @return true if the stack can be extracted
     */
    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        if (!(stack.getItem() instanceof MoleculeItem)) {
            return false;
        }
        if (side == null) {
            return true;
        }
        return switch (side) {
            case UP, DOWN -> false;
            default -> true;
        };

    }

    /**
     * Returns true if the stack can be extracted from the slot at the side.
     *
     * <p> We only extract form the bottom
     *
     * @param slot  the slot
     * @param stack the stack
     * @param side  the side
     * @return true if the stack can be extracted
     */
    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        return side == Direction.DOWN;
    }
}