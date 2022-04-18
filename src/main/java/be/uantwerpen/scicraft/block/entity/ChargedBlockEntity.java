package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.Scicraft;
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

public class ChargedBlockEntity extends BlockEntity{
    private double charge;
    private Vec3f field = Vec3f.ZERO;
    private boolean update_next_tick = false;
    public long time = 0;
    public boolean is_moving = false;
    public Vec3i movement_direction = Vec3i.ZERO;

    public ChargedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, double charge) {
        super(type, pos, state);
        this.charge = charge;
    }
    @Override
    public void writeNbt(NbtCompound tag) {
        // Save the current value of the number to the tag
        tag.putFloat("ex", field.getX());
        tag.putFloat("ey", field.getY());
        tag.putFloat("ez", field.getZ());
        tag.putDouble("q", charge);
        tag.putBoolean("ut", update_next_tick);
        tag.putBoolean("is", is_moving);
        tag.putLong("time", time);
        tag.putInt("mdx", movement_direction.getX());
        tag.putInt("mdy", movement_direction.getY());
        tag.putInt("mdz", movement_direction.getZ());
        super.writeNbt(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        field = new Vec3f(tag.getFloat("ex"), tag.getFloat("ey"), tag.getFloat("ez"));
        charge = tag.getDouble("q");
        update_next_tick = tag.getBoolean("ut");
        is_moving = tag.getBoolean("is");
        time = tag.getLong("time");
        movement_direction = new Vec3i(tag.getInt("mdx"), tag.getInt("mdy"), tag.getInt("mdz"));
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
        if ((world.getTime() + pos.asLong()) % 5 == 0) {
            int e_radius = 5;
            double kc = 1;
            double e_move = .8;
            Iterable<BlockPos> blocks_in_radius = BlockPos.iterate(pos.mutableCopy().add(-e_radius, -e_radius, -e_radius), pos.mutableCopy().add(e_radius, e_radius, e_radius));
            field = Vec3f.ZERO;
            for (BlockPos pos_block : blocks_in_radius) {
                if (world.getBlockEntity(pos_block) instanceof ChargedBlockEntity particle2 && !pos.equals(pos_block)) {
                    Vec3f vec_pos = new Vec3f(pos.getX()-pos_block.getX(), pos.getY()-pos_block.getY(), pos.getZ()-pos_block.getZ());
                    float d_E = (float) ((getCharge() * particle2.getCharge() * kc) / Math.pow(Math.sqrt(vec_pos.dot(vec_pos)), 3.));
                    vec_pos.scale(d_E);
                    field.subtract(vec_pos);
                }
            }
            needsUpdate(field.dot(field) > e_move);
            markDirty();
        }
    }

    private Vec3i movementDirection(World world, BlockPos pos) {
        // Find x, y, z blocks to hop to
        // Determine if hoppable
        Vec3d field2 = new Vec3d(field.getX() * field.getX(), field.getY() * field.getY(), field.getZ() * field.getZ());
        double sum_field2 = field2.dotProduct(Vec3d.ZERO);
        double random = Math.random();
        Vec3i movement =Vec3i.ZERO;
        if (random < (field2.getX() / sum_field2)) {
            movement = movement.add((int) Math.signum(field.getX()), 0, 0);
        } else if (random < (field2.getY() / sum_field2)) {
            movement = movement.add(0, (int) Math.signum(field.getY()), 0);
        } else {
            movement = movement.add(0, 0, (int) Math.signum(field.getZ()));
        }
        if (!world.getBlockState(pos.add(movement)).isAir()) {
            movement = Vec3i.ZERO;
        }
        return movement;
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        this.updateField(world, pos);
        if (update_next_tick && !is_moving) { //TODO check if moving is possible first
            Vec3i movement = movementDirection(world, pos);
            if (!movement.equals(Vec3i.ZERO)) {
                update_next_tick = false;
                is_moving = true;
                time = world.getTime();
                movement_direction = movement;
                markDirty();
            }
        }
        if (is_moving && world.getTime() - time > 4) {
            is_moving = false;
            update_next_tick = false;
            BlockPos nPos = pos.mutableCopy().add(movement_direction); //TODO to late, moving has already finished and we only now check if it's possible
            if (world.getBlockState(nPos).isAir()) {
                world.setBlockState(nPos, state.getBlock().getDefaultState(), Block.NOTIFY_ALL);
                world.removeBlockEntity(pos);
                world.removeBlock(pos, false);
            }
            movement_direction = Vec3i.ZERO;
            markDirty();
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, ChargedBlockEntity be) {
        be.tick(world, pos, state);
    }

}
