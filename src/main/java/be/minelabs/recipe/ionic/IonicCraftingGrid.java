package be.minelabs.recipe.ionic;


import be.minelabs.recipe.lewis.LewisCraftingGrid;
import be.minelabs.recipe.molecules.MoleculeItemGraph;
import net.minecraft.item.ItemStack;

public class IonicCraftingGrid extends LewisCraftingGrid {

    public IonicCraftingGrid(int width, int height) {
        super(width, height);
    }

    public IonicCraftingGrid(int width, int height, ItemStack... items) {
        super(width, height, items);
    }

    @Override
    protected MoleculeItemGraph positionGraphToMoleculeItemGraph(MoleculeItemGraph structure) {
        // TODO new ALGORITHM!
        return super.positionGraphToMoleculeItemGraph(structure);
    }
}
