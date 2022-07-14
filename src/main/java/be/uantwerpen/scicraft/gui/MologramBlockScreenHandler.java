package be.uantwerpen.scicraft.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

public class MologramBlockScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    PropertyDelegate propertyDelegate;
    private ItemStack output;
    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public MologramBlockScreenHandler(int syncId, PlayerInventory playerInventory) {
        //this(syncId, playerInventory, new SimpleInventory(35));
        this(syncId, playerInventory, new SimpleInventory(1), new ArrayPropertyDelegate(3));
    }
    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public MologramBlockScreenHandler(int syncId, @NotNull PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(Screens.MOLOGRAM_SCREEN_HANDLER, syncId);
        checkSize(inventory, 1);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.addProperties(propertyDelegate);
        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);

        //This will place the slot in the correct locations for a 3x3 Grid. The slots exist on both server and client!
        //This will not render the background of the slots however, this is the Screens job
        int m;
        int l;

        // offset
        int o = 11 - 29;
        // TODO position of slots
        // Lewis Crafting Table Inventory (5x5 grid)

        this.addSlot(new Slot(inventory, 0, 18*4, 18*5-o));



        //The player inventory (3x9 slots)
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 , 8 + l * 18, 122 + m * 18 - o + 5));
            }
        }
        //The player Hotbar (9 slots)
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m+3*9, 8 + m * 18, 180 - o + 5));
        }

        this.addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                handler.sendContentUpdates();
                handler.onContentChanged(inventory);
                handler.sendContentUpdates();
            }

            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                //maybe use later
                handler.updateToClient();
            }
        });
    }
    /*
    public int getPropertyDelegate(int index) {
        return propertyDelegate.get(index);
    }

    public void setPropertyDelegate(int index, int a) {
        propertyDelegate.set(index, a);
    }

    public Inventory getInventory() {
        return inventory;
    }
*/

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
