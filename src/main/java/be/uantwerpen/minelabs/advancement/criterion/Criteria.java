package be.uantwerpen.minelabs.advancement.criterion;

import net.minecraft.advancement.criterion.Criterion;

public class Criteria {


    public static LCTMakeBondCriterion LCT_MAKE_BOND_CRITERION = register(new LCTMakeBondCriterion());


    private static <T extends Criterion<?>> T register(T c){
        return net.minecraft.advancement.criterion.Criteria.register(c);
    }

    public static void registerCriteria(){

    }


}
