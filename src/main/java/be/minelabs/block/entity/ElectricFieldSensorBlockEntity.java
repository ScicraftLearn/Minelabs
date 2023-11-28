package be.minelabs.block.entity;

import be.minelabs.entity.projectile.thrown.ChargedEntity;
import be.minelabs.world.MinelabsGameRules;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ElectricFieldSensorBlockEntity extends BlockEntity {

    private Vec3d field = new Vec3d(0, 0, 0);

    public ElectricFieldSensorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public Vec3d getField() {
        return field;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        if (field != null) {
            nbt.putDouble("ex", field.x);
            nbt.putDouble("ey", field.y);
            nbt.putDouble("ez", field.z);
        }
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("ex")) {
            field = new Vec3d(nbt.getDouble("ex"), nbt.getDouble("ey"), nbt.getDouble("ez"));
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbtWithIdentifyingData();
    }

    public void calculateField(World world, BlockPos pos, BlockState state) {
        int e_radius = world.getGameRules().getInt(MinelabsGameRules.E_RADIUS);
        Iterable<Entity> entities_in_radius = world.getOtherEntities(null, Box.of(pos.toCenterPos(), e_radius, e_radius, e_radius));

        field = Vec3d.ZERO; // Clean field / RESET the field

        Vec3d vec_pos = pos.toCenterPos();
        for (Entity entity : entities_in_radius) {
            if (entity instanceof ChargedEntity charged) {
                double force = 8.987f * charged.getCharge() / charged.getPos().distanceTo(vec_pos);
                Vec3d vector = vec_pos.subtract(charged.getPos()).normalize(); // Vector between entities
                vector = vector.multiply(force); //scale vector with Force
                vector = vector.multiply(0.1);

                field = field.add(vector);
            }
        }
        if (field.length() >= ChargedEntity.MAX_FIELD) {
            field = field.multiply(ChargedEntity.MAX_FIELD / field.length()); // SCALE TO MAX_FIELD
        }

        markDirty(world, pos, state);
        world.markDirty(pos);
        world.updateListeners(pos, getCachedState(), getCachedState(), 3);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        calculateField(world, pos, state);
    }
}
