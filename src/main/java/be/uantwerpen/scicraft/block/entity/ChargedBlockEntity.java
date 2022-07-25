package be.uantwerpen.scicraft.block.entity;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.RandomSeed;
import net.minecraft.util.math.random.RandomSplitter;
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom;
import org.jetbrains.annotations.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.world.World;

import java.util.*;

public class ChargedBlockEntity extends BlockEntity{
    private double charge;
    private Vec3f field;
    private boolean update_next_tick = false;
    private static final double e_move = 0.5;
    private final Block anti_block;
    private final double decay_time;
    private final ItemStack decay_drop;

    public ChargedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, double charge, Block anit_block, double decay_time, ItemStack decay_drop) {
        super(type, pos, state);
        this.charge = charge;
        this.anti_block = anit_block;
        this.decay_time = decay_time;
        this.decay_drop = decay_drop;
        field = Vec3f.ZERO;
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

    private void updateField(World world, BlockPos pos) {
        int e_radius = 8;
        double kc = 1;
        Iterable<BlockPos> blocks_in_radius = BlockPos.iterate(pos.mutableCopy().add(-e_radius, -e_radius, -e_radius), pos.mutableCopy().add(e_radius, e_radius, e_radius));
        field = new Vec3f(0f, 0f, 0f);
        for (BlockPos pos_block : blocks_in_radius) {
            if (world.getBlockEntity(pos_block) instanceof ChargedBlockEntity particle2 && !pos.equals(pos_block)) {
                Vec3f vec_pos = new Vec3f(pos.getX()-pos_block.getX(), pos.getY()-pos_block.getY(), pos.getZ()-pos_block.getZ());
                float d_E = (float) ((getCharge() * particle2.getCharge() * kc) / Math.pow(vec_pos.dot(vec_pos), 1.5));
                vec_pos.scale(d_E);
                field.add(vec_pos);
                needsUpdate(field.dot(field) > e_move); // putting it here makes it so the field stays zero.
            }
        }
        markDirty();
    }

    private Direction movementDirection(World world, BlockPos pos) {
        Vec3d field_abs = new Vec3d(Math.abs(field.getX()), Math.abs(field.getY()), Math.abs(field.getZ()));
        Vec3i intvec = new Vec3i(field.getX(), field.getY(), field.getZ());
        if (field_abs.getX() >= field_abs.getY()) {
            if (field_abs.getX() >= field_abs.getZ()) { // XYZ & XZY
                if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // X is taken -> (X)YZ or (X)ZY
                    if (field_abs.getY() >= field_abs.getZ()) { //(X)YZ
                        if (field_abs.getY() >= e_move) {
                            if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // Y is taken -> (XY)Z
                                if (field_abs.getZ() >= e_move) {
                                    if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // Z is taken -> don't move (XYZ)
                                    }
                                } else {
                                }
                            }
                        } else {
                        }
                    } else { //(X)ZY
                        if (field_abs.getZ() >= e_move) {
                            if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // Z is taken -> (XZ)Y
                                if (field_abs.getY() >= e_move) {
                                    if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // Y is taken -> don't move (XZY)
                                    }
                                } else {
                                }
                            }
                        } else {
                        }
                    }
                }
            } else {
                if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // Z is taken -> (Z)XY
                    if (field_abs.getX() >= e_move) {
                        if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // X is taken -> (ZX)Y
                            if (field_abs.getY() >= e_move) {
                                if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // Y is taken -> don't move (ZXY)
                                }
                            } else {
                            }
                        }
                    } else {
                    }
                }
            }
        } else if (field_abs.getY() >= field_abs.getZ()) {
            if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // Y is taken -> (Y)ZX or (Y)XZ
                if (field_abs.getZ() >= field_abs.getX()) { //(Y)ZX
                    if (field_abs.getZ() >= e_move) {
                        if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // Z is taken -> (YZ)X
                            if (field_abs.getX() >= e_move) {
                                if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // X is taken -> don't move (YZX)
                                }
                            } else {
                            }
                        }
                    } else {
                    }
                } else { //(Y)XZ
                    if (field_abs.getX() >= e_move) {
                        if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // X is taken -> (YX)Z
                            if (field_abs.getZ() >= e_move) {
                                if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // Z is taken -> don't move (YXZ)
                                }
                            } else {
                            }
                        }
                    } else {

                    }
                }
            }
        } else {
            if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // Z is taken -> (Z)YX
                if (field_abs.getY() >= e_move) {
                    if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // Y is taken -> (ZY)X
                        if (field_abs.getX() >= e_move) {
                            if (!world.getBlockState(pos.mutableCopy().add(intvec)).isAir()) { // X is taken -> don't move (ZYX)
                            }
                        } else {

                        }
                    }
                } else {

                }
            }
        }
        return Direction.fromVector(intvec.getX(), intvec.getY(), intvec.getZ());
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
                        if (world.getBlockEntity(nPos) instanceof ChargedBlockEntity particle2) {
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
                    } else {
                        this.updateField(world, pos);
                    }
                }
                if (update_next_tick) {
                    Direction movement = movementDirection(world, pos);
                    if (movement != null) {
                        BlockPos nPos = pos.mutableCopy().offset(movement);
                        if (world.getBlockState(nPos).isAir()) {
                            update_next_tick = false;
                            world.removeBlockEntity(pos);
                            world.removeBlock(pos, false);
                            world.setBlockState(pos, be.uantwerpen.scicraft.block.Blocks.ANIMATED_CHARGED.getDefaultState(), Block.NOTIFY_ALL);
                            world.setBlockState(nPos, be.uantwerpen.scicraft.block.Blocks.CHARGED_PLACEHOLDER.getDefaultState(), Block.NOTIFY_ALL);
                            if (world.getBlockEntity(pos) instanceof AnimatedChargedBlockEntity animation1) {
                                animation1.movement_direction = movement;
                                animation1.render_state = getCachedState();
                            }
                        }
                        markDirty();
                    } else {
                        update_next_tick = false;
                    }
                }
            }
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, ChargedBlockEntity be) {
        be.tick(world, pos, state);
    }

}
