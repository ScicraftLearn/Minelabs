package be.minelabs.block.blocks;

import be.minelabs.util.Tags;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;

/**
 * @author pixar02
 */
public class GreenFire extends FireBlock {

    public GreenFire(Settings settings) {
        super(settings);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);

        if (isCopperBase(world.getBlockState(pos.down()))){
            // BASE IS COPPER (incase fire was place with command)
            BlockState blockState = world.getBlockState(pos.down());
            Block block = blockState.getBlock();
            if (block instanceof Degradable<?> degradable){
                degradable.tickDegradation(blockState, world, pos.down(), random);
            }
        }
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
