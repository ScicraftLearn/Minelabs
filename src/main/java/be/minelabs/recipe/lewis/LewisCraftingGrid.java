package be.minelabs.recipe.lewis;

import be.minelabs.science.Atom;
import be.minelabs.recipe.molecules.Bond;
import be.minelabs.recipe.molecules.MoleculeItemGraph;
import be.minelabs.recipe.molecules.PartialMolecule;
import be.minelabs.inventory.OrderedInventory;
import be.minelabs.item.items.AtomItem;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class LewisCraftingGrid extends OrderedInventory {

    private PartialMolecule currentMolecule = new PartialMolecule();

    private int width = 5;
    private int height = 5;

    public LewisCraftingGrid(int width, int height) {
        super(width * height);
        this.width = width;
        this.height = height;
    }

    public LewisCraftingGrid(int width, int height, ItemStack... items) {
        super(items);
        this.width = width;
        this.height = height;
        markDirty();
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
        assert x >= 0 && x < width;
        assert y >= 0 && y < height;
        return y * width + x;
    }

    private Collection<Integer> getNeighbourPositions(int x, int y) {
        List<Integer> slots = new ArrayList<>();
        if (x > 0) slots.add(toSlot(x - 1, y));
        if (x < width - 1) slots.add(toSlot(x + 1, y));
        if (y > 0) slots.add(toSlot(x, y - 1));
        if (y < height - 1) slots.add(toSlot(x, y + 1));
        return slots;
    }

    /**
     * Creates a topological graph based on the crafting grid. Intended to simplify traversal.
     */
    private MoleculeItemGraph getPositionGraph() {
        MoleculeItemGraph graph = new MoleculeItemGraph();

        // First build vertices
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                ItemStack stack = getStack(toSlot(x, y));
                // We assume only AtomItems make it into the LCT.
                if (stack.isEmpty()) continue;
                Atom atom = ((AtomItem) stack.getItem()).getAtom();
                graph.addVertex(atom, stack);
            }
        }

        // Then connect them
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
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
//        Minelabs.LOGGER.info("Position graph\n" + positionGraph.toString());

        // We use MoleculeItemGraph which can keep a mapping between vertices and ItemStacks.
        MoleculeItemGraph structure = positionGraphToMoleculeItemGraph(positionGraph);
//        Minelabs.LOGGER.info("Molecule formed: " + structure.toString());
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
