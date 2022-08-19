package be.uantwerpen.scicraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

import static be.uantwerpen.scicraft.block.entity.BlockEntities.QUANTUM_FIELD_BLOCK_ENTITY;

public class QuantumFieldBlockEntity extends BlockEntity {
    public static final int max_age = 20;
    public static final int decayrate = 6;
    public static final IntProperty AGE = IntProperty.of("age", 0, max_age);
    public static final int max_light = 20;

    public QuantumFieldBlockEntity(BlockPos pos, BlockState state) {
        super(QUANTUM_FIELD_BLOCK_ENTITY, pos, state);
    }


    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("age", Objects.requireNonNull(getWorld()).getBlockState(getPos()).get(AGE));

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Objects.requireNonNull(getWorld()).setBlockState(getPos(), getCachedState().with(AGE, Math.min(nbt.getInt("age"),max_age)));

    }
}
