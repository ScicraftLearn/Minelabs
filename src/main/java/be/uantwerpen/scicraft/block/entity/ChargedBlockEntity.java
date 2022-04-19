package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.ChargedBlock;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
    private Vec3f field = Vec3f.ZERO;
    private boolean update_next_tick = false;
    public long time = 0;
    public boolean is_moving = false;
    public Vec3i movement_direction = Vec3i.ZERO;
    public final int time_move_ticks = 10;
    public boolean waiting_placement;

    public ChargedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, double charge) {
        super(type, pos, state);
        for (Property<?> prop : state.getProperties()) {
            if (prop.getName().equals("placement")) {waiting_placement = (Boolean) state.get(prop);}
        }
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
        tag.putBoolean("is", is_moving);
        tag.putBoolean("wp", waiting_placement);
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
        waiting_placement = tag.getBoolean("wp");
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
        return waiting_placement ? charge : 0;
    }

    public void needsUpdate(boolean b) {
        this.update_next_tick = b;
    }

    private void updateField(World world, BlockPos pos) {
        if ((world.getTime() + pos.asLong()) % 10 == 0) {
            int e_radius = 5;
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
        // Find x, y, z blocks to hop to
        // Determine if hoppable
        // TODO: remove the randomness or update with server to agree on the direction
        Vec3d field2 = new Vec3d(field.getX() * field.getX(), field.getY() * field.getY(), field.getZ() * field.getZ());
        double sum_field2 = field2.dotProduct(new Vec3d(1, 1, 1));
        double random = Math.random(); //Random movement makes it so the client and server can desync
        Vec3i movement = Vec3i.ZERO;
        if (random < (field2.getX() / sum_field2)) {
            movement = field.getX() > 0 ? new Vec3i(1, 0, 0) : new Vec3i(-1, 0, 0);
        } else if (random < ((field2.getX() + field2.getY() )/ sum_field2)) {
            movement = field.getY() > 0 ? new Vec3i(0,1, 0) : new Vec3i(0,-1, 0);
        } else {
            movement = field.getZ() > 0 ? new Vec3i(0,0,1) : new Vec3i(0,0,-1);
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
                time = world.getTime();
                BlockPos nPos = pos.mutableCopy().add(movement);
                if (world.getBlockState(nPos).isAir()) {
                    movement_direction = movement;
                    update_next_tick = false;
                    is_moving = true;
                    if (!world.isClient) { //Random movement makes it so the client and server can desync, only listen to the server
                        world.setBlockState(nPos, state.getBlock().getDefaultState().with(ChargedBlock.PLACEMENT, false), Block.NOTIFY_ALL);

                    }
                }
                markDirty();
            }
        }
        if (is_moving && world.getTime() - time > time_move_ticks) {
            is_moving = false;
            update_next_tick = false; //TODO to late, moving has already finished and we only now check if it's possible
            if (world.getBlockEntity(pos.mutableCopy().add(movement_direction)) instanceof ChargedBlockEntity particle2 && !world.isClient) {
                world.removeBlockEntity(pos);
                world.removeBlock(pos, false);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
                particle2.waiting_placement = true;
            }
            movement_direction = Vec3i.ZERO;
            markDirty();
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, ChargedBlockEntity be) {
        be.tick(world, pos, state);
    }

}
