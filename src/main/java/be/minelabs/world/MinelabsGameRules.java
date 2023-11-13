package be.minelabs.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class MinelabsGameRules {

    public static final GameRules.Key<GameRules.BooleanRule> RANDOM_QUANTUM_DROPS = GameRuleRegistry.register(
            "randomQuantumDrops", GameRules.Category.DROPS, GameRuleFactory.createBooleanRule(true));

    public static final GameRules.Key<GameRules.BooleanRule> BOHR_PROJECTILES = GameRuleRegistry.register(
            "bohrProjectiles", GameRules.Category.SPAWNING, GameRuleFactory.createBooleanRule(true));

    public static final GameRules.Key<GameRules.BooleanRule> ALLOW_CHEMICAL_PROJECTILES = GameRuleRegistry.register(
            "allowChemicalProjectiles", GameRules.Category.SPAWNING, GameRuleFactory.createBooleanRule(true));

    public static void onInitialize() {
    }
}
