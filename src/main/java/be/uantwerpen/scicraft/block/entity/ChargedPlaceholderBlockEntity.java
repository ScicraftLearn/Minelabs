package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.network.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
            // This part shouldn't be used, but is as a backup if the 'parent' block that does the animation doesn't remove this placeholder. The placeholder thus auto-removes itself after a number of ticks.
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
