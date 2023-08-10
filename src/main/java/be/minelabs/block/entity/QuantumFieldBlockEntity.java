package be.minelabs.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import static be.minelabs.block.entity.BlockEntities.QUANTUM_FIELD_BLOCK_ENTITY;

public class QuantumFieldBlockEntity extends BlockEntity {

    public QuantumFieldBlockEntity(BlockPos pos, BlockState state) {
        super(QUANTUM_FIELD_BLOCK_ENTITY, pos, state);
    }

}
