package be.uantwerpen.scicraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class AtomItem extends Item {

    private final int atomicNumber;
    private final String symbol;
    private final double defaultElectronegativity;

    public AtomItem(Item.Settings settings, int atomicNumber, String symbol, double defaultElectronegativity) {
        super(settings);
        this.atomicNumber = atomicNumber;
        this.symbol = symbol;
        this.defaultElectronegativity = defaultElectronegativity;
    }

    public int getAtomicNumber(){
        return atomicNumber;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getDefaultElectronegativity() {
        return defaultElectronegativity;
    }

    public double getElectronegativity(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().contains("EN", 6 /* check for type double */) ? stack.getNbt().getDouble("EN") : defaultElectronegativity;
    }

    public void setElectronegativity(ItemStack stack, double electronegativity) {
        NbtCompound nbtData = stack.hasNbt() ? stack.getNbt() : new NbtCompound();
        nbtData.putDouble("EN", electronegativity);
        stack.setNbt(nbtData);
    }
}
