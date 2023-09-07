package be.minelabs.recipe.molecules;

import net.minecraft.item.Item;

public class Molecule extends PartialMolecule {
    private final Item item;

    private final Integer count;

    public Molecule(MoleculeGraph structure, Item item, Integer count) {
        super(structure);
        this.item = item;
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public Integer getCount() {
        return count;
    }
}
