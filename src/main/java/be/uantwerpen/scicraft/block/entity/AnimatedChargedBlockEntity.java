package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AnimatedChargedBlockEntity extends BlockEntity {
    public long time = 0;
    public Vec3i movement_direction = Vec3i.ZERO;
    public final int time_move_ticks = 10;
    public BlockState render_state = Blocks.ELECTRON.getDefaultState();

    public AnimatedChargedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        tag.putLong("time", time);
        tag.putInt("md", ChargedBlockEntity.vec2int(movement_direction));
        super.writeNbt(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        time = tag.getLong("time");
        movement_direction = ChargedBlockEntity.int2vec(tag.getInt("md"));
        super.readNbt(tag);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (!world.isClient) {
            if (time == 0) {
                time = world.getTime();
            }
            if (world.getTime() - time > time_move_ticks) {
                world.removeBlockEntity(pos);
                world.removeBlock(pos, false);
                if (world.getBlockState(pos.mutableCopy().add(movement_direction)).getBlock().equals(be.uantwerpen.scicraft.block.Blocks.CHARGED_PLACEHOLDER)) { //also change other particle for client
                    world.setBlockState(pos.mutableCopy().add(movement_direction), render_state, Block.NOTIFY_ALL);
                }
                markDirty();
            }
        } else {
            if (time == 0) {
                time = world.getTime();
            }
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, AnimatedChargedBlockEntity be) {
        be.tick(world, pos, state);
    }
}
