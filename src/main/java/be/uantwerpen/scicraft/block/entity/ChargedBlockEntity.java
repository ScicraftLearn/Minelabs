package be.uantwerpen.scicraft.block.entity;

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

import java.util.Objects;

public class ChargedBlockEntity extends BlockEntity{
    private double charge;
    private Vec3f field;
    private boolean update_next_tick = false;

    public ChargedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, double charge) {
        super(type, pos, state);
        this.charge = charge;
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
        if ((world.getTime() + pos.asLong()) % 10 == 0) {
            int e_radius = 6;
            double kc = 1;
            double e_move = .8;
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
    }

    private Vec3i movementDirection(World world, BlockPos pos) {
        Vec3d field_abs = new Vec3d(Math.abs(field.getX()), Math.abs(field.getY()), Math.abs(field.getZ()));
        int movement = 0;
        if (field_abs.getX() >= field_abs.getY()) {
            if (field_abs.getX() >= field_abs.getZ()) {
                movement = field.getX() > 0 ? 1 : -1; // XYZ & XZY
            } else {
                movement = field.getZ() > 0 ? 3 : -3; // ZXY
            }
        } else if (field_abs.getY() >= field_abs.getZ()) {
            movement = field.getY() > 0 ? 2 : -2; // YZX & YXZ
        } else {
            movement = field.getZ() > 0 ? 3 : -3; // ZYX
        }
        return int2vec(movement);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        // TODO: create a whole "lifecycle" for a charged particle, begining from either placement by user or by algorithm and checking if algorithms in client and server are still in sync.
        if (!world.isClient) {
            this.updateField(world, pos);
            if (update_next_tick) {
                Vec3i movement = movementDirection(world, pos);
                if (!movement.equals(Vec3i.ZERO)) {
                    BlockPos nPos = pos.mutableCopy().add(movement);
                    if (world.getBlockState(nPos).isAir()) {
                        update_next_tick = false;
                        world.removeBlockEntity(pos);
                        world.removeBlock(pos, false);
                        world.setBlockState(pos, be.uantwerpen.scicraft.block.Blocks.ANIMATED_CHARGED.getDefaultState(), Block.NOTIFY_ALL);
                        world.setBlockState(nPos, be.uantwerpen.scicraft.block.Blocks.CHARGED_PLACEHOLDER.getDefaultState(), Block.NOTIFY_ALL);
                        if (world.getBlockEntity(pos) instanceof AnimatedChargedBlockEntity particle2) {
                            particle2.movement_direction = movement;
                            particle2.render_state = getCachedState();
                        }
                    }
                    markDirty();
                } else {
                    update_next_tick = false;
                }
            }
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, ChargedBlockEntity be) {
        be.tick(world, pos, state);
    }

}
