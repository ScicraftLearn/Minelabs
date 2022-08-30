package be.uantwerpen.minelabs.crafting.ionic;

import be.uantwerpen.minelabs.crafting.lewis.LewisCraftingGrid;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

import static be.uantwerpen.minelabs.gui.ionic_gui.IonicBlockScreenHandler.GRIDSIZE;

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
        return new LewisCraftingGrid(3 ,3 ,items);
    }

    public LewisCraftingGrid getRightGrid() {
        ItemStack[] items = new ItemStack[9];
        for (int i = 0; i < GRIDSIZE; i++) {
            items[i] = this.getStack(i + GRIDSIZE);
        }
        return new LewisCraftingGrid(3, 3, items);
    }
}
