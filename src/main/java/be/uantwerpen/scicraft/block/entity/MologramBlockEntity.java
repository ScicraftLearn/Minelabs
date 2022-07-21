package be.uantwerpen.scicraft.block.entity;


import be.uantwerpen.scicraft.block.MologramBlock;
import be.uantwerpen.scicraft.particle.Particles;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MologramBlockEntity extends BlockEntity {
    public SimpleInventory inventory = new SimpleInventory(1);
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

        Inventories.writeNbt(tag, DefaultedList.ofSize(1, inventory.getStack(0)));

        super.writeNbt(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        rot_x = tag.getInt("rotate_x");
        rot_y = tag.getInt("rotate_y");
        rot_z = tag.getInt("rotate_z");

        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(1,ItemStack.EMPTY);
        Inventories.readNbt(tag, stacks);
        inventory.setStack(0,stacks.get(0));
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, MologramBlockEntity entity){
        if (!state.get(MologramBlock.LIT)) return;
        world.addParticle(Particles.HOLOGRAM_PARTICLE, pos.getX()+ 0.5d, pos.getY() + 1.75d, pos.getZ() + 0.5d,
                0,0,0);
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

    public SimpleInventory getInventory() {
        return inventory;
    }
}