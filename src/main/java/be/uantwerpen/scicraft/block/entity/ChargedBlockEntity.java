package be.uantwerpen.scicraft.block.entity;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class ChargedBlockEntity extends BlockEntity{
    private double charge;
    private Vec3f field;
    private boolean update_next_tick = false;
    private static final double e_move = 0.01d;
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

    public static Vec3i int2vec(int movement) {
        // Easy way to describe the movement of 1 block, in which direction. Save the amount of NBT data to use.
        if (movement == 1) {
            return new Vec3i(1, 0, 0);
        } else if (movement == -1) {
            return new Vec3i(-1, 0, 0);
        } else if (movement == 2) {
            return new Vec3i(0, 1, 0);
        } else if (movement == -2) {
            return new Vec3i(0,-1, 0);
        } else if (movement == 3) {
            return new Vec3i(0,0,1);
        } else if (movement == -3) {
            return new Vec3i(0,0,-1);
        } else {
            return new Vec3i(0,0,0);
        }
    }

    public static int vec2int(Vec3i movement) {
        // Easy way to describe the movement of 1 block, in which direction. Save the amount of NBT data to use.
        if (Objects.equals(movement, new Vec3i(1, 0, 0))) {
            return 1;
        } else if (movement.equals(new Vec3i(-1, 0, 0))) {
            return -1;
        } else if (movement.equals(new Vec3i(0, 1, 0))) {
            return 2;
        } else if (movement.equals(new Vec3i(0,-1, 0))) {
            return -2;
        } else if (movement.equals(new Vec3i(0,0,1))) {
            return 3;
        } else if (movement.equals(new Vec3i(0,0,-1))) {
            return -3;
        } else {
            return 0;
        }
    }
    private void updateField(World world, BlockPos pos) {
        // Search for all the charged block in the neighbourhood and update the field. If the field is large enough, mark the particle for an update on the next tick.
        int e_radius = 8;
        double kc = 1;
        Iterable<BlockPos> blocks_in_radius = BlockPos.iterate(pos.mutableCopy().add(-e_radius, -e_radius, -e_radius), pos.mutableCopy().add(e_radius, e_radius, e_radius));
        field = new Vec3f(0f, 0f, 0f);
        for (BlockPos pos_block : blocks_in_radius) {
            if (world.getBlockEntity(pos_block) instanceof ChargedBlockEntity particle2 && !pos.equals(pos_block)) {
                Vec3f vec_pos = new Vec3f(pos.getX()-pos_block.getX(), pos.getY()-pos_block.getY(), pos.getZ()-pos_block.getZ());
                // This way, we only need to do the 'large' calculation of the field once
                float d_E = (float) ((getCharge() * particle2.getCharge() * kc) / Math.pow(vec_pos.dot(vec_pos), 1.5));
                // find the componnts of the fields
                vec_pos.scale(d_E);
                // add the the other parts of the fields already calculated
                field.add(vec_pos);
                needsUpdate(field.dot(field) > e_move); // putting it here makes it so the field stays zero.
            }
        }
        markDirty();
    }

    private Vec3i movementDirection(World world, BlockPos pos) {
        // Determine which way to move (in essence, sorting the order from large to small and seeing if the force is large enough for a movement).
        Vec3d field_abs = new Vec3d(Math.abs(field.getX()), Math.abs(field.getY()), Math.abs(field.getZ()));
        int movement = 0;
        if (field_abs.getX() >= field_abs.getY()) {
            if (field_abs.getX() >= field_abs.getZ()) { // XYZ & XZY
                movement = field.getX() > 0 ? 1 : -1;
                if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // X is taken -> (X)YZ or (X)ZY
                    if (field_abs.getY() >= field_abs.getZ()) { //(X)YZ
                        if (field_abs.getY()*field_abs.getY() >= e_move) {
                            movement = field.getY() > 0 ? 2 : -2; // (X)YZ
                            if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // Y is taken -> (XY)Z
                                if (field_abs.getZ()*field_abs.getZ() >= e_move) {
                                    movement = field.getZ() > 0 ? 3 : -3; // (XY)Z
                                    if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // Z is taken -> don't move (XYZ)
                                        movement = 0;
                                    }
                                } else {
                                    movement = 0; // Z too small
                                }
                            }
                        } else {
                            movement = 0; // Y too small. Z<Y, so no Z movement.
                        }
                    } else { //(X)ZY
                        if (field_abs.getZ()*field_abs.getZ() >= e_move) {
                            movement = field.getZ() > 0 ? 3 : -3; // (X)ZY
                            if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // Z is taken -> (XZ)Y
                                if (field_abs.getY()*field_abs.getY() >= e_move) {
                                    movement = field.getY() > 0 ? 2 : -2; // (XZ)Y
                                    if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // Y is taken -> don't move (XZY)
                                        movement = 0;
                                    }
                                } else {
                                    movement = 0; // Y too small
                                }
                            }
                        } else {
                            movement = 0; // Z too small. Y<Z, so no Y movement.
                        }
                    }
                }
            } else {
                movement = field.getZ() > 0 ? 3 : -3; // ZXY
                if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // Z is taken -> (Z)XY
                    if (field_abs.getX() >= e_move) {
                        movement = field.getX() > 0 ? 1 : -1;
                        if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // X is taken -> (ZX)Y
                            if (field_abs.getY() >= e_move) {
                                movement = field.getY() > 0 ? 2 : -2; // (ZX)Y
                                if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // Y is taken -> don't move (ZXY)
                                    movement = 0;
                                }
                            } else {
                                movement = 0; // Y too small
                            }
                        }
                    } else {
                        movement = 0; // X too small. Y<X, so no Y movement
                    }
                }
            }
        } else if (field_abs.getY() >= field_abs.getZ()) {
            movement = field.getY() > 0 ? 2 : -2; // YZX & YXZ
            if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // Y is taken -> (Y)ZX or (Y)XZ
                if (field_abs.getZ() >= field_abs.getX()) { //(Y)ZX
                    if (field_abs.getZ()*field_abs.getZ() >= e_move) {
                        movement = field.getZ() > 0 ? 3 : -3; // (Y)ZX
                        if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // Z is taken -> (YZ)X
                            if (field_abs.getX()*field_abs.getX() >= e_move) {
                                movement = field.getX() > 0 ? 1 : -1; // (YZ)X
                                if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // X is taken -> don't move (YZX)
                                    movement = 0;
                                }
                            } else {
                                movement = 0; // X too small
                            }
                        }
                    } else {
                        movement = 0; // Z too small. X<Z, so no X movement.
                    }
                } else { //(Y)XZ
                    if (field_abs.getX()*field_abs.getX() >= e_move) {
                        movement = field.getX() > 0 ? 1 : -1; // (Y)XZ
                        if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // X is taken -> (YX)Z
                            if (field_abs.getZ()*field_abs.getZ() >= e_move) {
                                movement = field.getZ() > 0 ? 3 : -3; // (YX)Z
                                if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // Z is taken -> don't move (YXZ)
                                    movement = 0;
                                }
                            } else {
                                movement = 0; // Z too small
                            }
                        }
                    } else {
                        movement = 0; // X too small. Z<X, so no Z movement.
                    }
                }
            }
        } else {
            movement = field.getZ() > 0 ? 3 : -3; // ZYX
            if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // Z is taken -> (Z)YX
                if (field_abs.getY()*field_abs.getY() >= e_move) {
                    movement = field.getY() > 0 ? 2 : -2;
                    if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // Y is taken -> (ZY)X
                        if (field_abs.getX()*field_abs.getX() >= e_move) {
                            movement = field.getX() > 0 ? 1 : -1; // (ZY)X
                            if (!world.getBlockState(pos.mutableCopy().add(int2vec(movement))).isAir()) { // X is taken -> don't move (ZYX)
                                movement = 0;
                            }
                        } else {
                            movement = 0; // X too small
                        }
                    }
                } else {
                    movement = 0; // Y too small. X<Y, so no X movement
                }
            }
        }
        return int2vec(movement);
    }

    public int checkAnnihilation() {
        if (anti_block == null) {
            return 0;
        }
        java.util.List<Integer> direction_neighbours = new ArrayList<Integer>(Arrays.asList(1, -1, 2, -2, 3, -3));
        Collections.shuffle(direction_neighbours);
        for (int direction : direction_neighbours) {
            if (world.getBlockState(pos.mutableCopy().add(int2vec(direction))).getBlock().equals(anti_block)) {
                return direction;
            }
        }
        return 0;
    }

    private boolean decay() {
        // For radioactive decay
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
            //server
            // skip some ticks to not overload the server with updates
            if ((world.getTime() + pos.asLong()) % 10 == 0) {
                if (decay()) {
                    // some random chance for a decay, so remove particle if it has decayed, and spawn some items.
                    world.removeBlockEntity(pos);
                    world.removeBlock(pos, false);
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), decay_drop);
                    world.spawnEntity(itemEntity);
                    markDirty();
                } else {
                    // see if we can annihilate
                    int movement_annihilation = this.checkAnnihilation();
                    if (movement_annihilation != 0) {
                        // save the position of the block to annihilate
                        BlockPos nPos = pos.mutableCopy().add(int2vec(movement_annihilation));
                        if (world.getBlockEntity(nPos) instanceof ChargedBlockEntity particle2) {
                            // remove the other block and place a placeholder there
                            BlockState render_state2 = particle2.getCachedState();
                            world.removeBlockEntity(pos);
                            world.removeBlockEntity(nPos);
                            world.removeBlock(pos, false);
                            world.removeBlock(nPos, false);
                            world.setBlockState(pos, be.uantwerpen.scicraft.block.Blocks.ANIMATED_CHARGED.getDefaultState(), Block.NOTIFY_ALL);
                            world.setBlockState(nPos, be.uantwerpen.scicraft.block.Blocks.ANIMATED_CHARGED.getDefaultState(), Block.NOTIFY_ALL);
                            // give information about the way the blocks should render
                            if (world.getBlockEntity(pos) instanceof AnimatedChargedBlockEntity animation1) {
                                // this is only given to the server-side block. The client doesn't see this update. The BlockEntity thus also submits a packet to the client with these values.
                                animation1.movement_direction = int2vec(movement_annihilation);
                                animation1.render_state = getCachedState();
                                animation1.annihilation = true;
                            }
                            if (world.getBlockEntity(nPos) instanceof AnimatedChargedBlockEntity animation2) {
                                // this is only given to the server-side block. The client doesn't see this update. The BlockEntity thus also submits a packet to the client with these values.
                                animation2.movement_direction = int2vec(-movement_annihilation);
                                animation2.render_state = render_state2;
                                animation2.annihilation = true;
                            }
                        }
                        markDirty();
                    } else {
                        // update the field if we don't need to annihilate
                        this.updateField(world, pos);
                    }
                }
                // If we can move (from updateField()), we should move accordingly
                if (update_next_tick) {
                    Vec3i movement = movementDirection(world, pos);
                    // check if movement is allowed
                    if (!movement.equals(Vec3i.ZERO)) {
                        BlockPos nPos = pos.mutableCopy().add(movement);
                        if (world.getBlockState(nPos).isAir()) {
                            // remove block and place placeholders in the current position (also for animation) and the new position (no 2 blokcs rendering the same position)
                            update_next_tick = false;
                            world.removeBlockEntity(pos);
                            world.removeBlock(pos, false);
                            world.setBlockState(pos, be.uantwerpen.scicraft.block.Blocks.ANIMATED_CHARGED.getDefaultState(), Block.NOTIFY_ALL);
                            world.setBlockState(nPos, be.uantwerpen.scicraft.block.Blocks.CHARGED_PLACEHOLDER.getDefaultState(), Block.NOTIFY_ALL);
                            // update for the animation
                            if (world.getBlockEntity(pos) instanceof AnimatedChargedBlockEntity animation1) {
                                // this is only given to the server-side block. The client doesn't see this update. The BlockEntity thus also submits a packet to the client with these values.
                                animation1.movement_direction = movement;
                                animation1.render_state = getCachedState();
                            }
                        }
                        markDirty();
                    } else {
                        // The block can't move anymore
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
