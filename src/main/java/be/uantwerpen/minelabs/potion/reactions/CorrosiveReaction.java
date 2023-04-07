package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class CorrosiveReaction extends Reaction {
    @Override
    protected void react(World world, double x, double y, double z, BlockHitResult hitResult) {
        BlockState blockState = world.getBlockState(hitResult.getBlockPos());
        if (blockState.getBlock() == net.minecraft.block.Blocks.WATER)
            // TODO: make this work
            MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.CLOUD,
                    x, y, z, 0, 0, 0);
        else
            // TODO: add degradations
            world.setBlockState(hitResult.getBlockPos(), Blocks.AIR.getDefaultState());
    }

    @Override
    protected void react(World world, double x, double y, double z, EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity livingEntity)
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));

    }
}
