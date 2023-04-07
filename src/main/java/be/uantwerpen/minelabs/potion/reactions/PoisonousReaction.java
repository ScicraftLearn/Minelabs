package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PoisonousReaction extends Reaction {

    private final int radius, duration, amplifier;

    public PoisonousReaction(int radius, int duration, int amplifier) {
        this.radius = radius;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    protected void react(World world, Vec3d position, BlockPos blockPos) {
        Utils.applyRadius(world, position, radius, e -> e.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, duration, amplifier)));
    }
}
