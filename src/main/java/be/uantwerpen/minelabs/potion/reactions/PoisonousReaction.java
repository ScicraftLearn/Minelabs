package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class PoisonousReaction extends Reaction {

    private final int radius, duration, amplifier;

    public PoisonousReaction(int radius, int duration, int amplifier) {
        this.radius = radius;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    protected void react(World world, BlockPos pos) {
        Utils.applyRadius(world, pos, radius, this::react);
    }

    @Override
    public void react(LivingEntity entity) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, duration, amplifier));
    }
}
