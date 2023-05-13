package be.minelabs.crafting.ionic;

import be.minelabs.crafting.lewis.LewisCraftingGrid;
import be.minelabs.gui.ionic_gui.IonicBlockScreenHandler;
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
        return new LewisCraftingGrid(3 ,3 ,items);
    }

    public LewisCraftingGrid getRightGrid() {
        ItemStack[] items = new ItemStack[9];
        for (int i = 0; i < IonicBlockScreenHandler.GRIDSIZE; i++) {
            items[i] = this.getStack(i + IonicBlockScreenHandler.GRIDSIZE);
        }
        return new LewisCraftingGrid(3, 3, items);
    }
}
