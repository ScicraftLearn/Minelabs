package be.uantwerpen.scicraft.lewisrecipes;


import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.util.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MoleculeGraph extends Graph<Atom, Bond> {


    /**
     * Computes the amount of remaining bonds an atom can make based on its already made bonds.
     */
    public int getOpenConnections(Vertex v){
        int usableElectrons = Math.min(v.data.getInitialValenceElectrons(), 8 - v.data.getInitialValenceElectrons());
        return usableElectrons - v.getEdgesData().stream().map(bond -> bond.bondOrder).reduce(0, Integer::sum);
    }

    public void incrementBond(Vertex v1, Vertex v2){
        if (v1 == null || v2 == null) return;
        Edge edge = getEdge(v1, v2);
        if (edge == null)
            throw new IllegalArgumentException("Can only increment bonds between neighbouring atoms");
        edge.data = edge.data.higher();
    }

    public void removeZeroBonds(){
        for(Edge edge: new ArrayList<>(getEdges())){
            if (edge.data == Bond.COVALENT_ZERO){
                removeEdge(edge);
            }
        }
    }


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
