package be.uantwerpen.scicraft.gui;

import be.uantwerpen.scicraft.item.AtomItem;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.lewisrecipes.Atom;
import be.uantwerpen.scicraft.lewisrecipes.DelegateSettings;
import be.uantwerpen.scicraft.lewisrecipes.Molecule;
import be.uantwerpen.scicraft.lewisrecipes.RecipeManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LewisBlockScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public LewisBlockScreenHandler(int syncId, PlayerInventory playerInventory) {
        //this(syncId, playerInventory, new SimpleInventory(35));
        this(syncId, playerInventory, new SimpleInventory(36), new ArrayPropertyDelegate(DelegateSettings.DELEGATE_SIZE));
        this.onContentChanged(inventory);
    }

    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public LewisBlockScreenHandler(int syncId, @NotNull PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(Screens.LEWIS_SCREEN_HANDLER, syncId);
        checkSize(inventory, 36);
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
        this.addSlot(new LewisCraftingResultSlot(inventory, 34, 8 + 7 * 18, 2 * 18 - o));

        // Lewis Crafting Table Inventory (1 slot for erlenmeyer)
        this.addSlot(new LewisErlenmeyerSlot(inventory, 35, 8 + 7 * 18, 2 * 18 - o + 36));

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

    public int getPropertyDelegate(int index) {
        return propertyDelegate.get(index);
    }

    public void setPropertyDelegate(int index, int a) {
        propertyDelegate.set(index, a);
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Shift + Player Inv Slot
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

    public void craftingAnimation(Molecule molecule) {
//        if (getPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS) >= 0) return;
//        this.setPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS, 0);
//        this.setPropertyDelegate(DelegateSettings.LCT_CURRENT_MOLECULE, molecule.ordinal());
    }

    @Override
    public void onSlotClick(int slotIndex, int button, @NotNull SlotActionType actionType, PlayerEntity player) {
        if (slotIndex < 0) return;
        if (actionType.equals(SlotActionType.QUICK_CRAFT)) {
            this.endQuickCraft();
            this.sendContentUpdates();
            this.updateToClient();
        } else if (actionType.equals(SlotActionType.QUICK_MOVE)) {
            Slot slot = slots.get(slotIndex);
            if (!slot.canTakeItems(player)) return;
            for (int i = 25; i < inventory.size(); i++) {
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

    @Override
    public void onContentChanged(Inventory inventory) {
        this.sendContentUpdates();

        // TODO: Show bonds where possible

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

        Molecule molecule = RecipeManager.getMolecule(ingredients);
        if (molecule == null) {
            if (isInputOpen()) {
                closeInputSlots();
                openGridSlots();
            }
            return;
        }

        if (!RecipeManager.isCorrectMolecule(molecule, atoms)) {
            if (isInputOpen()) {
                closeInputSlots();
                openGridSlots();
            }
            return;
        }

        if (isInputOpen()) {
            if (isInputEmpty()) {
                if (!isGridOpen()) openGridSlots();
            } else {
                if (isGridOpen()) closeGridSlots();
            }
        } else {
            openInputSlots(getIngredients(molecule));
        }

        if (hasCorrectInput(molecule)) {
//            if (this.getPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS) == -1)
//                this.craftingAnimation(molecule);
            // reset the crafting animation so it can start over later
            this.setPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS, -1);
            // get the molecule that's being crafted
            // put it in the output slot
            clearInput(true);
            this.getSlot(34).setStack(molecule.getItem().getDefaultStack());
        } else {
//            if (this.getPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS) >= 0
//                    && this.getPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS) < 23)
//                this.setPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS, -1);
        }
    }

    protected boolean hasCorrectInput(Molecule molecule) {
        List<Atom> ingredients = this.getIngredients(molecule);
        int readySlots = 1;
        boolean isCorrect = true;
        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack stack = inventory.getStack(i + 25);
            if (stack == null
                    || stack.isEmpty()
                    || !ingredients.get(i).getItem().equals(stack.getItem())
                    || stack.getCount() != 10) {
                isCorrect = false;
            } else readySlots *= DelegateSettings.SLOT_MAPPINGS.get(i);
        }
        this.setPropertyDelegate(DelegateSettings.LCT_SLOT_READY, readySlots);
        return isCorrect && this.getSlot(35).getStack().getItem().equals(Items.ERLENMEYER);
    }

    @Contract(pure = true)
    public List<Atom> getIngredients(@NotNull Molecule molecule) {
        List<Atom> ingr = new ArrayList<>();
        molecule.getIngredients().keySet().forEach(atom -> {
            for (int i = 0; i < molecule.getIngredients().get(atom); i++)
                ingr.add(atom);
        });
        return ingr;
    }

    protected boolean isGridOpen() {
        return ((LewisGridSlot) this.getSlot(0)).isLocked();
    }

    protected boolean isInputOpen() {
        return this.getSlot(25).isEnabled();
    }

    protected boolean isInputEmpty() {
        for (int i = 0; i < 9; i++) {
            if (!this.getSlot(i + 25).getStack().isEmpty()) return false;
        }
        return true;
    }

    protected void clearInput(boolean erlenmeyer) {
        if (erlenmeyer) this.getSlot(35).getStack().decrement(1);
        for (int i = 0; i < 9; i++) {
            this.getSlot(i + 25).setStack(ItemStack.EMPTY);
        }
    }

    /*
     * code to open slots (by type)
     */
    protected void openGridSlots() {
        for (int i = 0; i < 25; i++) {
            ((LewisGridSlot) this.getSlot(i)).setLocked(false);
        }
    }

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

    protected void openOutputSlot() {
        ((LewisCraftingResultSlot) this.getSlot(34)).setReady(true);
    }

    /*
     * code to close slots (by type)
     */
    protected void closeGridSlots() {
        for (int i = 0; i < 25; i++) {
            ((LewisGridSlot) this.getSlot(i)).setLocked(true);
        }
    }

    protected void closeInputSlots() {
        setPropertyDelegate(DelegateSettings.LCT_TEXTURE_ID, 0);
        for (int i = 25; i < 34; i++) {
            ((LewisInputSlot) this.getSlot(i)).setAllowedItem(null);
            this.sendContentUpdates();
        }
        //closeErlenmeyer();
    }

    protected void closeOutputSlot() {
        ((LewisCraftingResultSlot) this.getSlot(34)).setReady(false);
    }

    /**
     * Gets called every tick.<br>
     * Checks if the crafting animation is over, and handles its completion
     */
    public void tick() {
        // if the crafting animation is over
/*
        if (this.getPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS) >= 23) {
            Scicraft.LOGGER.info("TICK DID SOMETHING");
            // reset the crafting animation so it can start over later
            this.setPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS, -1);
            // get the molecule that's being crafted
            Molecule molecule = Molecule.getFromOrdinal(this.getPropertyDelegate(DelegateSettings.LCT_CURRENT_MOLECULE));
            Scicraft.LOGGER.info("molecule (tick): " + molecule);
            // put it in the output slot
            if (molecule != null) {
                clearInput(true);
                this.getSlot(34).setStack(molecule.getItem().getDefaultStack());
            }
        } else {
            Scicraft.LOGGER.info("T - " + this.getPropertyDelegate(DelegateSettings.LCT_CRAFTING_PROGRESS));
        }
 */
    }
}
