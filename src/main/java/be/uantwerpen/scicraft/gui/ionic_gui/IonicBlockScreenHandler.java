package be.uantwerpen.scicraft.gui.ionic_gui;

import be.uantwerpen.scicraft.gui.Screens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class IonicBlockScreenHandler extends ScreenHandler {

    private final Inventory inventory;

    public IonicBlockScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId,playerInventory,new SimpleInventory(29));
    }

    public IonicBlockScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(Screens.IONIC_SCREEN_HANDLER, syncId);
        checkSize(inventory, 29);
        this.inventory=inventory;
        inventory.onOpen(playerInventory.player);

        //first 3x3 gridslots(left)
        //14 is the x position where the top-left corner of the square for the item needs to be drawn, 18 is height of 1 square
        //22 is the y position where the top-left corner of the square needs to be
        for(int i = 0; i<3;i++){
            for(int y = 0;y<3;y++){
                this.addSlot(new Slot(inventory,i*3+y,14+y*18,22+i*18));
            }
        }
        //second 3x3 gridslots(right)
        for(int i = 0; i<3;i++){
            for(int y = 0;y<3;y++){
                this.addSlot(new Slot(inventory,i*3+y+9,87+y*18,22+i*18));
            }
        }
        //row of inputslots
        for(int i = 0; i<9;i++){
            this.addSlot(new Slot(inventory,i+18,12+i*18,86));
        }
        //erlemeyer slot
        this.addSlot(new Slot(inventory,27,178,86));
        //result slot
        this.addSlot(new Slot(inventory,28,176,40));
        //add inventory and hotbar slots
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i+29, 23 + i * 18, 176));
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9+29, 23 + l * 18, 118 + i * 18));
            }
        }
    }


}
