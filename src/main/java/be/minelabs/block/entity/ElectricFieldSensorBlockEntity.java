package be.minelabs.block.entity;

import be.minelabs.entity.ChargedEntity;
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

    private Vec3d field = Vec3d.ZERO;

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

    public void calculateField(World world, BlockPos pos) {
        int e_radius = ChargedEntity.e_radius;
        Iterable<Entity> entities_in_radius = world.getOtherEntities(null, Box.of(pos.toCenterPos(), e_radius, e_radius, e_radius));

        field = Vec3d.ZERO;
        for (Entity entity : entities_in_radius) {
            if (entity instanceof ChargedEntity chargedEntity) {
                if (!chargedEntity.getPos().equals(pos.toCenterPos()) && chargedEntity.getVelocity().length() > .0001) {
                    // Moving + not in sensor
                    Vec3d vec_pos = getPos().toCenterPos().subtract(chargedEntity.getPos()).normalize();
                    double force = 8.987f * chargedEntity.getCharge() / getPos().toCenterPos().squaredDistanceTo(chargedEntity.getPos());
                    vec_pos.multiply(force * 100);
                    field.add(vec_pos);
                }
            }
        }
        markDirty();
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        calculateField(world, pos);
    }
}
