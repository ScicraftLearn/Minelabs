package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.item.AtomItem;
import be.uantwerpen.scicraft.util.Graph;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.stream.Stream;

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

    private Collection<Integer> getNeighbourPositions(int x, int y) {
        List<Integer> slots = new ArrayList<>();
        if (x > 0) slots.add(toSlot(x - 1, y));
        if (x < WIDTH - 1) slots.add(toSlot(x + 1, y));
        if (y > 0) slots.add(toSlot(x, y - 1));
        if (y < HEIGHT - 1) slots.add(toSlot(x, y + 1));
        return slots;
    }

    /**
     * Creates a topological graph based on the crafting grid. Intended to simplify traversal.
     */
    private MoleculeItemGraph getPositionGraph() {
        MoleculeItemGraph graph = new MoleculeItemGraph();

        // First build vertices
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                ItemStack stack = getStack(toSlot(x, y));
                // We assume only AtomItems make it into the LCT.
                if (stack.isEmpty()) continue;
                Atom atom = ((AtomItem) stack.getItem()).getAtom();
                graph.addVertex(atom, stack);
            }
        }

        // Then connect them
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                ItemStack stack = getStack(toSlot(x, y));
                if (stack.isEmpty()) continue;
                MoleculeItemGraph.Vertex from = graph.getVertexOfItemStack(stack);
                for (int slot : getNeighbourPositions(x, y)) {
                    ItemStack neighbourStack = getStack(slot);
                    if (neighbourStack.isEmpty()) continue;
                    MoleculeItemGraph.Vertex to = graph.getVertexOfItemStack(neighbourStack);
                    try {
                        graph.addEdge(from, to, Bond.COVALENT_ZERO);
                    } catch (IllegalArgumentException e) {
                        // edges are added twice due to iteration -> just ignore.
                    }
                }
            }
        }

        return graph;
    }

    private PartialMolecule buildPartialMoleculeFromInventory() {
        MoleculeItemGraph positionGraph = getPositionGraph();

        // debug
        Scicraft.LOGGER.info("Position graph\n" + positionGraph.toString());

        // We use MoleculeItemGraph which can keep a mapping between vertices and ItemStacks.
        MoleculeItemGraph structure = positionGraphToMoleculeItemGraph(positionGraph);
        Scicraft.LOGGER.info("Molecule formed: " + structure.toString());
        return new PartialMolecule(structure);
    }

    /**
     * Make the actual bonds between elements.
     */
    private MoleculeItemGraph positionGraphToMoleculeItemGraph(MoleculeItemGraph structure) {
        List<Atom> sourceOrder = Stream.of("H", "F", "Cl", "Br", "I", "O", "N", "C", "B", "S", "P", "Si", "Al", "Sn", "Pb").map(Atom::getBySymbol).toList();
        List<Atom> targetOrder = Stream.of("O", "N", "C", "B", "S", "P", "Si", "Al", "Sn", "Pb", "I", "Br", "Cl", "F", "H").map(Atom::getBySymbol).toList();

        for (Atom sourceAtom : sourceOrder) {
            boolean changed = true;
            while (changed) {
                changed = false;
                for (MoleculeItemGraph.Vertex source : structure.getVertices()) {
                    if (source.data != sourceAtom) continue;
                    if (structure.getOpenConnections(source) < 1) continue;
                    MoleculeItemGraph.Vertex target = source.getNeighbours().stream()
                            .filter(t -> structure.getOpenConnections(t) > 0)
                            .min(Comparator.comparingInt(t -> targetOrder.indexOf(((AtomItem) t.data.getItem()).getAtom())))
                            .orElse(null);
                    if (target == null) continue;
                    changed = changed || structure.incrementBond(source, target);
                }
            }
        }

        structure.removeZeroBonds();
        return structure;
    }

}
