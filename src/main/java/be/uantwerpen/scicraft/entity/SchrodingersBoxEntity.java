package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.block.ExtraChestTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class SchrodingersBoxEntity extends ChestBlockEntity {

    protected SchrodingersBoxEntity(BlockPos blockPos, BlockState blockState) {
        super(ExtraChestTypes.SCHRODINGERS_BOX.getBlockEntityType(), blockPos, blockState);
    }
}
