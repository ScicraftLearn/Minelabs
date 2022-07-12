package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.item.Items;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

public enum Atom {
    HYDROGEN(1, "H", 2.1, AtomType.NON_METAL, 1, 0, Items.HYDROGEN_ATOM, Items.HYDROGEN_ATOM_INTERNAL),
    HELIUM(2, "He", 0, AtomType.NOBLE_GAS, 2, 2, Items.HELIUM_ATOM, Items.HELIUM_ATOM_INTERNAL),

    LITHIUM(3, "Li", 1.0, AtomType.METAL, 1, 4, Items.LITHIUM_ATOM, Items.LITHIUM_ATOM_INTERNAL),
    BERYLLIUM(4, "Be", 1.5, AtomType.METAL, 2, 5, Items.BERYLLIUM_ATOM, Items.BERYLLIUM_ATOM_INTERNAL),
    BORON(5, "B", 2.0, AtomType.NON_METAL, 3, 6, Items.BORON_ATOM, Items.BORON_ATOM_INTERNAL),
    CARBON(6, "C", 2.5, AtomType.NON_METAL, 4, 6, Items.CARBON_ATOM, Items.CARBON_ATOM_INTERNAL),
    NITROGEN(7, "N", 3.0, AtomType.NON_METAL, 5, 7, Items.NITROGEN_ATOM, Items.NITROGEN_ATOM_INTERNAL),
    OXYGEN(8, "O", 3.5, AtomType.NON_METAL, 6, 8, Items.OXYGEN_ATOM, Items.OXYGEN_ATOM_INTERNAL),
    FLUORINE(9, "F", 4.0, AtomType.NON_METAL, 7, 10, Items.FLUORINE_ATOM, Items.FLUORINE_ATOM_INTERNAL),
    NEON(10, "Ne", 0, AtomType.NOBLE_GAS, 8, 10, Items.NEON_ATOM, Items.NEON_ATOM_INTERNAL),

    SODIUM(11, "Na", 0.9, AtomType.METAL, 1, 12, Items.SODIUM_ATOM, Items.SODIUM_ATOM_INTERNAL),
    MAGNESIUM(12, "Mg", 1.2, AtomType.METAL, 2, 12, Items.MAGNESIUM_ATOM, Items.MAGNESIUM_ATOM_INTERNAL),
    ALUMINIUM(13, "Al", 1.5, AtomType.METAL, 3, 14, Items.ALUMINIUM_ATOM, Items.ALUMINIUM_ATOM_INTERNAL),
    SILICON(14, "Si", 1.8, AtomType.NON_METAL, 4, 14, Items.SILICON_ATOM, Items.SILICON_ATOM_INTERNAL),
    PHOSPHORUS(15, "P", 2.1, AtomType.NON_METAL, 5, 16, Items.PHOSPHORUS_ATOM, Items.PHOSPHORUS_ATOM_INTERNAL),
    SULFUR(16, "S", 2.5, AtomType.NON_METAL, 6, 16, Items.SULFUR_ATOM, Items.SULFUR_ATOM_INTERNAL),
    CHLORINE(17, "Cl", 3.0, AtomType.NON_METAL, 7, 18, Items.CHLORINE_ATOM, Items.CHLORINE_ATOM_INTERNAL),
    ARGON(18, "Ar", 0, AtomType.NOBLE_GAS, 8, 22, Items.ARGON_ATOM, Items.ARGON_ATOM_INTERNAL),


    //valence electrons of transition metals may not be correct
    POTASSIUM(19,"K",0.8, AtomType.METAL,1,1, Items.POTASSIUM_ATOM, Items.POTASSIUM_ATOM_INTERNAL /* TODO: Add correct number of neutrons */),
    CALCIUM(20,"Ca",1.0, AtomType.METAL,2,1, Items.CALCIUM_ATOM, Items.CALCIUM_ATOM_INTERNAL /* TODO: Add correct number of neutrons */),
    IRON(26,"Fe", 1.8, AtomType.METAL,2,1, Items.IRON_ATOM, Items.IRON_ATOM_INTERNAL /* TODO: Add correct number of neutrons */),
    COPPER(29,"Cu", 1.9, AtomType.METAL,1,1, Items.COPPER_ATOM, Items.COPPER_ATOM_INTERNAL /* TODO: Add correct number of neutrons */),
    ZINC(30, "Zn", 1.6, AtomType.METAL, 2, 1, Items.ZINC_ATOM, Items.ZINC_ATOM_INTERNAL /* TODO: Add correct number of neutrons */),
    BROMINE(35, "Br", 2.8, AtomType.NON_METAL, 7, 1, Items.BROMINE_ATOM, Items.BROMINE_ATOM_INTERNAL /* TODO: Add correct number of neutrons */),

    SILVER(47, "Ag", 1.9, AtomType.METAL, 1, 1, Items.SILVER_ATOM, Items.SILVER_ATOM_INTERNAL /* TODO: Add correct number of neutrons */),
    CADMIUM(48, "Cd", 1.7, AtomType.METAL, 2, 1, Items.CADMIUM_ATOM, Items.CADMIUM_ATOM_INTERNAL /* TODO: Add correct number of neutrons */),
    TIN(50, "Sn", 1.8, AtomType.METAL, 4, 1, Items.TIN_ATOM, Items.TIN_ATOM_INTERNAL /* TODO: Add correct number of neutrons */),
    IODINE(53, "I", 2.5, AtomType.NON_METAL, 7, 1, Items.IODINE_ATOM, Items.IODINE_ATOM_INTERNAL /* TODO: Add correct number of neutrons */),

    GOLD(79, "Au", 2.4, AtomType.METAL, 1, 1, Items.GOLD_ATOM, Items.GOLD_ATOM_INTERNAL /* TODO: Add correct number of neutrons */),
    MERCURY(80, "Hg", 2, AtomType.METAL, 2, 1, Items.MERCURY_ATOM, Items.MERCURY_ATOM_INTERNAL /* TODO: Add correct number of neutrons */),
    LEAD(82, "Pb", 1.8, AtomType.METAL, 4, 1, Items.LEAD_ATOM, Items.LEAD_ATOM_INTERNAL /* TODO: Add correct number of neutrons */),
    URANIUM(92, "U", 1.7, AtomType.METAL, 2, 1, Items.URANIUM_ATOM, Items.URANIUM_ATOM_INTERNAL /* TODO: Add correct number of neutrons */);


    private final int atomNumber;
    private final String symbol;
    private final double electronegativity;
    private final AtomType type;
    private final int initialValenceElectrons;
    private final int initialNeutrons;

    private final Item item;
    private final Item internalItem;

    Atom(int AN, String symbol, double EN, AtomType type, int initialVE, int initialN, Item item, Item internalItem) {
        this.atomNumber = AN;
        this.symbol = symbol;
        this.electronegativity = EN;
        this.type = type;
        this.initialValenceElectrons = initialVE;
        this.initialNeutrons = initialN;
        this.item = item;
        this.internalItem = internalItem;
    }

    public int getAtomNumber() {
        return atomNumber;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getElectronegativity() {
        return electronegativity;
    }

    public AtomType getType() {
        return type;
    }

    public int getInitialValenceElectrons() {
        return initialValenceElectrons;
    }

    public int getInitialNeutrons() {
        return initialNeutrons;
    }

    public Item getItem() {
        return item;
    }

    public Item getInternalItem() {
        return internalItem;
    }

    @Nullable
    public static Atom getBySymbol(String symbol) {
        for (Atom atom : values())
            if (atom.getSymbol().equals(symbol)) return atom;
        return null;
    }

    @Nullable
    public static Atom getByItem(Item item) {
        for (Atom atom : values())
            if (atom.getItem().equals(item)) return atom;
        return null;
    }

    @Nullable
    public static Atom getByInternalItem(Item item) {
        for (Atom atom : values())
            if (atom.getInternalItem().equals(item)) return atom;
        return null;
    }

    public enum AtomType {
        METAL,
        NON_METAL,
        NOBLE_GAS
    }

}
