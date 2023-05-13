package be.minelabs.advancement.criterion;

import net.minecraft.advancement.criterion.Criterion;

public class Criteria {

    public static LCTCriterion LCT_CRITERION = register(new LCTCriterion());
    public static CoulombCriterion COULOMB_FORCE_CRITERION = register(new CoulombCriterion());
    public static ErlenmeyerCriterion ERLENMEYER_CRITERION = register(new ErlenmeyerCriterion());
    public static BohrCriterion BOHR_CRITERION = register(new BohrCriterion());

    private static <T extends Criterion<?>> T register(T c) {
        return net.minecraft.advancement.criterion.Criteria.register(c);
    }

    public static void registerCriteria() {

    }
}
