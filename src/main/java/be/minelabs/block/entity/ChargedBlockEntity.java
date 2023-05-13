package be.minelabs.block.entity;

import be.minelabs.advancement.criterion.CoulombCriterion;
import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ChargedBlockEntity extends BlockEntity{
    protected double charge;
    public static final int e_radius = 12;
    private Vector3f field;
    private boolean update_next_tick = false;
    private static final double e_move = 0.1; //if force is larger, then particles can move
    private final Block anti_block;
    private final double decay_time;
    private final ArrayList<ItemStack> decay_drop;
    private final Block decay_replace;

    public ChargedBlockEntity(BlockEntityType<?> type,
                              BlockPos pos,
                              BlockState state,
                              double charge,
                              Block anti_block,
                              double decay_time,
                              ArrayList<ItemStack> decay_drop,
                              Block decay_replace) {
        super(type, pos, state);
        this.charge = charge;
        this.anti_block = anti_block;
        this.decay_time = decay_time;
        this.decay_drop = decay_drop;
        this.decay_replace = decay_replace;
    }
    @Override
    public void writeNbt(NbtCompound tag) {
        // Save the current value of the number to the tag
        if (field != null) {
            tag.putFloat("ex", field.x);
            tag.putFloat("ey", field.y);
            tag.putFloat("ez", field.z);
        }
        tag.putDouble("q", charge);
        tag.putBoolean("ut", update_next_tick);
        super.writeNbt(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        if (tag.contains("ex")) {
            field = new Vector3f(tag.getFloat("ex"), tag.getFloat("ey"), tag.getFloat("ez"));
        }
        charge = tag.getDouble("q");
        update_next_tick = tag.getBoolean("ut");
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public Vector3f getField() {
        return field;
    }

    public double getCharge() {
         return charge;
    }

    public void needsUpdate(boolean b) {
//        if(b) {
//            System.out.println("PROCESSING UPDATE!");
//        } else {
//        System.out.println("NOT PROCESSING ANY UPDATES!");
//        }

        this.update_next_tick = b;
    }

    public void updateField(World world, BlockPos pos) {
        this.removeField(world, pos);
        this.makeField(world, pos, false);
        markDirty();
    }

    //First time field
    public void makeField(World world, BlockPos pos, boolean afterTimeFreeze) {
        double kc = 1;
        Iterable<BlockPos> blocks_in_radius = BlockPos.iterate(pos.mutableCopy().add(-e_radius, -e_radius, -e_radius), pos.mutableCopy().add(e_radius, e_radius, e_radius));
        field = new Vector3f(0f, 0f, 0f);

        // first look whether there is a time freeze block in range
        boolean freeze = false;
        for (BlockPos pos_block : blocks_in_radius) {
            if (world.getBlockEntity(pos_block) instanceof TimeFreezeBlockEntity tf_block && !pos.equals(pos_block)) {
                //System.out.println("freeze now!");
                freeze = true;
                break;
            }
        }

        for (BlockPos pos_block : blocks_in_radius) {
            // if there is a time freeze block in range, no effect should happen to the particles themselves
            if (world.getBlockEntity(pos_block) instanceof ChargedBlockEntity particle2 && !pos.equals(pos_block) && particle2.field != null) {
                //System.out.println("calculating.....");
                Vector3f vec_pos = new Vector3f(pos.getX()-pos_block.getX(), pos.getY()-pos_block.getY(), pos.getZ()-pos_block.getZ());
                float d_E = (float) ((getCharge() * particle2.getCharge() * kc) / Math.pow(vec_pos.dot(vec_pos), 1.5));
                vec_pos.mul(d_E);
                field.add(vec_pos);
                particle2.field.sub(vec_pos);
                needsUpdate(!freeze);
                particle2.needsUpdate(!freeze);
            }

            // electric field sensors can still change when the time is frozen
            // therefore they must not be recalculated after the time freeze stops
            if(!afterTimeFreeze) {
                if (world.getBlockEntity(pos_block) instanceof ElectricFieldSensorBlockEntity sensor && !pos.equals(pos_block) ){
                    sensor.calculateField(world, pos_block, null);

                    // Vector3f vec_pos = new Vector3f(pos.getX()-pos_block.getX(), pos.getY()-pos_block.getY(), pos.getZ()-pos_block.getZ());
                    // float d_E = (float) ((getCharge() * 1 * kc) / Math.pow(vec_pos.dot(vec_pos), 1.5));
                    // vec_pos.scale(d_E);
                    // sensor.getField().subtract(vec_pos);

                    sensor.markDirty();
                    sensor.sync();
                }
            }
        }
        markDirty();
    }

    public void removeField(World world, BlockPos pos) {
        double kc = 1;
        Iterable<BlockPos> blocks_in_radius = BlockPos.iterate(pos.mutableCopy().add(-e_radius, -e_radius, -e_radius), pos.mutableCopy().add(e_radius, e_radius, e_radius));

        // first look whether there is a time freeze block in range
        boolean freeze = false;
        for (BlockPos pos_block : blocks_in_radius) {
            if (world.getBlockEntity(pos_block) instanceof TimeFreezeBlockEntity tf_block && !pos.equals(pos_block)) {
                //System.out.println("freeze now!");
                freeze = true;
                break;
            }
        }
        for (BlockPos pos_block : blocks_in_radius) {
            if (world.getBlockEntity(pos_block) instanceof ChargedBlockEntity particle2 && !pos.equals(pos_block) && particle2.field != null) {
                Vector3f vec_pos = new Vector3f(pos.getX() - pos_block.getX(), pos.getY() - pos_block.getY(), pos.getZ() - pos_block.getZ());
                float d_E = (float) ((getCharge() * particle2.getCharge() * kc) / Math.pow(vec_pos.dot(vec_pos), 1.5));
                vec_pos.mul(d_E);
                particle2.field.add(vec_pos);
                particle2.markDirty();
                particle2.needsUpdate(!freeze);
            }

            //for sensors, we calculate the entire field again
            if (world.getBlockEntity(pos_block) instanceof ElectricFieldSensorBlockEntity sensor && !pos.equals(pos_block) ){
                sensor.calculateField(world, pos_block, pos);

                //Vector3f vec_pos = new Vector3f(pos.getX()-pos_block.getX(), pos.getY()-pos_block.getY(), pos.getZ()-pos_block.getZ());
                //float d_E = (float) ((getCharge() * 1 * kc) / Math.pow(vec_pos.dot(vec_pos), 1.5));
                //vec_pos.scale(d_E);
                //sensor.getField().add(vec_pos);
                sensor.markDirty();
                sensor.sync();
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

    private Direction movementDirection(World world, BlockPos pos, Vector3f oldField) {
        if (oldField.equals(0, 0, 0)) {
            return null;
        }
        ArrayList<Float> list = new ArrayList<>();
        list.add(Math.abs(oldField.x));
        list.add(Math.abs(oldField.y));
        list.add(Math.abs(oldField.z));
        float max = Collections.max(list);
        if (max < e_move ) {
            return null;
        }
        oldField.mul(1/max);
        Vector3f movement = new Vector3f(Math.round(oldField.x), Math.round(oldField.y), Math.round(oldField.z));
        if (movement.dot(movement) > 1) {
            if (Math.abs(movement.x) == 1) {
                oldField.set(0, oldField.y, oldField.z);
            } else if (Math.abs(movement.y) == 1) {
                oldField.set(oldField.x, 0, oldField.z);
            }
        }
        Direction dir = Direction.fromVector(Math.round(oldField.x), Math.round(oldField.y), Math.round(oldField.z));

        if (dir == null || !world.getBlockState(pos.offset(dir)).isAir() && !world.getBlockState(pos.offset(dir)).isOf(Blocks.ANIMATED_CHARGED) && movement.dot(movement) > 1) {
            if (checkAnnihilation() != null) {
                needsUpdate(true);
                return dir;
            }
            movement.mul(max);
            dir = movementDirection(world, pos, new Vector3f(oldField));
        }
        return dir;
    }

    public ItemStack getInventory() {
        return ItemStack.EMPTY;
    }

    public static void tick(World world, BlockPos pos, BlockState state, ChargedBlockEntity be) {
        be.tick(world, pos, state);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (!world.isClient) {
            if (field == null) {
                makeField(world, pos, false);
            }
            if (world.getTime() % 10 == 0) {
                if (decay()) {
                    world.removeBlockEntity(pos);
                    world.removeBlock(pos, false);
                    if (decay_drop != null) {
                        for (ItemStack itemStack : decay_drop) {
                            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack));
                        }
                    }
                    if (decay_replace != null) {
                        world.setBlockState(pos, decay_replace.getDefaultState());
                    }
                    Criteria.COULOMB_FORCE_CRITERION.trigger((ServerWorld) world, pos, 5, (condition) -> condition.test(CoulombCriterion.Type.DECAY));
                    markDirty();
                } else {
                    Direction movement_annihilation = this.checkAnnihilation();
                    if (movement_annihilation != null && update_next_tick) {
                        BlockPos nPos = pos.mutableCopy().offset(movement_annihilation);
                        if (world.getBlockEntity(nPos) instanceof ChargedBlockEntity particle2) {
                            BlockState render_state2 = particle2.getCachedState();
                            world.setBlockState(pos, Blocks.ANIMATED_CHARGED.getDefaultState(), Block.NOTIFY_ALL);
                            world.setBlockState(nPos, Blocks.ANIMATED_CHARGED.getDefaultState(), Block.NOTIFY_ALL);
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
                        //System.out.println("OVERRIDING ANYWAY");
                        Direction movement = movementDirection(world, pos, new Vector3f(field));
                        if (movement != null) {
                            BlockPos nPos = pos.mutableCopy().offset(movement);
                            if (world.getBlockState(nPos).isAir()) {
                                world.setBlockState(pos, Blocks.ANIMATED_CHARGED.getDefaultState(), Block.NOTIFY_ALL);
                                world.setBlockState(nPos, Blocks.CHARGED_PLACEHOLDER.getDefaultState(), Block.NOTIFY_ALL);
                                if (world.getBlockEntity(pos) instanceof AnimatedChargedBlockEntity animation1) {
                                    animation1.movement_direction = movement;
                                    animation1.render_state = getCachedState();
                                    animation1.setInventory(getInventory());
                                }
                            }
                            markDirty();
                        }
                        update_next_tick = false;
                    }
                }
            }
        }
    }
}
