package be.uantwerpen.scicraft.potion;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.effects.SplashColor;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.registry.Registry;

public class Potions {
    public static final Potion N2;

    public Potions() {

    }

    public static void registerPotions() {
        Scicraft.LOGGER.info("registering potions");
    }

    private static Potion register(String name, Potion potion) {
        return (Potion) Registry.register(Registry.POTION, name, potion);
    }

    static {
        N2 = register("n2", new Potion(new StatusEffectInstance(new SplashColor(261143), 2)));
    }
}
