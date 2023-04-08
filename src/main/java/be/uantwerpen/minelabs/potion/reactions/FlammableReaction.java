package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class FlammableReaction extends Reaction {

    private final int duration;
    private final int radius;
    private final boolean pyrophoric;

    public FlammableReaction(int duration, int radius, boolean pyrophoric) {
        this.duration = duration;
        this.radius = radius;
        this.pyrophoric = pyrophoric;
    }

    public FlammableReaction(int duration, int radius, boolean pyrophoric, List<Block> whiteList, List<Block> blackList) {
        super(whiteList, blackList);
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

        });
    }

    @Override
    public void react(LivingEntity entity) {
        if (this.pyrophoric || Utils.isFlameNearby(entity.getWorld(), entity.getBlockPos(), 3))
            entity.setFireTicks(duration);
    }
}
