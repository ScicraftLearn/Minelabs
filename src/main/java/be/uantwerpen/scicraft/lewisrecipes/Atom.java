package be.uantwerpen.scicraft.lewisrecipes;

import org.jetbrains.annotations.Nullable;

public enum Atom {
    HYDROGEN("H"),
    HELIUM("He"),

    LITHIUM("Li"),
    BERYLLIUM("Be"),
    BORON("B"),
    CARBON("C"),
    NITROGEN("N"),
    OXYGEN("O"),
    FLUORINE("F"),
    NEON("Ne"),

    SODIUM("Na"),
    MAGNESIUM("Mg"),
    ALUMINIUM("Al"),
    SILICON("Si"),
    PHOSPHORUS("P"),
    SULFUR("S"),
    CHLORINE("Cl"),
    ARGON("Ar");

    private String symbol;

    Atom(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Nullable
    public static Atom getBySymbol(String symbol) {
        for (Atom value : values())
            if (value.getSymbol().equals(symbol)) return value;
        return null;
    }
}
