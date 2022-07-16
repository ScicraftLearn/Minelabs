package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.util.Graph;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MoleculeItemGraph extends MoleculeGraph {

    private final Map<Vertex, ItemStack> vertexToItemStack = new HashMap<>();
    private final Map<ItemStack, Vertex> itemStackToVertex = new HashMap<>();


    public Graph<Atom, Bond>.Vertex addVertex(Atom data, ItemStack link) {
        Vertex vertex = super.addVertex(data);
        vertexToItemStack.put(vertex, link);
        itemStackToVertex.put(link, vertex);
        return vertex;
    }

    public MoleculeItemGraph.Vertex getVertexOfItemStack(ItemStack stack){
        return itemStackToVertex.get(stack);
    }

}
