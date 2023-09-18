package be.minelabs.science;

import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class CoulombGson {

    public String anti_item;
    public Boolean stable;
    public Integer decay_chance;
    public Integer charge;
    public Integer mass;

    public Annihilation annihilation_drop;
    public Decay decay_drop;

    public static class Decay {
        public String item;
        public Integer count;
    }

    public static class Annihilation {
        public String item;
        public Integer count;
    }


    public void validate() {
        if (anti_item == null)
            throw new JsonSyntaxException("Attribute 'anti_item' is missing");
        if (stable == null)
            stable = false;
        if (decay_chance == null && !stable)
            throw new JsonSyntaxException("Attribute 'decay_chance' is missing & 'stable' missing/false");
        if (charge == null)
            throw new JsonSyntaxException("Attribute 'charge' is missing");
        if (mass == null)
            throw new JsonSyntaxException("Attribute 'mass' is missing");
        if (annihilation_drop == null)
            throw new JsonSyntaxException("Attribute 'annihilation_drop' is missing");
        if (annihilation_drop.item == null)
            throw new JsonSyntaxException("Attribute 'annihilation_drop item' is missing");
        if (annihilation_drop.count == null)
            annihilation_drop.count = 1;
        if (!stable) {
            if (decay_drop == null)
                throw new JsonSyntaxException("Attribute 'decay_drop' is missing OR 'stable' is false/missing");
            if (decay_drop.item == null)
                throw new JsonSyntaxException("Attribute 'decay_drop item' is missing OR 'stable' is false/missing");
            if (decay_drop.count == null)
                decay_drop.count = 1;
        }
    }

    public ItemStack getDecayDrop() {
        return new ItemStack(Registries.ITEM.get(new Identifier(decay_drop.item)), decay_drop.count);
    }

    public ItemStack getAnnihilationDrop() {
        return new ItemStack(Registries.ITEM.get(new Identifier(annihilation_drop.item)), annihilation_drop.count);
    }

    public Item getAntiItem(){
        return Registries.ITEM.get(new Identifier(anti_item));
    }
}
