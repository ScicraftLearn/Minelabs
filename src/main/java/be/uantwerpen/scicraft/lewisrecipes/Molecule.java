package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.item.Items;
import net.minecraft.item.Item;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Molecule {
    WATER(List.of(Atom.HYDROGEN, Atom.OXYGEN), List.of(2, 1), net.minecraft.item.Items.POTION),
    DIOXYGEN(Collections.singletonList(Atom.OXYGEN), Collections.singletonList(2), Items.ERLENMEYER_CO2),
    DINITROGEN(Collections.singletonList(Atom.NITROGEN), Collections.singletonList(2), Items.ERLENMEYER_N2),
    SALT(List.of(Atom.SODIUM, Atom.CHLORINE), List.of(1, 1), Items.ERLENMEYER /* TODO: Add Salt */),
    METHANE(List.of(Atom.CARBON, Atom.HYDROGEN), List.of(1, 4), Items.ERLENMEYER_CH4),

    DIHYDROGEN(Collections.singletonList(Atom.HYDROGEN), Collections.singletonList(2), Items.ERLENMEYER_H2),
    NITRIC_OXIDE(List.of(Atom.NITROGEN, Atom.OXYGEN), List.of(1, 1), Items.ERLENMEYER /* TODO: Item bestaat nog niet */),
    NITROGEN_DIOXIDE(List.of(Atom.NITROGEN, Atom.OXYGEN), List.of(1, 2), Items.ERLENMEYER /* TODO: Item bestaat nog niet */),
    SODIUM(Collections.singletonList(Atom.SODIUM), Collections.singletonList(1), Items.ERLENMEYER /* TODO: Item bestaat nog niet */),
    DICHLORINE(Collections.singletonList(Atom.CHLORINE), Collections.singletonList(2), Items.ERLENMEYER_Cl2),
    SODIUM_HYDROXIDE(List.of(Atom.SODIUM, Atom.HYDROGEN, Atom.OXYGEN), List.of(1, 1, 1), Items.ERLENMEYER /* TODO: Item bestaat nog niet */),
    CARBON_DIOXIDE(List.of(Atom.CARBON, Atom.OXYGEN), List.of(1, 2), Items.ERLENMEYER_CO2),
    CARBON_MONOXIDE(List.of(Atom.CARBON, Atom.OXYGEN), List.of(1, 1), Items.ERLENMEYER_CO),

    NITRIC_ACID(List.of(Atom.HYDROGEN, Atom.NITROGEN, Atom.OXYGEN), List.of(1, 1, 3), Items.ERLENMEYER_HNO3),
    AMMONIA(List.of(Atom.NITROGEN, Atom.HYDROGEN), List.of(1, 3), Items.ERLENMEYER_NH3),
    AMMONIUM_NITRATE(List.of(Atom.NITROGEN, Atom.HYDROGEN, Atom.OXYGEN), List.of(2, 4, 3), Items.ERLENMEYER /* TODO: Item bestaat nog niet */),
    NITROUS_OXIDE(List.of(Atom.NITROGEN, Atom.OXYGEN), List.of(2, 1), Items.ERLENMEYER_N2O),
    HYDROGEN_CLORIDE(List.of(Atom.HYDROGEN, Atom.CHLORINE), List.of(1, 1), Items.ERLENMEYER_HCl),
    SODIUM_OXIDE(List.of(Atom.SODIUM, Atom.OXYGEN), List.of(2, 1), Items.ERLENMEYER /* TODO: Item bestaat nog niet */);

    private final Map<Atom, Integer> ingredients;
    private final Item item;

    Molecule(List<Atom> atoms, List<Integer> amounts, Item item) {
        HashMap<Atom, Integer> ingredients = new HashMap<>();
        for (int i = 0; i < atoms.size(); i++)
            ingredients.put(atoms.get(i), amounts.get(i));
        this.ingredients = ingredients;
        this.item = item;
    }

    public Map<Atom, Integer> getIngredients() {
        return ingredients;
    }

    public Item getItem() {
        return item;
    }
}
