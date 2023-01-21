package be.uantwerpen.minelabs.advancement.criterion;

import be.uantwerpen.minelabs.Minelabs;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public class LCTMakeBondCriterion extends AbstractCriterion<LCTMakeBondCriterion.Condition> {

    public static final Identifier IDENTIFIER = new Identifier(Minelabs.MOD_ID, "lct_make_bond");

    @Override
    protected Condition conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        int order = obj.getAsJsonPrimitive("order").getAsInt();
        return new Condition(playerPredicate, order);
    }

    @Override
    public Identifier getId() {
        return IDENTIFIER;
    }

    @Override
    public void trigger(ServerPlayerEntity player, Predicate<Condition> predicate) {
        super.trigger(player, predicate);
    }

    public void trigger(ServerPlayerEntity player, int order) {
        trigger(player, condition -> condition.test(order));
    }

    public static class Condition extends AbstractCriterionConditions {

        private final int order;

        private Condition(EntityPredicate.Extended playerPredicate, int order) {
            super(IDENTIFIER, playerPredicate);
            this.order = order;
        }

        private boolean test(int bondMadeOrder){
            return this.order == bondMadeOrder;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("order", new JsonPrimitive(order));
            return jsonObject;
        }
    }

}
