package be.minelabs.recipe.ionic;

import be.minelabs.recipe.lewis.LewisCraftingGrid;
import be.minelabs.recipe.molecules.PartialMolecule;
import be.minelabs.screen.IonicBlockScreenHandler;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

/**
 * Inventory which can give 2 Lewisgrids.
 */
public class IonicInventory extends SimpleInventory {

    public IonicInventory(int gridLeft, int gridRight, int other) {
        super(gridLeft + gridRight + other);
    }

    public LewisCraftingGrid getLeftGrid() {
        ItemStack[] items = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            items[i] = this.getStack(i);
        }
        return new IonicCraftingGrid(3, 3, items);
    }

    public LewisCraftingGrid getRightGrid() {
        ItemStack[] items = new ItemStack[9];
        for (int i = 0; i < IonicBlockScreenHandler.GRIDSIZE; i++) {
            items[i] = this.getStack(i + IonicBlockScreenHandler.GRIDSIZE);
        }
        return new IonicCraftingGrid(3, 3, items);
    }

    public ItemStack getContainerStack() {
        return getStack(27);
    }

    public ItemStack getOutputStack() {
        return getStack(28);
    }
}
