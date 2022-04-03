package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.entity.ChargedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ChargedBlock extends Block implements BlockEntityProvider {

    public ChargedBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ChargedBlockEntity(pos, state);
    }
}
