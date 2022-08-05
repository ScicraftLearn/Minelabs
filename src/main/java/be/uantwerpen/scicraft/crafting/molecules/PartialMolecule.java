package be.uantwerpen.scicraft.crafting.molecules;

import java.util.Collection;

public class PartialMolecule {

    private final MoleculeGraph structure;

    public PartialMolecule(){
        this(new MoleculeGraph());
    }

    public PartialMolecule(MoleculeGraph structure){
        this.structure = structure;
    }


    public MoleculeGraph getStructure(){
        return structure;
    }

    public Collection<Atom> getIngredients() {
        return structure.getVertexData();
    }

}
