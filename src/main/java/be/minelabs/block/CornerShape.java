package be.minelabs.block;

import net.minecraft.util.StringIdentifiable;

public enum CornerShape implements StringIdentifiable {

    STRAIGHT("straight"),
    LEFT("left"),
    RIGHT("right");

    private final String name;

    private CornerShape(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }
}
