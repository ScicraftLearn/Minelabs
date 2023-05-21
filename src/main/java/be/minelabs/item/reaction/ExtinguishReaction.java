package be.minelabs.item.reaction;

import net.minecraft.block.AbstractCandleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ExtinguishReaction extends Reaction {

    private final int radius;

    public ExtinguishReaction(int radius) {
        this.radius = radius;
    }

    @Override
    protected void react(World world, Vec3d pos) {
        Utils.applyRadius(pos, this.radius, blockPosition -> {
            BlockState block = world.getBlockState(BlockPos.ofFloored(blockPosition));
            BlockPos blockPos = BlockPos.ofFloored(pos);
            if (block.isIn(BlockTags.FIRE)) {
                world.removeBlock(blockPos, false);
            } else if (AbstractCandleBlock.isLitCandle(block)) {
                AbstractCandleBlock.extinguish(null, block, world, blockPos);
            } else if (CampfireBlock.isLitCampfire(block)) {
                world.syncWorldEvent(null, 1009, blockPos, 0);
                CampfireBlock.extinguish(null, world, blockPos, block);
                world.setBlockState(blockPos, block.with(CampfireBlock.LIT, false));
            }
        });

        Utils.applyRadius(world, pos, this.radius, this::react);
    }

    @Override
    public void react(LivingEntity entity) {
        entity.setFireTicks(0);
    }
}
