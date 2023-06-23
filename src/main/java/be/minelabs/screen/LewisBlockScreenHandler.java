package be.minelabs.screen;

import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.block.entity.LewisBlockEntity;
import be.minelabs.inventory.AtomicInventory;
import be.minelabs.inventory.OrderedInventory;
import be.minelabs.item.Items;
import be.minelabs.recipe.lewis.LewisCraftingGrid;
import be.minelabs.recipe.molecules.Bond;
import be.minelabs.recipe.molecules.MoleculeGraph;
import be.minelabs.screen.slot.AtomSlot;
import be.minelabs.screen.slot.CraftingResultSlot;
import be.minelabs.screen.slot.FilteredSlot;
import be.minelabs.screen.slot.LockableGridSlot;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class LewisBlockScreenHandler extends ScreenHandler {

    // size of inventory without grid
    private final int INVENTORY_SIZE = 11;
    public static final int GRIDSIZE = 25;

    private final LewisCraftingGrid craftingGrid;

    private final SimpleInventory ioInventory;

    private AtomicInventory atomicStorage = new AtomicInventory(512);

    //PropertyDelegate that holds the progress and density
    private final PropertyDelegate propertyDelegate;

    //The LewisBlockEntity that belongs to the screen. Can be used to get extra data, as long as that data is synced
    private LewisBlockEntity lewis;

    private boolean storage;


    /**
     * This constructor gets called on the client when the server wants it to open the screenHandler<br>
     * The client will call the other constructor with an empty Inventory and the screenHandler will automatically
     * sync this empty inventory with the inventory on the server.
     * <p>
     * This constructor uses the buffer from {@link LewisBlockEntity#writeScreenOpeningData(ServerPlayerEntity, PacketByteBuf)}
     * This gives it the Blockpos of the BlockEntity
     *
     * @param syncId
     * @param playerInventory
     * @param buf
     */
    public LewisBlockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new LewisCraftingGrid(5, 5), new OrderedInventory(11), new ArrayPropertyDelegate(3), buf.readBlockPos());
    }

    /**
     * This constructor gets called from the BlockEntity on the server without calling the other constructor first,
     * the server knows the inventory of the container and can therefore directly provide it as an argument.
     * This inventory will then be synced to the client.
     *
     * @param syncId           ID used to sync client and server handlers
     * @param playerInventory  Player's inventory to sync with screen's inventory slots
     * @param propertyDelegate PropertyDelegate is used to sync data across server and client side handlers
     */
    public LewisBlockScreenHandler(int syncId, @NotNull PlayerInventory playerInventory, LewisCraftingGrid craftingGrid, SimpleInventory ioInventory, PropertyDelegate propertyDelegate, BlockPos pos) {
        super(ScreenHandlers.LEWIS_SCREEN_HANDLER, syncId);
        checkSize(ioInventory, 10);
        this.craftingGrid = craftingGrid;
        this.ioInventory = ioInventory;
        this.propertyDelegate = propertyDelegate;
        storage = false;
        BlockEntity be = playerInventory.player.world.getBlockEntity(pos);
        if (be instanceof LewisBlockEntity lewis) {
            this.lewis = lewis;
        }

        //Register properties for syncing
        this.addProperties(propertyDelegate);

        //Register slot listener, to reset the recipe if the items change, and sync
        this.addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                if (slotId < GRIDSIZE) {
                    lewis.resetRecipe();
                    handler.updateToClient();
                } else {
                    handler.updateToClient();
                }
            }

            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                //maybe use later
                handler.updateToClient();
            }
        });

        //some inventories do custom logic when a player opens it.
        craftingGrid.onOpen(playerInventory.player);
        ioInventory.onOpen(playerInventory.player);

        //This will place the slot in the correct locations. The slots exist on both server and client!
        //This will not render the background of the slots however, this is the Screens job

        // offset
        int o = 11 - 29;

        // Lewis Crafting Table Inventory (5x5 grid)
        for (int m = 0; m < 5; ++m) {
            for (int l = 0; l < 5; ++l) {
                this.addSlot(new LockableGridSlot(craftingGrid, l + m * 5, 8 + l * 18, m * 18 - o) {//Anonymous implementation to link it to the slots.
                    @Override
                    public boolean isLocked() {
                        return !isInputEmpty(); //Locked if the input had items
                    }
                });
            }
        }
        // Lewis Crafting Table Inventory (9 input slots)
        for (int m = 0; m < 9; ++m) {
            this.addSlot(new Slot(ioInventory, m, 8 + m * 18, 5 * 18 - o + 5) {//Anonymous implementation to link it to the slots.
                @Override
                public boolean isEnabled() {
                    return hasRecipe();
                }

                @Override
                public boolean canInsert(ItemStack stack) {
                    if (getIngredients().size() > this.getIndex()) {
                        return getIngredients().get(this.getIndex()).test(stack);
                    }
                    return false;
                }
            });
        }

        // Lewis Crafting Table Inventory (1 slot for erlenmeyer)
        this.addSlot(new FilteredSlot(ioInventory, 9, 8 + 7 * 18, 2 * 18 - o + 36, s -> s.isOf(Items.ERLENMEYER)));

        // Lewis Crafting Table Inventory (1 output slot)
        this.addSlot(new CraftingResultSlot(ioInventory, 10, 8 + 7 * 18, 2 * 18 - o));

        // TODO ATOMIC STORAGE ... (check if "click/selected" pack/button)
        //  REDO SLOTS (dynamic change between player io and Atomic)
        if (storage){
            addAtomicSlots();
        } else {
            //The player inventory (3x9 slots)
            for (int m = 0; m < 3; ++m) {
                for (int l = 0; l < 9; ++l) {
                    this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 122 + m * 18 - o + 5));
                }
            }
            //The player Hotbar (9 slots)
            for (int m = 0; m < 9; ++m) {
                this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 180 - o + 5));
            }
        }
    }

    private void addAtomicSlots() {
        addSlot(new AtomSlot(atomicStorage, 0, 8, 145));
        addSlot(new AtomSlot(atomicStorage, 1, 152, 145));
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 9; ++j) {
                //TODO FIX INDEX (must match atoms to show)
                if (j==2)
                    continue;
                addSlot(new AtomSlot(atomicStorage, 2 + j + i * 9, 8 + j * 18, 163 + i * 18));
            }
        }
    }

    public Inventory getIoInventory() {
        return ioInventory;
    }

    /**
     * Check if a player has access to the inventory and thus the BlockEntity
     *
     * @param player Player to check access for
     * @return Returns true if the player has access
     */
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.ioInventory.canPlayerUse(player);
    }

    /**
     * Gets called when a player shift clicks while the screen is open
     *
     * @param player  Player that's shift clicking
     * @param invSlot SlotId of the clicked slot (-999 if outside of inventory, else -1 if not on slot)
     * @return The {@link ItemStack} to be left in the clicked slot
     */
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (invSlot < GRIDSIZE) {
            slot.setStack(itemStack);
            onGridChangedByPlayer(player);
            return itemStack;
        }
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (invSlot < GRIDSIZE + INVENTORY_SIZE) {
                if (!this.insertItem(itemStack2, GRIDSIZE + INVENTORY_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, GRIDSIZE, GRIDSIZE + INVENTORY_SIZE, false)) { //start from slot GRIDSIZE, this is outside of the grid.
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

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if (slotIndex >= 36 && slotIndex <= 71){
            if (getSlot(slotIndex).getStack().isOf(Items.ATOM_PACK)) {
                // TODO ENABLE BACK BUTTON
                storage = true;
                atomicStorage = new AtomicInventory(getSlot(slotIndex).getStack().getOrCreateNbt());
                return;
            }
        }
        super.onSlotClick(slotIndex, button, actionType, player);
        if (slotIndex < GRIDSIZE)
            onGridChangedByPlayer(player);
    }

    /**
     * Callback used for advancements
     */
    private void onGridChangedByPlayer(PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity serverPlayer))
            return;
        MoleculeGraph structure = getLewisCraftingGrid().getPartialMolecule().getStructure();

        boolean[] foundOrders = {false, false, false};
        for (MoleculeGraph.Edge edge : structure.getEdges()) {
            Bond bond = edge.data;
            foundOrders[bond.bondOrder - 1] = true;
        }
        for (int order = 1; order < foundOrders.length + 1; order++) {
            if (foundOrders[order - 1]) {
                Criteria.LCT_CRITERION.trigger(serverPlayer, order);
            }
        }
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
        if (startIndex < GRIDSIZE) {
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
        if (slot.getIndex() > GRIDSIZE - 1) {
            return false;
        }
        return super.canInsertIntoSlot(slot);
    }

    /**
     * @return Returns true if the input slots are empty
     */
    public boolean isInputEmpty() {
        for (int i = 0; i < 9; i++) {
            if (!this.getSlot(i + GRIDSIZE).getStack().isEmpty()) return false;
        }
        return true;
    }

    public DefaultedList<Ingredient> getIngredients() {
        return lewis.getIngredients();
    }

    public int getDensity() {
        return propertyDelegate.get(2);
    }

    //A recipe is found (so the density is larger than 0)
    public boolean hasRecipe() {
        return this.getDensity() > 0;
    }

    /**
     * Should The Screen render the Atom Storage (link)
     *
     * @return boolean
     */
    public boolean showAtomStorage(){
        return storage;
    }

    public LewisCraftingGrid getLewisCraftingGrid() {
        return craftingGrid;
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = propertyDelegate.get(0);
        int maxProgress = propertyDelegate.get(1);
        int arrowSize = 26;
        return maxProgress != 0 && progress != 0 ? progress * arrowSize / maxProgress : 0;
    }

    public int getStatus() {
        if (propertyDelegate.get(2) > 0) {
            // Density found -> json recipe found
            return 2;
        }else if (craftingGrid.isEmpty() || craftingGrid.getPartialMolecule().getStructure().getTotalOpenConnections() != 0) {
            // Empty grid or still has possible conections
            return 0;
        }else if (craftingGrid.getPartialMolecule().getStructure().isConnectedManagerFunctieOmdatJoeyZaagtZoalsVaak()) {
            return 1;
        } else {
            return 3;
        }
    }
}
