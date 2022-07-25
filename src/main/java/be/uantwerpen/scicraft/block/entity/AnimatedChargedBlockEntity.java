package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.block.ChargedBlock;
import be.uantwerpen.scicraft.block.ChargedPionBlock;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.network.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Objects;

public class AnimatedChargedBlockEntity extends BlockEntity {
    public long time = 0;
    public Direction movement_direction;
    public final static int time_move_ticks = 8;
    public BlockState render_state = net.minecraft.block.Blocks.AIR.getDefaultState();
    public boolean annihilation = false;

    public AnimatedChargedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        tag.putLong("time", time);
        tag.putInt("md", movement_direction.getId());
        tag.put("rs",NbtHelper.fromBlockState(render_state));
        super.writeNbt(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        time = tag.getLong("time");
        movement_direction = Direction.byId(tag.getInt("md"));
        render_state = NbtHelper.toBlockState(tag.getCompound("rs"));
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

                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(pos);
                buf.writeString(render_state.toString());
                buf.writeBoolean(annihilation);
                for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, pos)) {
                    // update client on the block for the animation
                    ServerPlayNetworking.send(player, NetworkingConstants.CHARGED_MOVE_STATE, buf);
                }
            }
            if (world.getTime() - time > time_move_ticks) {
                world.removeBlockEntity(pos);
                world.removeBlock(pos, false);
                BlockPos blockPos = pos.mutableCopy().offset(movement_direction);
                if (world.getBlockState(blockPos).getBlock().equals(be.uantwerpen.scicraft.block.Blocks.CHARGED_PLACEHOLDER)) { //also change other particle for client
                    world.setBlockState(blockPos, render_state, Block.NOTIFY_ALL);
                    BlockEntity be = world.getBlockEntity(blockPos);
                    if (be instanceof ChargedBlockEntityNEW charge) {
                        charge.makeField(world, blockPos);
                    }
                }
                if (annihilation) {
                    ItemStack itemStack = new ItemStack(Items.PHOTON, 1);
                    double a = pos.getX() + movement_direction.getVector().getX() / 2d;
                    double b = pos.getY() + movement_direction.getVector().getY() / 2d;
                    double c = pos.getZ() + movement_direction.getVector().getZ() / 2d;
                    ItemEntity itemEntity = new ItemEntity(world, a, b, c, itemStack);
                    world.spawnEntity(itemEntity);
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
