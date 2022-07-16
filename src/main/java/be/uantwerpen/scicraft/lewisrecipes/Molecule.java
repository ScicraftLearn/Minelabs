package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.item.Items;
import net.minecraft.item.Item;

import java.util.Map;
import java.util.Objects;

public class Molecule extends PartialMolecule{
//    WATER(Map.of(Atom.HYDROGEN, 2, Atom.OXYGEN, 1), net.minecraft.item.Items.POTION),
//    DIOXYGEN(Map.of(Atom.OXYGEN, 2), Items.ERLENMEYER_CO2),
//    DINITROGEN(Map.of(Atom.NITROGEN, 2), Items.ERLENMEYER_N2),
//    SALT(Map.of(Atom.SODIUM, 1, Atom.CHLORINE, 1), Items.ERLENMEYER /* TODO: Add Salt */),
//    METHANE(Map.of(Atom.CARBON, 1, Atom.HYDROGEN, 4), Items.ERLENMEYER_CH4),
//
//    DIHYDROGEN(Map.of(Atom.HYDROGEN, 2), Items.ERLENMEYER_H2),
//    NITRIC_OXIDE(Map.of(Atom.NITROGEN, 1, Atom.OXYGEN, 1), Items.ERLENMEYER /* TODO: Item bestaat nog niet */),
//    NITROGEN_DIOXIDE(Map.of(Atom.NITROGEN, 1, Atom.OXYGEN, 2), Items.ERLENMEYER /* TODO: Item bestaat nog niet */),
//    SODIUM(Map.of(Atom.SODIUM, 1), Items.ERLENMEYER /* TODO: Item bestaat nog niet */),
//    DICHLORINE(Map.of(Atom.CHLORINE, 2), Items.ERLENMEYER_Cl2),
//    SODIUM_HYDROXIDE(Map.of(Atom.SODIUM, 1, Atom.HYDROGEN, 1, Atom.OXYGEN, 1), Items.ERLENMEYER /* TODO: Item bestaat nog niet */),
//    CARBON_DIOXIDE(Map.of(Atom.CARBON, 1, Atom.OXYGEN, 2), Items.ERLENMEYER_CO2),
//    CARBON_MONOXIDE(Map.of(Atom.CARBON, 1, Atom.OXYGEN, 1), Items.ERLENMEYER_CO),
//
//    NITRIC_ACID(Map.of(Atom.HYDROGEN, 1, Atom.NITROGEN, 1, Atom.OXYGEN, 3), Items.ERLENMEYER_HNO3),
//    AMMONIA(Map.of(Atom.NITROGEN, 1, Atom.HYDROGEN, 3), Items.ERLENMEYER_NH3),
//    AMMONIUM_NITRATE(Map.of(Atom.NITROGEN, 2, Atom.HYDROGEN, 4, Atom.OXYGEN, 3), Items.ERLENMEYER /* TODO: Item bestaat nog niet */),
//    NITROUS_OXIDE(Map.of(Atom.NITROGEN, 2, Atom.OXYGEN, 1), Items.ERLENMEYER_N2O),
//    HYDROGEN_CHLORIDE(Map.of(Atom.HYDROGEN, 1, Atom.CHLORINE, 1), Items.ERLENMEYER_HCl),
//    SODIUM_OXIDE(Map.of(Atom.SODIUM, 2, Atom.OXYGEN, 1), Items.ERLENMEYER /* TODO: Item bestaat nog niet */);

    private final Item item;

    Molecule(MoleculeGraph structure, Item item) {
        super(structure);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Molecule molecule)) return false;
        if (!super.equals(o)) return false;
        return item.equals(molecule.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), item);
    }
}
