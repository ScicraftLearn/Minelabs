package be.uantwerpen.minelabs.advancement.criterion;

import be.uantwerpen.minelabs.Minelabs;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class BohrCriterion extends AbstractCriterion<BohrCriterion.Condition> {

    public enum Type {
        ANY,
        ADD_ATOM,
        ADD_PARTICLE,
        REMOVE_PARTICLE,
        REMOVE_ATOM,
        CRAFT_ATOM
    }

    public static final Identifier IDENTIFIER = new Identifier(Minelabs.MOD_ID, "bohr_blueprint");

    @Override
    protected BohrCriterion.Condition conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        JsonPrimitive typeJson = obj.getAsJsonPrimitive("type");
        String typeStr = typeJson == null ? "ANY" : typeJson.getAsString().toUpperCase();
        JsonPrimitive hookJson = obj.getAsJsonPrimitive("hook");
        boolean hookBool = hookJson != null && hookJson.getAsBoolean();

        try {
            Type type = Type.valueOf(typeStr);
            return new BohrCriterion.Condition(playerPredicate, type, hookBool);
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Invalid type found: " + typeStr);
        }
    }

    @Override
    public Identifier getId() {
        return IDENTIFIER;
    }

    public void trigger(ServerPlayerEntity player, Type type) {
        trigger(player, condition -> condition.test(type, false));
    }
    public void trigger(ServerPlayerEntity player, Type type, boolean hook) {
        trigger(player, condition -> condition.test(type, hook));
    }

    public static class Condition extends AbstractCriterionConditions {

        private final Type type;
        private final boolean hook;

        public Condition(EntityPredicate.Extended playerPredicate, Type type, boolean isHook) {
            super(IDENTIFIER, playerPredicate);
            this.type = type;
            this.hook = isHook;
        }

        public boolean test(Type type, boolean hook) {
            return (!this.hook || hook) && (this.type == Type.ANY || this.type == type);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("type", new JsonPrimitive(type.toString()));
            jsonObject.add("hook", new JsonPrimitive(hook));
            return jsonObject;
        }
    }
}
