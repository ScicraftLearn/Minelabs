package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.util.Graph;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MoleculeItemGraph extends MoleculeGraph {

    private Map<Vertex, ItemStack> vertexToItemStack = new HashMap<>();


    public Graph<Atom, Bond>.Vertex addVertex(Atom data, ItemStack link) {
        Vertex vertex = super.addVertex(data);
        vertexToItemStack.put(vertex, link);
        return vertex;
    }
}
