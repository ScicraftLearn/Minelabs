package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.inventory.ImplementedInventory;
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

import javax.annotation.Nullable;

public class MologramBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int rot_x = 0;
    private int rot_y = 0;
    private int rot_z = 0;
    public MologramBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.MOLOGRAM_BLOCK_ENTITY, pos, state);
    }
    // Store the current value of the number

    // Serialize the BlockEntity
    @Override
    public void writeNbt(NbtCompound tag) { //should this be void? or not? https://fabricmc.net/wiki/tutorial:inventory
        // Save the current value of the number to the tag
        tag.putInt("rotate_x", rot_x);
        tag.putInt("rotate_y", rot_y);
        tag.putInt("rotate_z", rot_z);
        Inventories.writeNbt(tag, items);

        super.writeNbt(tag);
    }
    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        rot_x = tag.getInt("rotate_x");
        rot_y = tag.getInt("rotate_y");
        rot_z = tag.getInt("rotate_z");
        Inventories.readNbt(tag, items);
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
        return items;
    }

}

