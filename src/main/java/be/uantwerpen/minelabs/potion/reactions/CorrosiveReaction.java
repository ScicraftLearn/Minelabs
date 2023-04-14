package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class CorrosiveReaction extends Reaction {

    private final int radius;

    public CorrosiveReaction(int radius) {
        this.radius = radius;
    }

    @Override
    protected void react(World world, BlockPos pos) {
        Utils.applyRadius(pos, this.radius, block -> {
            BlockState blockState = world.getBlockState(pos);
            if (blockState.getBlock() == net.minecraft.block.Blocks.WATER)
                MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.CLOUD,
                        pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
            else
                if(canReact(world.getBlockState(block)))
                    world.setBlockState(block, Blocks.AIR.getDefaultState());
        });
        Utils.applyRadius(world, pos, radius, this::react);
    }

    @Override
    public void react(LivingEntity entity) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
    }
}
