package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.util.Tags;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class GreenFire extends AbstractFireBlock {

    public GreenFire(Settings settings, float damage) {
        super(settings, damage);
    }

    public static boolean isCopperBase(BlockState state){
        return state.isIn(Tags.Blocks.COPPER_BLOCKS);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return this.canPlaceAt(state, world, pos) ? this.getDefaultState() : Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return isCopperBase(world.getBlockState(pos.down()));
    }

    @Override
    protected boolean isFlammable(BlockState state) {
        return true;
    }
}
