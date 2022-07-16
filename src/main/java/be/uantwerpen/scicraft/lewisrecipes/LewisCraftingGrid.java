package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.util.Graph;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

import java.util.*;

public class LewisCraftingGrid extends SimpleInventory {

    private PartialMolecule currentMolecule = null;

    private static final int WIDTH = 5;
    private static final int HEIGHT = 5;

    public LewisCraftingGrid() {
        super(WIDTH * HEIGHT);
    }

    public LewisCraftingGrid(ItemStack... items) {
        super(items);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        currentMolecule = buildPartialMoleculeFromInventory();
    }

    public PartialMolecule getPartialMolecule() {
        return currentMolecule;
    }

    public int toSlot(int x, int y) {
        assert x >= 0 && x < WIDTH;
        assert y >= 0 && y < HEIGHT;
        return y * WIDTH + x;
    }

    private Collection<Integer> getNeighbourPositions(int x, int y){
        List<Integer> slots = new ArrayList<>();
        if (x > 0)slots.add(toSlot(x - 1, y));
        if (x < WIDTH - 1)slots.add(toSlot(x + 1, y));
        if (y > 0)slots.add(toSlot(x, y - 1));
        if (y < HEIGHT - 1)slots.add(toSlot(x, y + 1));
        return slots;
    }

    /**
     * Creates a topological graph based on the crafting grid. Intended to simplify traversal.
     */
    private Graph<ItemStack, String> getPositionGraph() {
        Graph<ItemStack, String> graph = new Graph<>();

        // First build vertices and keep mapping
        Map<ItemStack, Graph<ItemStack, String>.Vertex> stackMapping = new HashMap<>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                ItemStack stack = getStack(toSlot(x, y));
                if (stack.isEmpty()) continue;
                stackMapping.put(stack, graph.addVertex(stack));
            }
        }

        // Then connect them
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                ItemStack stack = getStack(toSlot(x, y));
                if (stack.isEmpty()) continue;
                if (!stackMapping.containsKey(stack)) continue;
                Graph<ItemStack, String>.Vertex from = stackMapping.get(stack);
                for (int slot:getNeighbourPositions(x, y)){
                    ItemStack neighbourStack = getStack(slot);
                    if (neighbourStack.isEmpty()) continue;
                    Graph<ItemStack, String>.Vertex to = stackMapping.get(neighbourStack);
                    try{
                        graph.addEdge(from, to, "");
                    }catch(IllegalArgumentException e){
                        // edges are added twice due to iteration -> just ignore.
                    }
                }
            }
        }

        return graph;
    }

    private PartialMolecule buildPartialMoleculeFromInventory() {
        Graph<ItemStack, String> positionGraph = getPositionGraph();

        // debug
        Scicraft.LOGGER.info("Position graph\n" + positionGraph.toString());

        // We use MoleculeItemGraph which can keep a mapping between vertices and ItemStacks.
        MoleculeItemGraph structure = positionGraphToMoleculeItemGraph(positionGraph);
        return new PartialMolecule(structure);
    }

    private MoleculeItemGraph positionGraphToMoleculeItemGraph(Graph<ItemStack, String> positionGraph) {
        MoleculeItemGraph structure = new MoleculeItemGraph();
        // TODO: implement

        return structure;
    }

}
