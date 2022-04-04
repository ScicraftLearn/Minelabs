package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.ChargedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ChargedBlock extends Block implements BlockEntityProvider {
    private final double charge;

    public ChargedBlock(Settings settings, double charge_in) {
        super(settings);
        this.charge = charge_in;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ChargedBlockEntity(pos, state, this.charge);
    }
}
