package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.AbstractCandleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ExtinguishReaction extends Reaction {
    @Override
    protected void react(World world, double x, double y, double z, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        BlockState blockState = world.getBlockState(pos);
        // TODO: this is copied from PotionEntity, rewrite
        if (blockState.isIn(BlockTags.FIRE)) {
            world.removeBlock(pos, false);
        } else if (AbstractCandleBlock.isLitCandle(blockState)) {
            AbstractCandleBlock.extinguish((PlayerEntity)null, blockState, world, pos);
        } else if (CampfireBlock.isLitCampfire(blockState)) {
            world.syncWorldEvent((PlayerEntity)null, 1009, pos, 0);
            CampfireBlock.extinguish(null, world, pos, blockState);
            world.setBlockState(pos, (BlockState)blockState.with(CampfireBlock.LIT, false));
        }
    }

    @Override
    protected void react(World world, double x, double y, double z, EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity livingEntity)
            livingEntity.setFireTicks(0);
    }
}
