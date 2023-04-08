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
        if (typeJson == null)
            throw new JsonParseException("Missing type for BohrCriterion");
        String typeStr = typeJson.getAsString().toUpperCase();

        try {
            BohrCriterion.Type type = BohrCriterion.Type.valueOf(typeStr);
            return new BohrCriterion.Condition(playerPredicate, type);
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Invalid type found: " + typeStr);
        }
    }

    @Override
    public Identifier getId() {
        return IDENTIFIER;
    }

    public void trigger(ServerPlayerEntity player, Type type) {
        trigger(player, condition -> condition.test(type));
    }

    public static class Condition extends AbstractCriterionConditions {

        private final Type type;

        public Condition(EntityPredicate.Extended playerPredicate, Type type) {
            super(IDENTIFIER, playerPredicate);
            this.type = type;
        }

        public boolean test(Type type) {
            return this.type == type;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("type", new JsonPrimitive(type.toString()));
            return jsonObject;
        }
    }
}
