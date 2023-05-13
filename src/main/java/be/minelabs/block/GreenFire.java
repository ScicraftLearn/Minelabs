package be.minelabs.block;

import be.minelabs.util.Tags;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

/**
 * @author pixar02
 */
public class GreenFire extends FireBlock {

    public GreenFire(Settings settings) {
        super(settings);
    }

    /**
     * Check if the lit block is of copper
     *
     * @param state : state of the base
     *
     * @return boolean
     */
    public static boolean isCopperBase(BlockState state) {
        return state.isIn(Tags.Blocks.COPPER_BLOCKS);
    }

    /**
     * Can we place this block?
     *
     * @param state : current BlockState
     * @param world : world we are placing in
     * @param pos   : position we are placing at
     * @return boolean
     */
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return super.canPlaceAt(state, world, pos) && isCopperBase(world.getBlockState(pos.down()));
    }
}
