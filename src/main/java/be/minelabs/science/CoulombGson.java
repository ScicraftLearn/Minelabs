package be.minelabs.science;

import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class CoulombGson {

    public String anti_item;
    public Boolean stable;
    public Float decay_chance;
    public Integer charge;
    public Float mass;

    public Annihilation annihilation_drop;
    public Decay decay_drop;


    public static class Decay {
        public String item;
        public Integer count;
        public String replace;
    }

    public static class Annihilation {
        public String item;
        public Integer count;
    }

    public void validate() {
        if (anti_item == null)
            anti_item = "";
        if (!anti_item.isEmpty() || !anti_item.isBlank()) {
            if (annihilation_drop == null)
                throw new JsonSyntaxException("Attribute 'annihilation_drop' is missing");
            if (annihilation_drop.item == null)
                throw new JsonSyntaxException("Attribute 'annihilation_drop item' is missing");
            if (annihilation_drop.count == null)
                annihilation_drop.count = 1;
        }
        if (stable == null)
            stable = false;
        if (decay_chance == null && !stable)
            throw new JsonSyntaxException("Attribute 'decay_chance' is missing & 'stable' missing/false");
        if (charge == null)
            throw new JsonSyntaxException("Attribute 'charge' is missing");
        if (mass == null)
            throw new JsonSyntaxException("Attribute 'mass' is missing");

        if (!stable) {
            if (decay_drop == null)
                throw new JsonSyntaxException("Attribute 'decay_drop' is missing OR 'stable' is false/missing");
            if (decay_drop.item == null && decay_drop.replace == null)
                throw new JsonSyntaxException("Attribute 'decay_drop' is missing an 'item' or 'replace' item attribute");
            if (decay_drop.item == null)
                decay_drop.item = "minecraft:air";
            if (decay_drop.count == null)
                decay_drop.count = 1;
            if (decay_drop.replace == null) {
                decay_drop.replace = "";
            }
        }
    }

    public ItemStack getDecayDrop() {
        return new ItemStack(Registries.ITEM.get(new Identifier(decay_drop.item)), decay_drop.count);
    }

    public boolean shouldReplace() {
        return !decay_drop.replace.isEmpty() || !decay_drop.replace.isBlank();
    }

    public ItemStack getDecayReplacement() {
        return new ItemStack(Registries.ITEM.get(new Identifier(decay_drop.replace)));
    }

    public ItemStack getAnnihilationDrop() {
        return new ItemStack(Registries.ITEM.get(new Identifier(annihilation_drop.item)), annihilation_drop.count);
    }

    public Item getAntiItem() {
        if (anti_item == null || anti_item.isEmpty() || anti_item.isBlank()) {
            return null;
        }
        return Registries.ITEM.get(new Identifier(anti_item));
    }
}
