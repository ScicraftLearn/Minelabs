package be.uantwerpen.scicraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class PionMinusBlockEntity extends ChargedBlockEntity{
    public PionMinusBlockEntity(BlockPos pos, BlockState state, double charge_in) {
        super(BlockEntities.PION_MINUS_BLOCK_ENTITY, pos, state, charge_in);
    }
}
