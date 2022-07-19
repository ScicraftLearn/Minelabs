package be.uantwerpen.scicraft.lewisrecipes;

import java.util.Collection;
import java.util.Objects;

public class PartialMolecule {

    private final MoleculeGraph structure;

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
