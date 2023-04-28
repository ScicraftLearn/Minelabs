package be.uantwerpen.minelabs.crafting.molecules;

import be.uantwerpen.minelabs.Minelabs;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Atom {
    HYDROGEN(1, "H", 2.1, AtomType.NON_METAL, 1, 0, 0xFFFFFF),
    HELIUM(2, "He", 0, AtomType.NOBLE_GAS, 2, 2, Atom.Color.CYAN),

    LITHIUM(3, "Li", 1.0, AtomType.ALKALI_METAL, 1, 4, Color.VIOLET),
    BERYLLIUM(4, "Be", 1.5, AtomType.ALKALINE_EARTH_METAL, 2, 5, Color.DARK_GREEN),
    BORON(5, "B", 2.0, AtomType.NON_METAL, 3, 6, Color.BEIGE),
    CARBON(6, "C", 2.5, AtomType.NON_METAL, 4, 6, Atom.Color.BLACK),
    NITROGEN(7, "N", 3.0, AtomType.NON_METAL, 5, 7, Color.BLUE),
    OXYGEN(8, "O", 3.5, AtomType.NON_METAL, 6, 8, Color.RED),
    FLUORINE(9, "F", 4.0, AtomType.NON_METAL, 7, 10, Color.GREEN),
    NEON(10, "Ne", 0, AtomType.NOBLE_GAS, 8, 10, Atom.Color.CYAN),

    SODIUM(11, "Na", 0.9, AtomType.ALKALI_METAL, 1, 12, Color.VIOLET),
    MAGNESIUM(12, "Mg", 1.2, AtomType.ALKALINE_EARTH_METAL, 2, 12, Color.DARK_GREEN),
    ALUMINIUM(13, "Al", 1.5, AtomType.POST_TRANSITION_METAL, 3, 14, Color.GRAY),
    SILICON(14, "Si", 1.8, AtomType.NON_METAL, 4, 14, Color.BEIGE),
    PHOSPHORUS(15, "P", 2.1, AtomType.NON_METAL, 5, 16, Color.ORANGE),
    SULFUR(16, "S", 2.5, AtomType.NON_METAL, 6, 16, Color.YELLOW),
    CHLORINE(17, "Cl", 3.0, AtomType.NON_METAL, 7, 18, Color.GREEN),
    ARGON(18, "Ar", 0, AtomType.NOBLE_GAS, 8, 22, Atom.Color.CYAN),

    //valence electrons of transition metals may not be correct
    POTASSIUM(19, "K", 0.8, AtomType.ALKALI_METAL, 1, 20, Color.VIOLET),
    CALCIUM(20, "Ca", 1.0, AtomType.ALKALINE_EARTH_METAL, 2, 20, Color.DARK_GREEN),
    TITANIUM(22, "Ti", 1.54, AtomType.TRANSITION_METAL, 2, 26, Color.GRAY),
    MANGANESE(25, "Mn", 1.55, AtomType.TRANSITION_METAL, 2, 30, Color.GRAY),
    IRON(26, "Fe", 1.8, AtomType.TRANSITION_METAL, 2, 30, Color.GRAY),
    COPPER(29, "Cu", 1.9, AtomType.TRANSITION_METAL, 1, 34, Color.ORANGE),
    ZINC(30, "Zn", 1.6, AtomType.TRANSITION_METAL, 2, 34, Color.GRAY),
    BROMINE(35, "Br", 2.8, AtomType.NON_METAL, 7, 44, Color.GREEN),
    STRONTIUM(38, "Sr", 0.59, AtomType.ALKALINE_EARTH_METAL, 2, 50, Color.DARK_GREEN),

    SILVER(47, "Ag", 1.9, AtomType.TRANSITION_METAL, 1, 60, Color.GRAY),
    CADMIUM(48, "Cd", 1.7, AtomType.TRANSITION_METAL, 2, 66, Color.BEIGE),
    TIN(50, "Sn", 1.8, AtomType.POST_TRANSITION_METAL, 4, 70, Color.GRAY),
    IODINE(53, "I", 2.5, AtomType.NON_METAL, 7, 74, Color.GREEN),

    TUNGSTEN(74, "W", 2.36, AtomType.TRANSITION_METAL, 2, 110, Atom.Color.CYAN),
    GOLD(79, "Au", 2.4, AtomType.TRANSITION_METAL, 1, 118, Color.YELLOW),
    MERCURY(80, "Hg", 2, AtomType.TRANSITION_METAL, 2, 122, Color.GRAY),
    LEAD(82, "Pb", 1.8, AtomType.POST_TRANSITION_METAL, 4, 126, Color.GRAY),
    URANIUM(92, "U", 1.7, AtomType.ACTINOID, 2, 146, Color.PINK);

    private static class Color{
        public static final int BLACK = 0x121215;
        public static final int CYAN = 0x0091D4;
        public static final int VIOLET = 0x5700A3;
        public static final int DARK_GREEN = 0x113900;
        public static final int BEIGE = 0x7B6B46;
        public static final int BLUE = 0x00079D;
        public static final int RED = 0x910000;
        public static final int GREEN = 0x518141;
        public static final int GRAY = 0x4A4A50;
        public static final int ORANGE = 0x8E5A0D;
        public static final int YELLOW = 0xADA10F;
        public static final int PINK = 0x9B4D7A;
    }

    public static final int LARGEST_ATOM_NUMBER = 118;
    private static final Atom[] ATOM_BY_PROTONS = createAtomProtonIndex();
    private static final Map<String, Atom> ATOM_BY_SYMBOL = createAtomSymbolIndex();

    // set in constructor
    private final int atomNumber;
    private final String symbol;
    private final double electronegativity;
    private final AtomType type;
    private final int initialValenceElectrons;
    private final int initialNeutrons;
    private final int color;

    // set when item is created
    private Item item;

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

    Atom(int p, String symbol, double en, AtomType type, int ve, int n, int color) {
        this.atomNumber = p;
        this.symbol = symbol;
        this.electronegativity = en;
        this.type = type;
        this.initialValenceElectrons = ve;
        this.initialNeutrons = n;
        this.color = color;
    }

    private static Atom[] createAtomProtonIndex(){
        Atom[] array = new Atom[LARGEST_ATOM_NUMBER];
        for (Atom atom : values())
            array[atom.getAtomNumber()] = atom;
        return array;
    }

    private static Map<String, Atom> createAtomSymbolIndex(){
        return Arrays.stream(values()).collect(Collectors.toMap(Atom::getSymbol, a -> a));
    }

    @Nullable
    public static Atom getBySymbol(String symbol) {
        return ATOM_BY_SYMBOL.getOrDefault(symbol, null);
    }

    public static Atom getByNumber(int atomNumber) {
        return ATOM_BY_PROTONS[atomNumber];
    }

    public int getAtomNumber() {
        return atomNumber;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName(){return Text.translatable(getItem().getTranslationKey()).getString();}

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

    public Identifier getItemId() {
        return new Identifier(Minelabs.MOD_ID, name().toLowerCase() + "_atom");
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getMaxPossibleBonds(){
        if (type == AtomType.NOBLE_GAS)
            return 0;
        else
            return Math.min(initialValenceElectrons, 8 - initialValenceElectrons);
    }

    @Override
    public String toString() {
        return symbol;
    }

    public int getColor() {
        return this.color;
    }

}
