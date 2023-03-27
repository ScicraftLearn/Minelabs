package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlammableReaction extends Reaction {

    private final int duration;
    private final boolean pyrophoric;

    public FlammableReaction(int duration, boolean pyrophoric) {
        this.duration = duration;
        this.pyrophoric = pyrophoric;
    }

    private static boolean isBlockNearby(World world, BlockPos source, BlockState blockState, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockState state = world.getBlockState(source.add(x, y, z));
                    if (state == blockState) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void react(World world, double x, double y, double z, BlockHitResult hitResult) {
        BlockPos neighbourPos = hitResult.getBlockPos().offset(hitResult.getSide());
        if (world.getBlockState(neighbourPos).getBlock() == Blocks.AIR)
            if(this.pyrophoric || isBlockNearby(world, neighbourPos, Blocks.TORCH.getDefaultState(), 3))
                world.setBlockState(neighbourPos, Blocks.FIRE.getDefaultState().with(FireBlock.AGE, 1));
    }

    @Override
    protected void react(World world, double x, double y, double z, EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity livingEntity)
            if(this.pyrophoric || isBlockNearby(world, hitResult.getEntity().getBlockPos(), Blocks.TORCH.getDefaultState(), 3))
                livingEntity.setFireTicks(duration);
    }
}
