package be.uantwerpen.minelabs.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class AtomConfiguration {
    private final int protons;
    private final int neutrons;
    private final int electrons;

    private final NucleusState nucleusState;

    public AtomConfiguration(int protons, int neutrons, int electrons){
        this.protons = protons;
        this.neutrons = neutrons;
        this.electrons = electrons;

        nucleusState = NuclidesTable.getNuclide(protons, neutrons);
    }

    @Nullable
    public Item getAtomItem() {
        if (protons == 0 || protons != electrons)
            return null;

        if (nucleusState == null || !nucleusState.isStable())
            return null;

        return nucleusState.getAtomItem();
    }

    public ItemStack getAtomStack(){
        // it's ok if this is null. The ItemStack will be the empty stack.
        Item atomItem = getAtomItem();
        return new ItemStack(atomItem, 1);
    }

    public int getProtons() {
        return protons;
    }

    public int getNeutrons() {
        return neutrons;
    }

    public int getElectrons() {
        return electrons;
    }

    // TODO: remove and wrap functionality
    public NucleusState getNucleusState(){
        return nucleusState;
    }


}
