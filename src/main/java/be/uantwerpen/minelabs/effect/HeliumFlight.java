package be.uantwerpen.minelabs.effect;

import be.uantwerpen.minelabs.item.BalloonItem;
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
                    System.out.println("aangeroepen 2");
                    if (!pe.getAbilities().allowFlying) {
                        pe.getAbilities().allowFlying = true;
                        ((ClientPlayerEntity) entity).sendAbilitiesUpdate();
                        System.out.println("aangeroepen 2BIS");
                    }
                } else {
                    System.out.println("aangeroepen 3b");
                    pe.getAbilities().allowFlying = false;
                    pe.getAbilities().flying = false;
                    ((ClientPlayerEntity) entity).sendAbilitiesUpdate();
                    System.out.println("aangeroepen 3bBIS");

                }
            }
        }
    }
}
