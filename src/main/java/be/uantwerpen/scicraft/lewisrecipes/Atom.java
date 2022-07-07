package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.item.Items;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

public enum Atom {
    HYDROGEN("H", Items.HYDROGEN_ATOM),
    HELIUM("He", Items.HELIUM_ATOM),

    LITHIUM("Li", Items.LITHIUM_ATOM),
    BERYLLIUM("Be", Items.BERYLLIUM_ATOM),
    BORON("B", Items.BORON_ATOM),
    CARBON("C", Items.CARBON_ATOM),
    NITROGEN("N", Items.NITROGEN_ATOM),
    OXYGEN("O", Items.OXYGEN_ATOM),
    FLUORINE("F", Items.FLUORINE_ATOM),
    NEON("Ne", Items.NEON_ATOM),

    SODIUM("Na", Items.SODIUM_ATOM),
    MAGNESIUM("Mg", Items.MAGNESIUM_ATOM),
    ALUMINIUM("Al", Items.ALUMINIUM_ATOM),
    SILICON("Si", Items.SILICON_ATOM),
    PHOSPHORUS("P", Items.PHOSPHORUS_ATOM),
    SULFUR("S", Items.SULFUR_ATOM),
    CHLORINE("Cl", Items.CHLORINE_ATOM),
    ARGON("Ar", Items.ARGON_ATOM);

    private final String symbol;
    private final Item item;

    Atom(String symbol, Item item) {
        this.symbol = symbol;
        this.item = item;
    }

    public String getSymbol() {
        return symbol;
    }

    public Item getItem() {
        return item;
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

}
