package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.util.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ChargedBlockEntityNEW extends BlockEntity{
    private double charge;
    private Vec3f field = Vec3f.ZERO;
    private boolean update_next_tick = false;
    private static final double e_move = 0.5;
    private final Block anti_block;
    private final double decay_time;
    private final ItemStack decay_drop;
    private float f;
    private ArrayList<Float> list;

    public ChargedBlockEntityNEW(BlockEntityType<?> type, BlockPos pos, BlockState state, double charge, Block anit_block, double decay_time, ItemStack decay_drop) {
        super(type, pos, state);
        this.charge = charge;
        this.anti_block = anit_block;
        this.decay_time = decay_time;
        this.decay_drop = decay_drop;
    }
    @Override
    public void writeNbt(NbtCompound tag) {
        // Save the current value of the number to the tag
        tag.putFloat("ex", field.getX());
        tag.putFloat("ey", field.getY());
        tag.putFloat("ez", field.getZ());
        tag.putDouble("q", charge);
        tag.putBoolean("ut", update_next_tick);
        super.writeNbt(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        field = new Vec3f(tag.getFloat("ex"), tag.getFloat("ey"), tag.getFloat("ez"));
        charge = tag.getDouble("q");
        update_next_tick = tag.getBoolean("ut");
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

    public Vec3f getField() {
        return field;
    }

    public double getCharge() {
         return charge;
    }

    public void needsUpdate(boolean b) {
        this.update_next_tick = b;
    }

    //First time field
    public void makeField(World world, BlockPos pos) {
        int e_radius = 8;
        double kc = 1;
        Iterable<BlockPos> blocks_in_radius = BlockPos.iterate(pos.mutableCopy().add(-e_radius, -e_radius, -e_radius), pos.mutableCopy().add(e_radius, e_radius, e_radius));
        field = new Vec3f(0f, 0f, 0f);
        for (BlockPos pos_block : blocks_in_radius) {
            if (world.getBlockEntity(pos_block) instanceof ChargedBlockEntityNEW particle2 && !pos.equals(pos_block)) {
                Vec3f vec_pos = new Vec3f(pos.getX()-pos_block.getX(), pos.getY()-pos_block.getY(), pos.getZ()-pos_block.getZ());
                float d_E = (float) ((getCharge() * particle2.getCharge() * kc) / Math.pow(vec_pos.dot(vec_pos), 1.5));
                vec_pos.scale(d_E);
                field.add(vec_pos);
                particle2.field.subtract(vec_pos);
                needsUpdate(true);
                particle2.needsUpdate(true);
            }
        }
        markDirty();
    }

    public void removeField(World world, BlockPos pos) {
        int e_radius = 8;
        double kc = 1;
        Iterable<BlockPos> blocks_in_radius = BlockPos.iterate(pos.mutableCopy().add(-e_radius, -e_radius, -e_radius), pos.mutableCopy().add(e_radius, e_radius, e_radius));
        for (BlockPos pos_block : blocks_in_radius) {
            if (world.getBlockEntity(pos_block) instanceof ChargedBlockEntityNEW particle2 && !pos.equals(pos_block)) {
                Vec3f vec_pos = new Vec3f(pos.getX() - pos_block.getX(), pos.getY() - pos_block.getY(), pos.getZ() - pos_block.getZ());
                float d_E = (float) ((getCharge() * particle2.getCharge() * kc) / Math.pow(vec_pos.dot(vec_pos), 1.5));
                vec_pos.scale(d_E);
                particle2.field.add(vec_pos);
                particle2.markDirty();
                particle2.needsUpdate(true);
            }
        }
        needsUpdate(true);
        markDirty();
    }


    public Direction checkAnnihilation() {
        if (anti_block == null) {
            return null;
        }
        Collection<Direction> shuffle = Direction.shuffle(new LocalRandom(1234567L));
        for (Direction direction : shuffle) {
            if (world.getBlockState(pos.mutableCopy().offset(direction)).getBlock().equals(anti_block)) {
                return direction;
            }
        }
        return null;
    }

    private boolean decay() {
        if (this.decay_time == 0) {
            return false;
        }
        if (this.decay_drop == null) {
            return false;
        }
        return Math.random() <= (1 / this.decay_time);
    }

    private Direction movementDirection(World world, BlockPos pos, Vec3f oldField) {
        if (oldField.equals(Vec3f.ZERO)) {
            return null;
        }
        Vec3f field_abs = new Vec3f(Math.abs(oldField.getX()), Math.abs(oldField.getY()), Math.abs(oldField.getZ()));
        list = new ArrayList<>();
        list.add(field_abs.getX());
        list.add(field_abs.getY());
        list.add(field_abs.getZ());
        float max = Collections.max(list);
        if (max < e_move ) {
            return null;
        }
        oldField.scale(1/max);
        Vec3f movement = new Vec3f(Math.round(oldField.getX()), Math.round(oldField.getY()), Math.round(oldField.getZ()));
        if (movement.dot(movement) > 1) {
            if (Math.abs(movement.getX()) == 1) {
                oldField.set(0, oldField.getY(), oldField.getZ());
            } else if (Math.abs(movement.getY()) == 1) {
                oldField.set(oldField.getX(), 0, oldField.getZ());
            }
        }
        Direction dir = Direction.fromVector(Math.round(oldField.getX()), Math.round(oldField.getY()), Math.round(oldField.getZ()));

        if (dir == null || !world.getBlockState(pos.offset(dir)).isAir() && !world.getBlockState(pos.offset(dir)).isOf(Blocks.ANIMATED_CHARGED) && movement.dot(movement) > 1) {
            if (checkAnnihilation() != null) {
                needsUpdate(true);
                return dir;
            }
            movement.scale(max);
            field_abs.subtract(movement);
            dir = movementDirection(world, pos, oldField.copy());
        }
        return dir;
    }

    public static void tick(World world, BlockPos pos, BlockState state, ChargedBlockEntityNEW be) {
        be.tick(world, pos, state);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (!world.isClient) {
            if ((world.getTime() + pos.asLong()) % 10 == 0) {
                if (decay()) {
                    world.removeBlockEntity(pos);
                    world.removeBlock(pos, false);
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), decay_drop);
                    world.spawnEntity(itemEntity);
                    markDirty();
                } else {
                    Direction movement_annihilation = this.checkAnnihilation();
                    if (movement_annihilation != null) {
                        BlockPos nPos = pos.mutableCopy().offset(movement_annihilation);
                        if (world.getBlockEntity(nPos) instanceof ChargedBlockEntityNEW particle2) {
                            BlockState render_state2 = particle2.getCachedState();
                            world.removeBlockEntity(pos);
                            world.removeBlockEntity(nPos);
                            world.removeBlock(pos, false);
                            world.removeBlock(nPos, false);
                            world.setBlockState(pos, be.uantwerpen.scicraft.block.Blocks.ANIMATED_CHARGED.getDefaultState(), Block.NOTIFY_ALL);
                            world.setBlockState(nPos, be.uantwerpen.scicraft.block.Blocks.ANIMATED_CHARGED.getDefaultState(), Block.NOTIFY_ALL);
                            if (world.getBlockEntity(pos) instanceof AnimatedChargedBlockEntity animation1) {
                                animation1.movement_direction = movement_annihilation;
                                animation1.render_state = getCachedState();
                                animation1.annihilation = true;
                            }
                            if (world.getBlockEntity(nPos) instanceof AnimatedChargedBlockEntity animation2) {
                                animation2.movement_direction = movement_annihilation.getOpposite();
                                animation2.render_state = render_state2;
                                animation2.annihilation = true;
                            }
                        }
                        markDirty();
                    }
                    if (update_next_tick) {
                        Direction movement = movementDirection(world, pos, field);
                        if (movement != null) {
                            BlockPos nPos = pos.mutableCopy().offset(movement);
                            if (world.getBlockState(nPos).isAir()) {
                                update_next_tick = false;
                                world.setBlockState(pos, be.uantwerpen.scicraft.block.Blocks.ANIMATED_CHARGED.getDefaultState(), Block.NOTIFY_ALL);
                                world.setBlockState(nPos, be.uantwerpen.scicraft.block.Blocks.CHARGED_PLACEHOLDER.getDefaultState(), Block.NOTIFY_ALL);
                                if (world.getBlockEntity(pos) instanceof AnimatedChargedBlockEntity animation1) {
                                    animation1.movement_direction = movement;
                                    animation1.render_state = getCachedState();
                                }
                            }
                            update_next_tick = false;
                            markDirty();
                        } else {
                            update_next_tick = false;
                        }
                    }
                }
            }
        }
    }
}
