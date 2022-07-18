package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Atom {
    HYDROGEN(1, "H", 2.1, AtomType.NON_METAL, 1, 0, Items.HYDROGEN_ATOM),
    HELIUM(2, "He", 0, AtomType.NOBLE_GAS, 2, 2, Items.HELIUM_ATOM),

    LITHIUM(3, "Li", 1.0, AtomType.ALKALI_METAL, 1, 4, Items.LITHIUM_ATOM),
    BERYLLIUM(4, "Be", 1.5, AtomType.ALKALINE_EARTH_METAL, 2, 5, Items.BERYLLIUM_ATOM),
    BORON(5, "B", 2.0, AtomType.NON_METAL, 3, 6, Items.BORON_ATOM),
    CARBON(6, "C", 2.5, AtomType.NON_METAL, 4, 6, Items.CARBON_ATOM),
    NITROGEN(7, "N", 3.0, AtomType.NON_METAL, 5, 7, Items.NITROGEN_ATOM),
    OXYGEN(8, "O", 3.5, AtomType.NON_METAL, 6, 8, Items.OXYGEN_ATOM),
    FLUORINE(9, "F", 4.0, AtomType.NON_METAL, 7, 10, Items.FLUORINE_ATOM),
    NEON(10, "Ne", 0, AtomType.NOBLE_GAS, 8, 10, Items.NEON_ATOM),

    SODIUM(11, "Na", 0.9, AtomType.ALKALI_METAL, 1, 12, Items.SODIUM_ATOM),
    MAGNESIUM(12, "Mg", 1.2, AtomType.ALKALINE_EARTH_METAL, 2, 12, Items.MAGNESIUM_ATOM),
    ALUMINIUM(13, "Al", 1.5, AtomType.POST_TRANSITION_METAL, 3, 14, Items.ALUMINIUM_ATOM),
    SILICON(14, "Si", 1.8, AtomType.NON_METAL, 4, 14, Items.SILICON_ATOM),
    PHOSPHORUS(15, "P", 2.1, AtomType.NON_METAL, 5, 16, Items.PHOSPHORUS_ATOM),
    SULFUR(16, "S", 2.5, AtomType.NON_METAL, 6, 16, Items.SULFUR_ATOM),
    CHLORINE(17, "Cl", 3.0, AtomType.NON_METAL, 7, 18, Items.CHLORINE_ATOM),
    ARGON(18, "Ar", 0, AtomType.NOBLE_GAS, 8, 22, Items.ARGON_ATOM),


    //valence electrons of transition metals may not be correct
    POTASSIUM(19, "K", 0.8, AtomType.ALKALI_METAL, 1, 20, Items.POTASSIUM_ATOM),
    CALCIUM(20, "Ca", 1.0, AtomType.ALKALINE_EARTH_METAL, 2, 20, Items.CALCIUM_ATOM),
    IRON(26, "Fe", 1.8, AtomType.TRANSITION_METAL, 2, 30, Items.IRON_ATOM), /*Valence electrons may not be correct */
    COPPER(29, "Cu", 1.9, AtomType.TRANSITION_METAL, 1, 34, Items.COPPER_ATOM), /*Valence electrons may not be correct */
    ZINC(30, "Zn", 1.6, AtomType.TRANSITION_METAL, 2, 34, Items.ZINC_ATOM), /*Valence electrons may not be correct */
    BROMINE(35, "Br", 2.8, AtomType.NON_METAL, 7, 44, Items.BROMINE_ATOM),

    SILVER(47, "Ag", 1.9, AtomType.TRANSITION_METAL, 1, 60, Items.SILVER_ATOM), /*Valence electrons may not be correct */
    CADMIUM(48, "Cd", 1.7, AtomType.TRANSITION_METAL, 2, 66, Items.CADMIUM_ATOM), /*Valence electrons may not be correct */
    TIN(50, "Sn", 1.8, AtomType.POST_TRANSITION_METAL, 4, 70, Items.TIN_ATOM),
    IODINE(53, "I", 2.5, AtomType.NON_METAL, 7, 74, Items.IODINE_ATOM),

    GOLD(79, "Au", 2.4, AtomType.TRANSITION_METAL, 1, 118, Items.GOLD_ATOM), /*Valence electrons may not be correct */
    MERCURY(80, "Hg", 2, AtomType.TRANSITION_METAL, 2, 122, Items.MERCURY_ATOM), /*Valence electrons may not be correct */
    LEAD(82, "Pb", 1.8, AtomType.POST_TRANSITION_METAL, 4, 126, Items.LEAD_ATOM),
    URANIUM(92, "U", 1.7, AtomType.ACTINOID, 2, 146, Items.URANIUM_ATOM); /*Valence electrons may not be correct */


    private final int atomNumber;
    private final String symbol;
    private final double electronegativity;
    private final AtomType type;
    private final int initialValenceElectrons;
    private final int initialNeutrons;

    private Item item;

    Atom(int AN, String symbol, double EN, AtomType type, int initialVE, int initialN, @NotNull Item item) {
        this.atomNumber = AN;
        this.symbol = symbol;
        this.electronegativity = EN;
        this.type = type;
        this.initialValenceElectrons = initialVE;
        this.initialNeutrons = initialN;
        this.item = item;
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
        if (item == null)
            setItem(Registry.ITEM.get(new Identifier(Scicraft.MOD_ID, name().toLowerCase() + "_atom")));
        return item;
    }

    private void setItem(Item item) {
        this.item = item;
    }

    public int getMaxPossibleBonds(){
        if(atomNumber <= 18)
            return Math.min(initialValenceElectrons, 8 - initialValenceElectrons);
        else
            return initialValenceElectrons;
    }

    @Nullable
    public static Atom getBySymbol(String symbol) {
        for (Atom atom : values())
            if (atom.getSymbol().equals(symbol)) return atom;
        return null;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public enum AtomType {
        ALKALI_METAL,
        ALKALINE_EARTH_METAL,
        LANTHANOID,
        ACTINOID,
        TRANSITION_METAL,
        POST_TRANSITION_METAL,
        NON_METAL,
        NOBLE_GAS
    }
}
