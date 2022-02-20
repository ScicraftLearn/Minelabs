package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.util.Tags;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

/**
 * TODO ART + Animation
 * TODO Half slab fire ??
 * @author pixar02
 */
public class GreenFire extends AbstractFireBlock {

    public GreenFire(Settings settings, float damage) {
        super(settings, damage);
    }

    /**
     * Check if the lit block is of copper
     *
     * @param state : stat of the base
     *
     * @return boolean
     */
    public static boolean isCopperBase(BlockState state){
        return state.isIn(Tags.Blocks.COPPER_BLOCKS);
    }

    /**
     *
     * @param state : current state of block
     * @param direction: Direction of the block
     * @param neighborState : BlockState of neighbor
     * @param world : world of the block
     * @param pos : pos of block
     * @param neighborPos : position of neighbor
     *
     * @deprecated No way around it....
     *
     * @return {@link BlockState} what state to place
     */
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return this.canPlaceAt(state, world, pos) ? this.getDefaultState() : Blocks.AIR.getDefaultState();
    }

    /**
     * Can we place this block?
     *
     * @param state : current BlockState
     * @param world : world we are placing in
     * @param pos : position we are placing at
     * @return boolean
     */
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return isCopperBase(world.getBlockState(pos.down()));
    }

    @Override
    protected boolean isFlammable(BlockState state) {
        return true;
    }
}
