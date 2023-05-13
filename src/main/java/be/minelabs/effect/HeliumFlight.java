package be.minelabs.effect;

import be.minelabs.item.BalloonItem;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class HeliumFlight extends StatusEffect {
    public HeliumFlight() {
        super(
                StatusEffectCategory.BENEFICIAL, 0x7070DD
        );
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity pe) {
            if (entity.getWorld().isClient()) {
                if (pe.getOffHandStack().getItem() instanceof BalloonItem) {
                    System.out.println("Try to fly: can we still fly?");
                    if (!pe.getAbilities().allowFlying) {
                        pe.getAbilities().allowFlying = true;
                        ((ClientPlayerEntity) entity).sendAbilitiesUpdate();
                        System.out.println("Try to fly: allow the flight :)");
                    }
                } else {
                    System.out.println("Try to fly: naughty you, you may not fly anymore! 3b");
                    pe.getAbilities().allowFlying = false;
                    pe.getAbilities().flying = false;
                    ((ClientPlayerEntity) entity).sendAbilitiesUpdate();
                    System.out.println("Try to fly: we sorted stuff out, now you're normal again");

                }
            }
        }
    }
}
