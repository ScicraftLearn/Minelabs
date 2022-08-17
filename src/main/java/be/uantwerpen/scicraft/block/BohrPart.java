package be.uantwerpen.scicraft.block;



import net.minecraft.util.StringIdentifiable;

public enum BohrPart implements StringIdentifiable
{
    BASE("base"),

    SOUTH("south"),
    EAST("east"),
    SOUTH_EAST("southeast");

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

