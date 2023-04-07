package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FlammableReaction extends Reaction {

    private final int duration;
    private final int radius;
    private final boolean pyrophoric;

    public FlammableReaction(int duration, int radius, boolean pyrophoric) {
        this.duration = duration;
        this.radius = radius;
        this.pyrophoric = pyrophoric;
    }

    @Override
    protected void react(World world, Vec3d pos, BlockPos blockPos) {
        Utils.applyRadius(blockPos, radius, block -> {
            if (world.getBlockState(block).getBlock() == Blocks.AIR) {
                if (this.pyrophoric || Utils.isFlameNearby(world, block, 3))
                    world.setBlockState(block, Blocks.FIRE.getDefaultState().with(FireBlock.AGE, 1));
            }
        });
        Utils.applyRadius(world, pos, radius, e -> {
            if (this.pyrophoric || Utils.isFlameNearby(world, e.getBlockPos(), 3))
                e.setFireTicks(duration);
        });
    }
}
