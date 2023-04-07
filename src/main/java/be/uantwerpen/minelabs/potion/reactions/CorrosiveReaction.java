package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CorrosiveReaction extends Reaction {

    private final int radius;

    public CorrosiveReaction(int radius) {
        this.radius = radius;
    }

    @Override
    protected void react(World world, Vec3d pos, BlockPos blockPos) {
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.getBlock() == net.minecraft.block.Blocks.WATER)
            // TODO: make this work
            MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.CLOUD,
                    pos.x, pos.y, pos.z, 0, 0, 0);
        else
            // TODO: add degradations
            world.setBlockState(blockPos, Blocks.AIR.getDefaultState());

        Utils.applyRadius(world, pos, radius, livingEntity -> {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
        });
    }
}
