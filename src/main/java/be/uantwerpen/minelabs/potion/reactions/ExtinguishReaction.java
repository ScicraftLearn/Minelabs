package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.AbstractCandleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ExtinguishReaction extends Reaction {

    private final int radius;

    public ExtinguishReaction(int radius) {
        this.radius = radius;
    }

    @Override
    protected void react(World world, BlockPos pos) {
        Utils.applyRadius(pos, this.radius, blockPosition -> {
            BlockState block = world.getBlockState(blockPosition);
            if (block.isIn(BlockTags.FIRE)) {
                world.removeBlock(blockPosition, false);
            } else if (AbstractCandleBlock.isLitCandle(block)) {
                AbstractCandleBlock.extinguish(null, block, world, blockPosition);
            } else if (CampfireBlock.isLitCampfire(block)) {
                world.syncWorldEvent(null, 1009, blockPosition, 0);
                CampfireBlock.extinguish(null, world, blockPosition, block);
                world.setBlockState(blockPosition, block.with(CampfireBlock.LIT, false));
            }
        });

        Utils.applyRadius(world, pos, this.radius, this::react);
    }

    @Override
    public void react(LivingEntity entity) {
        entity.setFireTicks(0);
    }
}
