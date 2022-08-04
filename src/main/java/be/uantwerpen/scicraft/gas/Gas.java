package be.uantwerpen.scicraft.gas;

import be.uantwerpen.scicraft.effects.SplashColor;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import org.jetbrains.annotations.Nullable;

public class Gas extends Potion {
    public Gas(StatusEffectInstance... effects) {
        super(effects);
    }

    public Gas(@Nullable String baseName, int color) {
        super(baseName, new StatusEffectInstance(new SplashColor(color), 2));
    }
}
