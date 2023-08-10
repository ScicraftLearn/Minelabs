package be.minelabs.item.items;

import be.minelabs.item.IMoleculeItem;
import net.minecraft.item.Item;

public class MoleculeItem extends Item implements IMoleculeItem {
    private final String molecule;

    public MoleculeItem(Settings settings, String molecule) {
        super(settings);
        this.molecule = molecule;
    }

    @Override
    public String getMolecule() {
        return molecule;
    }
}
