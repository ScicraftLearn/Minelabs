package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class PoisonousReaction extends Reaction {

    private final int radius, duration, amplifier;

    public PoisonousReaction(int radius, int duration, int amplifier) {
        this.radius = radius;
        this.duration = duration;
        this.amplifier = amplifier;
    }
    @Override
    protected void react(World world, double x, double y, double z, BlockHitResult hitResult) {
        for(Entity entity: world.getOtherEntities(null,  new Box(x-radius,y-radius,z-radius,x+radius,y+radius,z+radius)))
            if (entity instanceof LivingEntity livingEntity)
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, duration, amplifier));
    }

    @Override
    protected void react(World world, double x, double y, double z, EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity livingEntity)
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, duration, amplifier));
    }
}
