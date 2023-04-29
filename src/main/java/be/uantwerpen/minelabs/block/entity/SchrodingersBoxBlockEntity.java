package be.uantwerpen.minelabs.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.BlockPos;

public class SchrodingersBoxBlockEntity extends ChestBlockEntity {
    protected SchrodingersBoxBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public SchrodingersBoxBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }


}
