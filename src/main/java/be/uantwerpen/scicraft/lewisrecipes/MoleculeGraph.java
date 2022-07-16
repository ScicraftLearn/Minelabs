package be.uantwerpen.scicraft.lewisrecipes;


import be.uantwerpen.scicraft.util.Graph;


public class MoleculeGraph extends Graph<Atom, Bond> {


    @Override
    public boolean isIsomorphicTo(Graph<Atom, Bond> other) {
        if (getVertices().size() != other.getVertices().size())
            return false;
        if (getEdges().size() != other.getEdges().size())
            return false;

        // for the simple graphs we deal with, comparing the canonical string representation should suffice.
        return toCanonical().equals(other.toCanonical());
    }
}
