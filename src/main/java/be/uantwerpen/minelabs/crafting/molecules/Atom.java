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
    HYDROGEN(1, "H", 2.1, AtomType.NON_METAL, 1, 0, Color.WHITE),
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
    POTASSIUM(19, "K", 0.8, AtomType.ALKALI_METAL, 1, 20, Color.VIOLET),
    CALCIUM(20, "Ca", 1.0, AtomType.ALKALINE_EARTH_METAL, 2, 20, Color.DARK_GREEN),
    SCANDIUM(21, "Sc", 1.36, AtomType.TRANSITION_METAL, 2, 24, Color.GRAY),
    TITANIUM(22, "Ti", 1.54, AtomType.TRANSITION_METAL, 2, 26, Color.GRAY),
    VANADIUM(23, "V", 1.63, AtomType.TRANSITION_METAL, 2, 28, Color.GRAY),
    CHROMIUM(24, "Cr", 1.66, AtomType.TRANSITION_METAL, 2, 28, Color.GRAY),
    MANGANESE(25, "Mn", 1.55, AtomType.TRANSITION_METAL, 2, 30, Color.GRAY),
    IRON(26, "Fe", 1.8, AtomType.TRANSITION_METAL, 2, 30, Color.GRAY),
    COBALT(27, "Co", 1.88, AtomType.TRANSITION_METAL, 2, 32, Color.GRAY),
    NICKEL(28, "Ni", 1.91, AtomType.TRANSITION_METAL, 2, 31, Color.GRAY),
    COPPER(29, "Cu", 1.9, AtomType.TRANSITION_METAL, 1, 34, Color.ORANGE),
    ZINC(30, "Zn", 1.6, AtomType.TRANSITION_METAL, 2, 34, Color.GRAY),
    GALLIUM(31, "Ga", 1.81, AtomType.POST_TRANSITION_METAL, 3, 39, Color.GRAY),
    GERMANIUM(32, "Ge", 2.01, AtomType.POST_TRANSITION_METAL, 4, 41, Color.GRAY),
    ARSENIC(33, "As", 2.18, AtomType.METALLOID, 5, 42, Color.GRAY),
    SELENIUM(34, "Se", 2.55, AtomType.NON_METAL, 6, 45, Color.GRAY),
    BROMINE(35, "Br", 2.8, AtomType.NON_METAL, 7, 44, Color.GREEN),
    KRYPTON(36, "Kr", 0, AtomType.NOBLE_GAS, 8, 48, Atom.Color.CYAN),
    RUBIDIUM(37, "Rb", 0.82, AtomType.ALKALI_METAL, 1, 48, Color.VIOLET),
    STRONTIUM(38, "Sr", 0.59, AtomType.ALKALINE_EARTH_METAL, 2, 50, Color.DARK_GREEN),
    YTTRIUM(39, "Y", 1.22, AtomType.TRANSITION_METAL, 2, 50, Color.GRAY),
    ZIRCONIUM(40, "Zr", 1.33, AtomType.TRANSITION_METAL, 2, 51, Color.GRAY),
    NIOBIUM(41, "Nb", 1.6, AtomType.TRANSITION_METAL, 1, 52, Color.GRAY),
    MOLYBDENUM(42, "Mo", 2.16, AtomType.TRANSITION_METAL, 1, 54, Color.GRAY),
    TECHNETIUM(43, "Tc", 1.9, AtomType.TRANSITION_METAL, 2, 55, Color.GRAY),
    RUTHENIUM(44, "Ru", 2.2, AtomType.TRANSITION_METAL, 2, 57, Color.GRAY),
    RHODIUM(45, "Rh", 2.28, AtomType.TRANSITION_METAL, 1, 58, Color.GRAY),
    PALLADIUM(46, "Pd", 2.2, AtomType.TRANSITION_METAL, 2, 60, Color.GRAY),
    SILVER(47, "Ag", 1.9, AtomType.TRANSITION_METAL, 1, 60, Color.GRAY),
    CADMIUM(48, "Cd", 1.7, AtomType.TRANSITION_METAL, 2, 66, Color.BEIGE),
    INDIUM(49, "In", 1.78, AtomType.POST_TRANSITION_METAL, 3, 66, Color.GRAY),
    TIN(50, "Sn", 1.8, AtomType.POST_TRANSITION_METAL, 4, 70, Color.GRAY),
    ANTIMONY(51, "Sb", 2.05, AtomType.METALLOID, 5, 74, Color.GRAY),
    TELLURIUM(52, "Te", 2.1, AtomType.METALLOID, 6, 75, Color.GRAY),
    IODINE(53, "I", 2.5, AtomType.NON_METAL, 7, 74, Color.GREEN),
    XENON(54, "Xe", 0, AtomType.NOBLE_GAS, 8, 77, Atom.Color.CYAN),
    CESIUM(55, "Cs", 0.79, AtomType.ALKALI_METAL, 1, 78, Color.VIOLET),
    BARIUM(56, "Ba", 0.89, AtomType.ALKALINE_EARTH_METAL, 2, 81, Color.DARK_GREEN),
    LANTHANUM(57, "La", 1.1, AtomType.LANTHANOID, 3, 81, Color.GRAY),
    CERIUM(58, "Ce", 1.12, AtomType.LANTHANOID, 3, 82, Color.GRAY),
    PRASEODYMIUM(59, "Pr", 1.13, AtomType.LANTHANOID, 3, 82, Color.GRAY),
    NEODYMIUM(60, "Nd", 1.14, AtomType.LANTHANOID, 3, 84, Color.GRAY),
    PROMETHIUM(61, "Pm", 1.13, AtomType.LANTHANOID, 3, 84, Color.GRAY),
    SAMARIUM(62, "Sm", 1.17, AtomType.LANTHANOID, 3, 88, Color.GRAY),
    EUROPIUM(63, "Eu", 1.2, AtomType.LANTHANOID, 3, 89, Color.GRAY),
    GADOLINIUM(64, "Gd", 1.2, AtomType.LANTHANOID, 3, 93, Color.GRAY),
    TERBIUM(65, "Tb", 1.2, AtomType.LANTHANOID, 3, 94, Color.GRAY),
    DYSPROSIUM(66, "Dy", 1.22, AtomType.LANTHANOID, 3, 97, Color.GRAY),
    HOLMIUM(67, "Ho", 1.23, AtomType.LANTHANOID, 3, 98, Color.GRAY),
    ERBIUM(68, "Er", 1.24, AtomType.LANTHANOID, 3, 99, Color.GRAY),
    THULIUM(69, "Tm", 1.25, AtomType.LANTHANOID, 3, 100, Color.GRAY),
    YTTERBIUM(70, "Yb", 1.1, AtomType.LANTHANOID, 3, 103, Color.GRAY),
    LUTETIUM(71, "Lu", 1.27, AtomType.LANTHANOID, 3, 104, Color.GRAY),
    HAFNIUM(72, "Hf", 1.3, AtomType.TRANSITION_METAL, 2, 106, Color.GRAY),
    TANTALUM(73, "Ta", 1.5, AtomType.TRANSITION_METAL, 2, 108, Color.GRAY),
    TUNGSTEN(74, "W", 2.36, AtomType.TRANSITION_METAL, 2, 110, Atom.Color.CYAN),
    RHENIUM(75, "Re", 1.9, AtomType.TRANSITION_METAL, 2, 111, Color.GRAY),
    OSMIUM(76, "Os", 2.2, AtomType.TRANSITION_METAL, 2, 114, Color.GRAY),
    IRIDIUM(77, "Ir", 2.2, AtomType.TRANSITION_METAL, 2, 115, Color.GRAY),
    PLATINUM(78, "Pt", 2.28, AtomType.TRANSITION_METAL, 2, 117, Color.GRAY),
    GOLD(79, "Au", 2.4, AtomType.TRANSITION_METAL, 1, 118, Color.YELLOW),
    MERCURY(80, "Hg", 2.0, AtomType.TRANSITION_METAL, 2, 121, Color.GRAY),
    THALLIUM(81, "Tl", 1.62, AtomType.POST_TRANSITION_METAL, 3, 123, Color.GRAY),
    LEAD(82, "Pb", 1.8, AtomType.POST_TRANSITION_METAL, 4, 126, Color.GRAY),
    BISMUTH(83, "Bi", 1.9, AtomType.POST_TRANSITION_METAL, 5, 126, Color.GRAY),
    POLONIUM(84, "Po", 2.0, AtomType.POST_TRANSITION_METAL, 6, 125, Color.GRAY),
    ASTATINE(85, "At", 2.2, AtomType.METALLOID, 7, 125, Color.GRAY),
    RADON(86, "Rn", 0, AtomType.NOBLE_GAS, 8, 136, Atom.Color.CYAN),
    FRANCIUM(87, "Fr", 0.7, AtomType.ALKALI_METAL, 1, 136, Color.VIOLET),
    RADIUM(88, "Ra", 0.9, AtomType.ALKALINE_EARTH_METAL, 2, 138, Color.DARK_GREEN),
    ACTINIUM(89, "Ac", 1.1, AtomType.ACTINOID, 3, 138, Color.GRAY),
    THORIUM(90, "Th", 1.3, AtomType.ACTINOID, 4, 142, Color.GRAY),
    PROTACTINIUM(91, "Pa", 1.5, AtomType.ACTINOID, 5, 140, Color.GRAY),
    URANIUM(92, "U", 1.7, AtomType.ACTINOID, 6, 146, Color.PINK),
    NEPTUNIUM(93, "Np", 1.36, AtomType.ACTINOID, 7, 144, Color.GRAY),
    PLUTONIUM(94, "Pu", 1.28, AtomType.ACTINOID, 8, 150, Color.GRAY),
    AMERICIUM(95, "Am", 1.3, AtomType.ACTINOID, 8, 148, Color.GRAY),
    CURIUM(96, "Cm", 0, AtomType.ACTINOID, 8, 151, Color.GRAY),
    BERKELIUM(97, "Bk", 0, AtomType.ACTINOID, 8, 150, Color.GRAY),
    CALIFORNIUM(98, "Cf", 0, AtomType.ACTINOID, 8, 153, Color.GRAY),
    EINSTEINIUM(99, "Es", 0, AtomType.ACTINOID, 8, 153, Color.GRAY),
    FERMIUM(100, "Fm", 0, AtomType.ACTINOID, 8, 157, Color.GRAY),
    MENDELEVIUM(101, "Md", 0, AtomType.ACTINOID, 8, 157, Color.GRAY),
    NOBELIUM(102, "No", 0, AtomType.ACTINOID, 8, 157, Color.GRAY),
    LAWRENCIUM(103, "Lr", 0, AtomType.ACTINOID, 8, 159, Color.GRAY),
    RUTHERFORDIUM(104, "Rf", 0, AtomType.TRANSITION_METAL, 4, 267, Color.GRAY),
    DUBNIUM(105, "Db", 0, AtomType.TRANSITION_METAL, 5, 270, Color.GRAY),
    SEABORGIUM(106, "Sg", 0, AtomType.TRANSITION_METAL, 6, 271, Color.GRAY),
    BOHRIUM(107, "Bh", 0, AtomType.TRANSITION_METAL, 7, 270, Color.GRAY),
    HASSIUM(108, "Hs", 0, AtomType.TRANSITION_METAL, 8, 277, Color.GRAY),
    MEITNERIUM(109, "Mt", 0, AtomType.UNKNOWN, 0, 278, Color.GRAY),
    DARMSTADTIUM(110, "Ds", 0, AtomType.UNKNOWN, 0, 281, Color.GRAY),
    ROENTGENIUM(111, "Rg", 0, AtomType.UNKNOWN, 0, 282, Color.GRAY),
    COPERNICIUM(112, "Cn", 0, AtomType.UNKNOWN, 0, 285, Color.GRAY),
    NIHONIUM(113, "Nh", 0, AtomType.UNKNOWN, 0, 286, Color.GRAY),
    FLEROVIUM(114, "Fl", 0, AtomType.POST_TRANSITION_METAL, 0, 289, Color.GRAY),
    MOSCOVIUM(115, "Mc", 0, AtomType.UNKNOWN, 0, 288, Color.GRAY),
    LIVERMORIUM(116, "Lv", 0, AtomType.UNKNOWN, 0, 293, Color.GRAY),
    TENNESSINE(117, "Ts", 0, AtomType.METALLOID, 0, 294, Color.GRAY),
    OGANESSON(118, "Og", 0, AtomType.NOBLE_GAS, 0, 294, Color.GRAY);

    private static class Color{
        public static final int WHITE = 0xFFFFFF;
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
        UNKNOWN, METALLOID, NOBLE_GAS
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
        Atom[] array = new Atom[OGANESSON.getAtomNumber()+1];
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

    @Nullable
    public static Atom getByNumber(int atomNumber) {
        if (atomNumber < 1 || atomNumber >= ATOM_BY_PROTONS.length)
            return null;
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
