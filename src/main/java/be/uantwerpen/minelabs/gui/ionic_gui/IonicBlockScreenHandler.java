package be.uantwerpen.minelabs.gui.ionic_gui;

import be.uantwerpen.minelabs.block.entity.IonicBlockEntity;
import be.uantwerpen.minelabs.crafting.ionic.IonicInventory;
import be.uantwerpen.minelabs.gui.ScreenHandlers;
import be.uantwerpen.minelabs.inventory.slot.CraftingResultSlot;
import be.uantwerpen.minelabs.inventory.slot.FilteredSlot;
import be.uantwerpen.minelabs.inventory.slot.LockableGridSlot;
import be.uantwerpen.minelabs.item.Items;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class IonicBlockScreenHandler extends ScreenHandler {

    public static final int GRIDSIZE = 9;
    private final IonicInventory inventory;
    //The IonicBlockEntity
    private IonicBlockEntity ionic;
    //PropertyDelegate that holds the progress, density and charge of both sides.
    private final PropertyDelegate propertyDelegate;

    /**
     * This constructor gets called on the client when the server wants it to open the screenHandler<br>
     * The client will call the other constructor with an empty Inventory and the screenHandler will automatically
     * sync this empty inventory with the inventory on the server.
     *
     * This constructor uses the buffer from {@link IonicBlockEntity#writeScreenOpeningData(ServerPlayerEntity, PacketByteBuf)}
     * This gives it the Blockpos of the BlockEntity
     */
    public IonicBlockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buff) {
        this(syncId,playerInventory,new IonicInventory(9, 9, 11), new ArrayPropertyDelegate(5), buff.readBlockPos());
    }

    /**
     * This constructor gets called from the BlockEntity on the server without calling the other constructor first,
     * the server knows the inventory of the container and can therefore directly provide it as an argument.
     * This inventory will then be synced to the client.
     *
     * @param syncId
     * @param playerInventory
     * @param inventory
     * @param propertyDelegate
     * @param pos
     */
    public IonicBlockScreenHandler(int syncId, PlayerInventory playerInventory, IonicInventory inventory, PropertyDelegate propertyDelegate, BlockPos pos) {
        super(ScreenHandlers.IONIC_SCREEN_HANDLER, syncId);
        checkSize(inventory, 29);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        BlockEntity be = playerInventory.player.world.getBlockEntity(pos);
        if (be instanceof IonicBlockEntity ionic) {
            this.ionic = ionic;
        }

        //Register properties for syncing
        this.addProperties(propertyDelegate);

        //Register slot listener, to reset the recipe if the items change, and sync
        this.addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                if (slotId < GRIDSIZE*2) {
                    ionic.resetRecipe();
                }
            }

            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {

            }
        });

        inventory.onOpen(playerInventory.player);

        //first 3x3 gridslots(left)
        //14 is the x position where the top-left corner of the square for the item needs to be drawn, 18 is height of 1 square
        //22 is the y position where the top-left corner of the square needs to be
        for(int i = 0; i<3;i++){
            for(int y = 0;y<3;y++){
                this.addSlot(new LockableGridSlot(inventory,i*3+y,14+y*18,22+i*18) {
                    @Override
                    public boolean isLocked() {
                        return !isInputEmpty();
                    }
                });
            }
        }
        //second 3x3 gridslots(right)
        for(int i = 0; i<3;i++){
            for(int y = 0;y<3;y++){
                this.addSlot(new LockableGridSlot(inventory,i*3+y+9,87+y*18,22+i*18) {
                    @Override
                    public boolean isLocked() {
                        return !isInputEmpty();
                    }
                });
            }
        }
        //row of inputslots
        for(int i = 0; i<9;i++){
            this.addSlot(new Slot(inventory,i+18,12+i*18,86) {

                @Override
                public boolean isEnabled() {
                    return hasRecipe();
                }

                @Override
                public boolean canInsert(ItemStack stack) {
                    if (getLeftIngredients().size() > this.getIndex()-2*GRIDSIZE && this.getIndex()-2*GRIDSIZE >= 0) {
                        return getLeftIngredients().get(this.getIndex()-2*GRIDSIZE).test(stack);
                    }
                    if (getRightIngredients().size() > this.getIndex()-2*GRIDSIZE - getLeftIngredients().size() && this.getIndex()-2*GRIDSIZE - getLeftIngredients().size() >= 0) {
                        return getRightIngredients().get(this.getIndex()-2*GRIDSIZE - getLeftIngredients().size()).test(stack);
                    }
                    return false;
                }

                @Override
                public int getMaxItemCount(ItemStack stack) {
                    if (getLeftIngredients().size() > this.getIndex()-2*GRIDSIZE) { //Slot differnce of GRIDSIZE due to the grid
                        return getLeftDensity();
                    }
                    if (getRightIngredients().size() > this.getIndex()-2*GRIDSIZE - getLeftIngredients().size()) { //Slot differnce of GRIDSIZE due to the grid
                        return getRightDensity();
                    }
                    return 0;
                }
            });
        }
        //erlemeyer slot
        this.addSlot(new FilteredSlot(inventory,27,178,86, s -> s.isOf(Items.ERLENMEYER)));
        //result slot
        this.addSlot(new CraftingResultSlot(inventory,28,176,40));
        //add inventory and hotbar slots
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 23 + l * 18, 118 + m * 18));
            }
        }
        //The player Hotbar (9 slots)
        for (int m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 23 + m * 18, 176));
        }

    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    /**
     * Gets called when a player shift clicks while the screen is open
     *
     * @param player  Player that's shift clicking
     * @param invSlot SlotId of the clicked slot (-999 if outside of inventory, else -1 if not on slot)
     * @return The {@link ItemStack} to be left in the clicked slot
     */
    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (invSlot < GRIDSIZE * 2) {
            slot.setStack(itemStack);
            return itemStack;
        }
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(itemStack2, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, GRIDSIZE * 2, this.inventory.size(), false)) { //start from slot GRIDSIZE, this is outside of the grid.
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return itemStack;
    }

    /**
     * Inserts the item into a slot, trying indexes from {@param startIndex} to {@param endIndex}.
     * If {@param fromLast}, it goes from {@param endIndex} to {@param startIndex}.
     *
     * @param stack
     * @param startIndex
     * @param endIndex
     * @param fromLast
     * @return
     */
    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        if(startIndex < GRIDSIZE *2) {
            return false;
        }
        return super.insertItem(stack, startIndex, endIndex, fromLast);
    }

    /**
     * Called when dragging items in the inventory
     *
     * @param slot
     * @return
     */
    @Override
    public boolean canInsertIntoSlot(Slot slot) {
        if(slot.getIndex() > GRIDSIZE * 2 -1) {
            return false;
        }
        return super.canInsertIntoSlot(slot);
    }

    /**
     * @return Returns true if the input slots are empty
     */
    public boolean isInputEmpty() {
        for (int i = 0; i < 9; i++) {
            if (!this.getSlot(i + (GRIDSIZE * 2)).getStack().isEmpty()) return false;
        }
        return true;
    }

    public int getProgress() {
        return propertyDelegate.get(0);
    }

    public DefaultedList<Ingredient> getLeftIngredients() {
        return ionic.getLeftIngredients();
    }

    public DefaultedList<Ingredient> getRightIngredients() {
        return ionic.getRightIngredients();
    }

    public int getLeftDensity() {
        return propertyDelegate.get(1);
    }

    public int getRightDensity() {
        return propertyDelegate.get(2);
    }

    public int getLeftCharge() {
        return propertyDelegate.get(3);
    }

    public int getRightCharge() {
        return propertyDelegate.get(4);
    }

    //A recipe is found (so the density is larger than 0)
    public boolean hasRecipe() {
        return this.getLeftDensity() > 0 && this.getRightDensity() > 0;
    }

    public IonicInventory getInventory() {
        return this.inventory;
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 23 + i * 18, 176));
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9, 23 + l * 18, 118 + i * 18));
            }
        }
    }
}
