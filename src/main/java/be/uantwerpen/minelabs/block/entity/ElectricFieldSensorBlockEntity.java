package be.uantwerpen.minelabs.block.entity;

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
import org.jetbrains.annotations.Nullable;

public class ElectricFieldSensorBlockEntity extends BlockEntity {

    private Vec3f field = new Vec3f(0,0,0);

    public ElectricFieldSensorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public Vec3f getField() {
        return field;
    }

    public void setField(Vec3f f) {
        field = f;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        if (field != null) {
            nbt.putFloat("ex", field.getX());
            nbt.putFloat("ey", field.getY());
            nbt.putFloat("ez", field.getZ());
        }
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("ex")) {
            field = new Vec3f(nbt.getFloat("ex"), nbt.getFloat("ey"), nbt.getFloat("ez"));
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbtWithIdentifyingData();
    }

    public void sync() {
        world.updateListeners(pos,getCachedState(),getCachedState(),3);
    }

    public void calculateField(World world, BlockPos pos, BlockPos removedBlockPos) {
        int e_radius = 12;
        double kc = 1;
        Iterable<BlockPos> blocks_in_radius = BlockPos.iterate(pos.mutableCopy().add(-e_radius, -e_radius, -e_radius), pos.mutableCopy().add(e_radius, e_radius, e_radius));
        field = new Vec3f(0f, 0f, 0f);
        for (BlockPos pos_block : blocks_in_radius) {

            //if a charge is removed, we calculate the entire field of the sensor again
            //otherwise if you only calculate the relative field, rounding errors will occur almost always
            //since this function is called when the block still exists, we need to skip the block
            if(pos_block.equals(removedBlockPos)) {
                continue;
            }
            if (world.getBlockEntity(pos_block) instanceof ChargedBlockEntity particle2 && !pos.equals(pos_block) && particle2.getField() != null) {
                Vec3f vec_pos = new Vec3f(pos.getX()-pos_block.getX(), pos.getY()-pos_block.getY(), pos.getZ()-pos_block.getZ());
                float d_E = (float) ((-1 * particle2.getCharge() * kc) / Math.pow(vec_pos.dot(vec_pos), 1.5));
                vec_pos.scale(d_E);
                field.add(vec_pos);
            }
        }
        markDirty();
    }
}
