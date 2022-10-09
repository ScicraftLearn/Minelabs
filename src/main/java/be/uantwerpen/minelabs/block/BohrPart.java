package be.uantwerpen.minelabs.block;



import net.minecraft.util.StringIdentifiable;

public enum BohrPart implements StringIdentifiable {
    BASE("base"),
    BACK("back"),
    RIGHT("right"),
    CORNER("corner");

    private final String name;

    private BohrPart(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}

