package be.uantwerpen.scicraft.gui;

import be.uantwerpen.scicraft.Scicraft;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import be.uantwerpen.scicraft.gui.LewisCraftingResultSlot;
import be.uantwerpen.scicraft.gui.LewisInputSlot;

import java.util.logging.Logger;


public class LewisBlockScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public LewisBlockScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(35));

    }

    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public LewisBlockScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(Screens.LEWIS_SCREEN_HANDLER, syncId);
        checkSize(inventory, 35);
        this.inventory = inventory;
        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);

        //This will place the slot in the correct locations for a 3x3 Grid. The slots exist on both server and client!
        //This will not render the background of the slots however, this is the Screens job
        int m;
        int l;

        // offset
        int o = 11-29;

        // Lewis Crafting Table Inventory (5x5 grid)
        for (m = 0; m < 5; ++m) {
            for (l = 0; l < 5; ++l) {
                this.addSlot(new Slot(inventory, l + m * 5, 8 + l * 18,  m * 18-o));
            }
        }
        // Lewis Crafting Table Inventory (9 input slots)
        for (m = 0; m < 9; ++m) {
            this.addSlot(new LewisInputSlot(inventory, m + 25, 8 + m * 18,5 * 18-o+5));
        }

        // Lewis Crafting Table Inventory (1 output slot)
        this.addSlot((new LewisCraftingResultSlot(inventory, 34, 8 + 7 * 18, 2 * 18-o)));

        //The player inventory (3x9 slots)
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 122 + m * 18-o+5));
            }
        }
        //The player Hotbar (9 slots)
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 180-o+5));
        }

        this.addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                // System.out.println("TEsT!!pls1");
                onContentChanged(inventory);
            }
            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                // System.out.println("TEsT!!pls2");
            }
        });

    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Shift + Player Inv Slot
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

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        //System.out.println(inventory.getStack(0));

        // todo: Bindingen
//        for (int i = 0; i < inventory.size(); i++) {
//            if(inventory.getStack(i).toString().equals("1 air")) {
//                continue;
//            }
//        }


//        boolean update = false;
//        for (int i = 0; i < 25; i++) {
//            if(inventory.getStack(i).toString().equals("1 air")) {
//                update = false;
//            } else {
//                update = true;
//                break;
//            }
//        }
//
//        if(update) {
//
//        }
    }
}