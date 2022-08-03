package be.uantwerpen.scicraft.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SplashColor extends StatusEffect {
    public SplashColor(int color) {
        super(StatusEffectCategory.BENEFICIAL, color);
    }
}
