package be.uantwerpen.scicraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class PionPlusBlockEntity extends ChargedBlockEntity{
    public PionPlusBlockEntity(BlockPos pos, BlockState state, double charge_in) {
        super(BlockEntities.PION_PLUS_BLOCK_ENTITY, pos, state, charge_in);
    }
}
