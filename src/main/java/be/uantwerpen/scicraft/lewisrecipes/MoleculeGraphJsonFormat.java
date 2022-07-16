package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.util.Graph;
import com.google.gson.JsonSyntaxException;

import java.util.*;

public class MoleculeGraphJsonFormat {

    List<AtomJson> atoms;

    static class AtomJson {
        String key;
        private String atom;

        public Atom getAtom() throws JsonSyntaxException {
            Atom atomObject = Atom.getBySymbol(atom);
            if (atomObject == null)
                throw new JsonSyntaxException("Invalid atom in json: " + atom);
            return atomObject;
        }
    }

    List<BondJson> bonds;

    static class BondJson {
        String from;
        String to;
        int bondOrder = 1;
    }

    public MoleculeGraph get() throws JsonSyntaxException{
        MoleculeGraph graph = new MoleculeGraph();
        Map<String, MoleculeGraph.Vertex> vertices = new HashMap<>();
        for (AtomJson atom : atoms) {
            if (vertices.containsKey(atom.key))
                throw new IllegalArgumentException("key: '" + atom.key + "' already present in graph.");
            MoleculeGraph.Vertex v = graph.addVertex(atom.getAtom());
            vertices.put(atom.key, v);
        }

        for (BondJson bond : bonds) {
            MoleculeGraph.Vertex v1 = vertices.get(bond.from);
            if (v1 == null)
                throw new JsonSyntaxException("Reference to unknown vertex in edge description '" + bond.from + "'");
            MoleculeGraph.Vertex v2 = vertices.get(bond.to);
            if (v2 == null)
                throw new JsonSyntaxException("Reference to unknown vertex in edge description '" + bond.to + "'");
            try{
                graph.addEdge(v1, v2, Bond.get(bond.bondOrder));
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("Edge between '" + v1 + "' and '" + v2 + "' already exists");
            }
        }

        return graph;
    }

}
