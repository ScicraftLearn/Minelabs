package be.minelabs.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class MinelabsGameRules {

    public static final GameRules.Key<GameRules.BooleanRule> ALLOW_DECAY = GameRuleRegistry.register(
            "allow_decay", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true));

    public static final GameRules.Key<GameRules.IntRule> E_RADIUS = GameRuleRegistry.register("e_radius",
            GameRules.Category.UPDATES, GameRuleFactory.createIntRule(12));


    public static void onInitialize() {
    }
}
