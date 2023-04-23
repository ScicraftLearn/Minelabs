package be.uantwerpen.minelabs.util;

import be.uantwerpen.minelabs.crafting.molecules.Atom;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AtomConfiguration {

    private static final int MAX_ELECTRONS_ABOVE_PROTONS = 8;

    private final int protons;
    private final int neutrons;
    private final int electrons;

    private final NucleusState nucleusState;
    // the atom based on protons
    @Nullable
    private final Atom atom;

    public AtomConfiguration(int protons, int neutrons, int electrons) {
        this.protons = protons;
        this.neutrons = neutrons;
        this.electrons = electrons;

        // TODO: make it so nucleusState cannot be null
        nucleusState = NuclidesTable.getNuclide(protons, neutrons);
        atom = Atom.getByNumber(protons);
    }

    @Nullable
    public Item getAtomItem() {
        if (atom == null) return null;
        return atom.getItem();
    }

    public ItemStack getAtomStack() {
        // it's ok if this is null. The ItemStack will be the empty stack.
        Item atomItem = getAtomItem();
        return new ItemStack(atomItem, 1);
    }

    public Optional<String> getSymbol(){
        if (atom != null) return Optional.of(atom.getSymbol());
        if (nucleusState != null) return Optional.of(nucleusState.getSymbol());
        return Optional.empty();
    }

    public Optional<String> getName(){
        if (nucleusState == null) return Optional.empty();
        return Optional.of(nucleusState.getAtomName());
    }

    public boolean isStable(){
        return isNucleusStable() && isElectronStable();
    }

    public boolean isNucleusStable() {
        if (nucleusState == null) return false;
        return nucleusState.isStable();
    }

    public boolean isNucleusDecomposing() {
        if (nucleusState == null) return true;
        // TODO: refactor
        return nucleusState.getUnstability() == 0.04f;
    }

    public float getNucleusInstability(){
        if (isNucleusStable()) return 0f;
        if (isNucleusDecomposing()) return 1f;

        // TODO: refactor
        return nucleusState.getUnstability() / 0.04f;
    }

    public boolean isElectronStable() {
        return electrons == protons;
    }

    public boolean isElectronDecomposing(){
        return electrons > protons + MAX_ELECTRONS_ABOVE_PROTONS;
    }

    public float getElectronInstability(){
        float delta = (float) (electrons - protons) / (float) MAX_ELECTRONS_ABOVE_PROTONS;
        return MathHelper.clampedLerp(0f, 1f, delta);
    }

    public @Nullable Atom getAtom(){
        return atom;
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


}
