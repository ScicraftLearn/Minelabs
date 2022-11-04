package be.uantwerpen.minelabs.block.entity;

import be.uantwerpen.minelabs.inventory.ImplementedInventory;
import be.uantwerpen.minelabs.item.MoleculeItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MologramBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> INVENTORY = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private float rotation;

    public MologramBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.MOLOGRAM_BLOCK_ENTITY, pos, state);
        rotation = 0.0f;
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, MologramBlockEntity entity) {
        if (!world.isClient) {
            // SERVER
            if (state.get(Properties.LIT) && entity.getStack(0).isEmpty()) { // hopper extracted
                world.setBlockState(blockPos, state.with(Properties.LIT, false));
            } else if (!state.get(Properties.LIT) && !entity.getStack(0).isEmpty()) {// hopper inserted
                world.setBlockState(blockPos, state.with(Properties.LIT, true));
            }
        } else {
            //CLIENT
            entity.rotation = (entity.rotation + 3.6f) % 360f;
        }
    }

    // Serialize the BlockEntity
    @Override
    public void writeNbt(NbtCompound tag) {
        Inventories.writeNbt(tag, INVENTORY);
        tag.putFloat("rotation", rotation);
        super.writeNbt(tag);
    }
    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        Inventories.readNbt(tag, INVENTORY);
        rotation = tag.getFloat("rotation");
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
     * @return true if the stack can be inserted
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

    public float getRotation() {
        return rotation;
    }
}