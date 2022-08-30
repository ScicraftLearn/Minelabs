package be.uantwerpen.minelabs.item;

import be.uantwerpen.minelabs.crafting.molecules.Atom;
import net.minecraft.item.Item;

public class AtomItem extends Item {

    private final Atom atom;

    public AtomItem(Item.Settings settings, Atom atom) {
        super(settings);
        this.atom = atom;
    }

    public Atom getAtom() {
        return atom;
    }
}
