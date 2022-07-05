package be.uantwerpen.scicraft.lewisrecipes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Molecule {
    WATER(List.of(Atom.HYDROGEN, Atom.OXYGEN), List.of(2, 1)),
    DIOXYGEN(Collections.singletonList(Atom.OXYGEN), Collections.singletonList(2)),
    DINITROGEN(Collections.singletonList(Atom.NITROGEN), Collections.singletonList(2)),
    SALT(List.of(Atom.SODIUM, Atom.CHLORINE), List.of(1, 1)),
    METHANE(List.of(Atom.CARBON, Atom.HYDROGEN), List.of(1, 4)),

    DIHYDROGEN(Collections.singletonList(Atom.HYDROGEN), Collections.singletonList(2)),
    NITRIC_OXIDE(List.of(Atom.NITROGEN, Atom.OXYGEN), List.of(1, 1)),
    NITROGEN_DIOXIDE(List.of(Atom.NITROGEN, Atom.OXYGEN), List.of(1, 2)),
    SODIUM(Collections.singletonList(Atom.SODIUM), Collections.singletonList(1)),
    DICHLORINE(Collections.singletonList(Atom.CHLORINE), Collections.singletonList(2)),
    SODIUM_HYDROXIDE(List.of(Atom.SODIUM, Atom.HYDROGEN, Atom.OXYGEN), List.of(1, 1, 1)),
    CARBON_DIOXIDE(List.of(Atom.CARBON, Atom.OXYGEN), List.of(1, 2)),
    CARBON_MONOXIDE(List.of(Atom.CARBON, Atom.OXYGEN), List.of(1, 1)),

    NITRIC_ACID(List.of(Atom.HYDROGEN, Atom.NITROGEN, Atom.OXYGEN), List.of(1, 1, 3)),
    AMMONIA(List.of(Atom.NITROGEN, Atom.HYDROGEN), List.of(1, 3)),
    AMMONIUM_NITRATE(List.of(Atom.NITROGEN, Atom.HYDROGEN, Atom.OXYGEN), List.of(2, 4, 3)),
    NITROUS_OXIDE(List.of(Atom.NITROGEN, Atom.OXYGEN), List.of(2, 1)),
    HYDROGEN_CLORIDE(List.of(Atom.HYDROGEN, Atom.CHLORINE), List.of(1, 1)),
    SODIUM_OXIDE(List.of(Atom.SODIUM, Atom.OXYGEN), List.of(2, 1));

    private Map<Atom, Integer> ingredients;

    Molecule(List<Atom> atoms, List<Integer> amounts) {
        HashMap<Atom, Integer> ingredients = new HashMap<>();
        for (int i = 0; i < atoms.size(); i++)
            ingredients.put(atoms.get(i), amounts.get(i));
        this.ingredients = ingredients;
    }

    public Map<Atom, Integer> getIngredients() {
        return ingredients;
    }
}
