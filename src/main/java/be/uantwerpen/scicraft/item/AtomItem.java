package be.uantwerpen.scicraft.item;

import net.minecraft.item.Item;

public class AtomItem extends Item {

    private final int atomicNumber;
    private final String symbol;

    public AtomItem(Item.Settings settings, int atomicNumber, String symbol) {
        super(settings);
        this.atomicNumber = atomicNumber;
        this.symbol = symbol;
    }

    public int getAtomicNumber(){
        return atomicNumber;
    }

    public String getSymbol() {
        return symbol;
    }
}
