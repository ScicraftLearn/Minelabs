package be.minelabs.block.blocks;

import be.minelabs.block.Blocks;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BuddingAmethystBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

public class BuddingSaltBlock extends BuddingAmethystBlock {

    private static final Direction[] DIRECTIONS = Direction.values();

    public BuddingSaltBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(5) == 0) {
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);
            Block block = null;
            if (canGrowIn(blockState)) {
                block = Blocks.SMALL_SALT_CRYSTAL;
            } else if (blockState.isOf(Blocks.SMALL_SALT_CRYSTAL) && blockState.get(AmethystClusterBlock.FACING) == direction) {
                block = Blocks.MEDIUM_SALT_CRYSTAL;
            } else if (blockState.isOf(Blocks.MEDIUM_SALT_CRYSTAL) && blockState.get(AmethystClusterBlock.FACING) == direction) {
                block = Blocks.LARGE_SALT_CRYSTAL;
            } else if (blockState.isOf(Blocks.LARGE_SALT_CRYSTAL) && blockState.get(AmethystClusterBlock.FACING) == direction) {
                block = Blocks.SALT_CRYSTAL;
            }

            if (block != null) {
                BlockState blockState2 = block.getDefaultState().with(AmethystClusterBlock.FACING, direction).with(AmethystClusterBlock.WATERLOGGED, blockState.getFluidState().getFluid() == Fluids.WATER);
                world.setBlockState(blockPos, blockState2);
            }

        }
    }
}
