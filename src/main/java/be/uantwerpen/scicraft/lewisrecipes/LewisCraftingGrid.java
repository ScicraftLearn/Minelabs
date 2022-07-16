package be.uantwerpen.scicraft.lewisrecipes;

import net.minecraft.inventory.SimpleInventory;

public class LewisCraftingGrid extends SimpleInventory {

    // TODO: might need to link stacks to vertices for this representation -> change graph
    private PartialMolecule currentMolecule = null;

    @Override
    public void markDirty() {
        super.markDirty();
        currentMolecule = buildPartialMoleculeFromInventory();
    }

    public PartialMolecule getPartialMolecule(){
        return currentMolecule;
    }

    private PartialMolecule buildPartialMoleculeFromInventory(){
        // TODO: implement
        return null;
    }

}
