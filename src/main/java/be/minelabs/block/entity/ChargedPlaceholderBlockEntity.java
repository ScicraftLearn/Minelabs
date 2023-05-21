package be.minelabs.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChargedPlaceholderBlockEntity extends BlockEntity{
    private Long time = 0L;

    public ChargedPlaceholderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        // TODO: create a whole "lifecycle" for a charged particle, begining from either placement by user or by algorithm and checking if algorithms in client and server are still in sync.
        if (!world.isClient) {
            if (time == 0) {
                time = world.getTime();
            }
            if (world.getTime() - time > AnimatedChargedBlockEntity.time_move_ticks) {
                world.removeBlockEntity(pos);
                world.removeBlock(pos, false);
                markDirty();
            }
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, ChargedPlaceholderBlockEntity be) {
        be.tick(world, pos, state);
    }
}
