package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlammableReaction extends Reaction {

    private final int duration;

    public FlammableReaction(int duration) {
        this.duration = duration;
    }

    @Override
    protected void react(World world, double x, double y, double z, BlockHitResult hitResult) {
        BlockPos neighbourPos = hitResult.getBlockPos().offset(hitResult.getSide());
        if (world.getBlockState(neighbourPos).getBlock() == Blocks.AIR)
            world.setBlockState(neighbourPos, Blocks.FIRE.getDefaultState().with(FireBlock.AGE, 1));
    }

    @Override
    protected void react(World world, double x, double y, double z, EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity livingEntity)
            livingEntity.setFireTicks(duration);
    }
}
