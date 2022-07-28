package be.uantwerpen.scicraft.gui.lewis_gui;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.gui.Screens;
import be.uantwerpen.scicraft.gui.lewis_gui.slots.LewisCraftingResultSlot;
import be.uantwerpen.scicraft.gui.lewis_gui.slots.LewisErlenmeyerSlot;
import be.uantwerpen.scicraft.gui.lewis_gui.slots.LewisGridSlot;
import be.uantwerpen.scicraft.gui.lewis_gui.slots.LewisInputSlot;
import be.uantwerpen.scicraft.item.AtomItem;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.lewisrecipes.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LewisBlockScreenHandler extends ScreenHandler {

    private final ScreenHandlerContext context;
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    private Molecule currentMolecule;

    private final LewisCraftingResultSlot outputSlot;
    private final LewisErlenmeyerSlot erlenmeyerSlot;

    /**
     * This constructor gets called on the client when the server wants it to open the screenHandler<br>
     * The client will call the other constructor with an empty Inventory and the screenHandler will automatically
     * sync this empty inventory with the inventory on the server.
     *
     * @param syncId          ID used to sync client and server handlers
     * @param playerInventory Player's inventory to sync with screen's inventory slots
     */
    public LewisBlockScreenHandler(int syncId, PlayerInventory playerInventory) {
        //this(syncId, playerInventory, new SimpleInventory(35));
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY, new SimpleInventory(36), new ArrayPropertyDelegate(DelegateSettings.DELEGATE_SIZE));
        this.onContentChanged(inventory);
    }

    /**
     * This constructor gets called from the BlockEntity on the server without calling the other constructor first,
     * the server knows the inventory of the container and can therefore directly provide it as an argument.
     * This inventory will then be synced to the client.
     *
     * @param syncId           ID used to sync client and server handlers
     * @param playerInventory  Player's inventory to sync with screen's inventory slots
     * @param context          The BlockEntity's context for running functions on its world and position
     * @param inventory        The BlockEntity's inventory
     * @param propertyDelegate PropertyDelegate is used to sync data across server and client side handlers
     */
    public LewisBlockScreenHandler(int syncId, @NotNull PlayerInventory playerInventory, ScreenHandlerContext context, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(Screens.LEWIS_SCREEN_HANDLER, syncId);
        checkSize(inventory, 36);
        this.context = context;
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;

        this.currentMolecule = null;

        this.addProperties(propertyDelegate);
        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);

        //This will place the slot in the correct locations for a 3x3 Grid. The slots exist on both server and client!
        //This will not render the background of the slots however, this is the Screens job
        int m;
        int l;

        // offset
        int o = 11 - 29;

        // Lewis Crafting Table Inventory (5x5 grid)
        for (m = 0; m < 5; ++m) {
            for (l = 0; l < 5; ++l) {
                this.addSlot(new LewisGridSlot(inventory, l + m * 5, 8 + l * 18, m * 18 - o));
            }
        }
        // Lewis Crafting Table Inventory (9 input slots)
        for (m = 0; m < 9; ++m) {
            this.addSlot(new LewisInputSlot(inventory, m + 25, 8 + m * 18, 5 * 18 - o + 5) {
                @Override
                public boolean isEnabled() {
                    return propertyDelegate.get(DelegateSettings.LCT_TEXTURE_ID) == 1;
                }
            });
        }

        // Lewis Crafting Table Inventory (1 output slot)
        this.outputSlot = (LewisCraftingResultSlot) this.addSlot(new LewisCraftingResultSlot(inventory, 34, 8 + 7 * 18, 2 * 18 - o));

        // Lewis Crafting Table Inventory (1 slot for erlenmeyer)
        this.erlenmeyerSlot = (LewisErlenmeyerSlot) this.addSlot(new LewisErlenmeyerSlot(inventory, 35, 8 + 7 * 18, 2 * 18 - o + 36));

        //The player inventory (3x9 slots)
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 122 + m * 18 - o + 5));
            }
        }
        //The player Hotbar (9 slots)
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 180 - o + 5));
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

        onContentChanged(inventory);
    }

    /**
     * Allows external access to {@link PropertyDelegate#get(int)}
     *
     * @param index Requested index from {@code PropertyDelegate}
     * @return Returns the property at {@code index} in {@code PropertyDelegate}
     */
    public int getPropertyDelegate(int index) {
        return propertyDelegate.get(index);
    }

    /**
     * Allows external access to {@link PropertyDelegate#set(int, int)}
     *
     * @param index Index in {@code PropertyDelegate} to set the value of
     * @param value Value to set at {@code index} in {@code PropertyDelegate}
     */
    public void setPropertyDelegate(int index, int value) {
        propertyDelegate.set(index, value);
    }

    /**
     * Supplies {@link LewisScreen} with an instance of the BlockEntity's Inventory
     *
     * @return Returns the BlockEntity's Inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Check if a player has access to the inventory and thus the BlockEntity
     *
     * @param player Player to check access for
     * @return Returns true if the player has access
     */
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Shift + Player Inv Slot

    /**
     * Gets called when a player shift clicks while the screen is open
     *
     * @param player  Player that's shift clicking
     * @param invSlot SlotId of the clicked slot (-999 if outside of inventory, else -1 if not on slot)
     * @return The {@link ItemStack} to be left in the clicked slot
     */
    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        this.sendContentUpdates();
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
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
        this.sendContentUpdates();
        return newStack;
    }

    /**
     * Starts the crafting animation and stores the molecule that's being crafted
     *
     * @param molecule The {@link Molecule} that will be crafted once the animation is done
     */
    public void craftingAnimation(Molecule molecule) {
        if (getPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS) >= 0) return;
        this.setPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS, 0);
        this.currentMolecule = molecule;
    }

    /**
     * Performs a slot click. This can behave in many different ways depending mainly on the action type.
     *
     * @param slotIndex  SlotId of the clicked slot (-999 if outside of inventory, else -1 if not on slot)
     * @param button     Mouse button used for the click (0 = Left click | 1 = Right click)
     * @param actionType The kind of click performed
     * @param player     Player that's clicking
     */
    @Override
    public void onSlotClick(int slotIndex, int button, @NotNull SlotActionType actionType, PlayerEntity player) {
        if (slotIndex < 0) return;
        if (actionType.equals(SlotActionType.CLONE)) {
            if (player.getAbilities().creativeMode && this.getCursorStack().isEmpty()) {
                Slot slot = this.getSlot(slotIndex);
                if (slot.hasStack()) {
                    ItemStack stack = slot.getStack().copy();
                    if (stack.hasNbt())
                        stack.getOrCreateNbt().remove("ScicraftItemInLCT");
                    stack.setCount(stack.getMaxCount());
                    this.setCursorStack(stack);
                }
            }
        } else if (actionType.equals(SlotActionType.QUICK_CRAFT)) {
            this.endQuickCraft();
            this.sendContentUpdates();
            this.updateToClient();
        } else if (actionType.equals(SlotActionType.QUICK_MOVE)) {
            Slot slot = slots.get(slotIndex);
            if (!slot.canTakeItems(player)) return;
            if (slot instanceof LewisInputSlot || slot instanceof LewisCraftingResultSlot) {
                player.giveItemStack(slot.getStack());
                slot.setStack(ItemStack.EMPTY);
            } else for (int i = 25; i < inventory.size(); i++) {
                Slot inventorySlot = this.getSlot(i);
                if (!slot.isEnabled()) continue;
                if (inventorySlot instanceof LewisGridSlot) continue;
                if ((inventorySlot instanceof LewisInputSlot && slot.getStack().getItem() instanceof AtomItem)
                        || (inventorySlot instanceof LewisErlenmeyerSlot && slot.getStack().getItem().equals(Items.ERLENMEYER))) {
                    if (!inventorySlot.canInsert(slot.getStack())) continue;
                    int insertCount = Math.min(slot.getStack().getCount(), inventorySlot.getStack().getCount() >= inventorySlot.getMaxItemCount(slot.getStack())
                            ? 0 : inventorySlot.getMaxItemCount(slot.getStack()) - inventorySlot.getStack().getCount());
                    if (insertCount <= 0) continue;
                    if (inventorySlot.getStack().isEmpty()) {
                        inventorySlot.setStack(slot.getStack().copy());
                        inventorySlot.getStack().setCount(insertCount);
                    } else {
                        inventorySlot.getStack().setCount(inventorySlot.getStack().getCount() + insertCount);
                    }
                    if (slot.getStack().getCount() <= insertCount) {
                        slot.setStack(ItemStack.EMPTY);
                        break;
                    } else {
                        slot.getStack().setCount(slot.getStack().getCount() - insertCount);
                    }
                }
            }
        } else if (this.slots.get(slotIndex) instanceof LewisGridSlot slot && actionType == SlotActionType.PICKUP) {
            if (slot.isLocked()) return;
            if (this.getCursorStack().isEmpty())
                this.getSlot(slotIndex).setStack(ItemStack.EMPTY);
            else slot.canInsert(getCursorStack());
        } else super.onSlotClick(slotIndex, button, actionType, player);
    }

    /**
     * Gets called whenever the inventory gets changed
     *
     * @param inventory The changed inventory
     */
    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);

        boolean inputEmpty = true;
        for (int i = 25; i < 34; i++) {
            if (this.getSlot(i).getStack().getCount() != 0) {
                inputEmpty = false;
                break;
            }
        }

        if (inputEmpty) openGridSlots();
        else closeGridSlots();

        Atom[][] atoms = new Atom[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Item item = this.inventory.getStack(i * 5 + j).getItem();
                atoms[i][j] = item instanceof AtomItem ? ((AtomItem) item).getAtom() : null;
            }
        }

        Map<Atom, Integer> ingredients = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (atoms[i][j] != null)
                    ingredients.put(atoms[i][j], ingredients.getOrDefault(atoms[i][j], 0) + 1);
            }
        }

        context.run((world, pos) -> updateContent(world));
    }

    /**
     * Creates a {@link LewisCraftingGrid} from the 5x5 grid
     *
     * @return the created {@code LewisCraftingGrid}
     */
    public LewisCraftingGrid getLewisCraftingGrid() {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            stacks.add(inventory.getStack(i));
        }
//        Scicraft.LOGGER.info("stacks: " + stacks);

        return new LewisCraftingGrid(stacks.toArray(new ItemStack[0]));
    }

    private void updateContent(World world) {
        LewisCraftingGrid grid = getLewisCraftingGrid();
        Optional<MoleculeRecipe> recipe = world.getRecipeManager().getFirstMatch(MoleculeRecipe.MOLECULE_CRAFTING, grid, world);


        if (recipe.isEmpty()) {
            if (isInputOpen()) closeInputSlots();
            return;
        }

        MoleculeRecipe moleculeRecipe = recipe.get();

        if (!isInputOpen() || !inventory.isEmpty()) {
            Scicraft.LOGGER.info("Opening input");
            for (int m = 0; m < 9; ++m) {
                ((LewisInputSlot)getSlot(m + 25)).setDensity(moleculeRecipe.getDensity());
            }

            ArrayList<Atom> atoms = new ArrayList<>(moleculeRecipe.getMolecule().getIngredients());
            atoms.sort(Comparator.comparingInt(a -> DelegateSettings.ATOM_MAPPINGS.get(a.getItem())));
            openInputSlots(atoms);
        }

        if (hasCorrectInput(moleculeRecipe.getMolecule(),moleculeRecipe)) {
            if (this.getPropertyDelegate(1) == -1
                    && (outputSlot.getStack().isEmpty()
                    || ItemStack.areEqual(
                    outputSlot.getStack(),
                    new ItemStack(
                            moleculeRecipe.getMolecule().getItem(),
                            outputSlot.getStack().getCount()
                    )
            )
            )
            )
                this.craftingAnimation(moleculeRecipe.getMolecule());
        } else {
            //arrow is running but input is no longer valid
            if (this.getPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS) >= 0 && this.getPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS) < 23) {
                //stop crafting animation
                this.setPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS, -1);
                this.currentMolecule = null;
            }
        }
    }

    private boolean hasCorrectInput(Molecule molecule,MoleculeRecipe moleculeRecipe) {
        List<Atom> ingredients = new ArrayList<>(molecule.getIngredients());
        ingredients.sort(Comparator.comparingInt(o -> DelegateSettings.ATOM_MAPPINGS.get(o.getItem())));
        int readySlots = 1;
        boolean isCorrect = true;
        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack stack = inventory.getStack(i + 25);
            if (stack == null
                    || stack.isEmpty()
                    || !ingredients.get(i).getItem().equals(stack.getItem())
                    || stack.getCount() != moleculeRecipe.getDensity()) {
                isCorrect = false;
            } else readySlots *= DelegateSettings.SLOT_MAPPINGS.get(i);
        }
        this.setPropertyDelegate(DelegateSettings.LCT_SLOT_READY, readySlots);
        return isCorrect && erlenmeyerSlot.getStack().getItem().equals(Items.ERLENMEYER);
    }

    /**
     * Sets the crafted item in the output slot,
     * clears the input slots and clears the saved molecule
     *
     * @return Returns false if no molecule was saved (and the operation failed)
     */
    public boolean setOutput() {
        if (currentMolecule == null) return false;
        if (outputSlot.hasStack()) outputSlot.getStack().increment(1);
        else outputSlot.setStack(currentMolecule.getItem().getDefaultStack());
        clearInput(true);
        currentMolecule = null;
        return true;
    }

    /**
     * @return Returns true if the grid is unlocked
     */
    protected boolean isGridOpen() {
        return ((LewisGridSlot) this.getSlot(0)).isLocked();
    }

    /**
     * @return Returns true if the input slots are enabled
     */
    protected boolean isInputOpen() {
        return this.getSlot(25).isEnabled();
    }

    /**
     * @return Returns true if the input slots are empty
     */
    protected boolean isInputEmpty() {
        for (int i = 0; i < 9; i++) {
            if (!this.getSlot(i + 25).getStack().isEmpty()) return false;
        }
        return true;
    }

    /**
     * Clears the input slots
     *
     * @param erlenmeyer Whether to take an erlenmeyer out as well
     *                   (if the recipe uses it)
     */
    protected void clearInput(boolean erlenmeyer) {
        if (erlenmeyer) erlenmeyerSlot.getStack().decrement(1);
        for (int i = 0; i < 9; i++) {
            this.getSlot(i + 25).setStack(ItemStack.EMPTY);
        }


    }

    /**
     * Opens all grid slots
     */
    protected void openGridSlots() {
        for (int i = 0; i < 25; i++) {
            ((LewisGridSlot) this.getSlot(i)).setLocked(false);
        }
    }

    /**
     * Opens the required input slots for the given {@link Atom}{@code s}
     *
     * @param atoms {@link List} of {@code Atoms} to allow in the slots
     */
    protected void openInputSlots(@NotNull List<Atom> atoms) {
        setPropertyDelegate(DelegateSettings.LCT_TEXTURE_ID, 1);
        int slotItems = 1;
        for (int i = 0; i < atoms.size(); i++) {
            Item atom = atoms.get(i).getItem();
            ((LewisInputSlot) this.getSlot(i + 25)).setAllowedItem(atom);
            this.sendContentUpdates();

            slotItems *= DelegateSettings.ATOM_MAPPINGS.get(atom);
        }
        this.setPropertyDelegate(DelegateSettings.LCT_SLOT_ITEMS, slotItems);
    }

    /**
     * Closes the grid slots
     */
    protected void closeGridSlots() {
        for (int i = 0; i < 25; i++) {
            ((LewisGridSlot) this.getSlot(i)).setLocked(true);
        }
    }

    /**
     * Closes the input slots
     */
    protected void closeInputSlots() {
        setPropertyDelegate(DelegateSettings.LCT_TEXTURE_ID, 0);
        for (int i = 25; i < 34; i++) {
            ((LewisInputSlot) this.getSlot(i)).setAllowedItem(null);
            this.sendContentUpdates();
        }
        //closeErlenmeyer();
    }
}
