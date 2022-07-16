package be.uantwerpen.scicraft.gui;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.item.AtomItem;
import be.uantwerpen.scicraft.lewisrecipes.Atom;
import be.uantwerpen.scicraft.lewisrecipes.LewisCraftingGrid;
import be.uantwerpen.scicraft.lewisrecipes.Molecule;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class LewisBlockScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    PropertyDelegate propertyDelegate;
    private ItemStack output;

    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public LewisBlockScreenHandler(int syncId, PlayerInventory playerInventory) {
        //this(syncId, playerInventory, new SimpleInventory(35));
        this(syncId, playerInventory, new SimpleInventory(36), new ArrayPropertyDelegate(4));
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
                    return propertyDelegate.get(0) == 1;
                }
            });
        }

        // Lewis Crafting Table Inventory (1 output slot)
        this.addSlot(new LewisCraftingResultSlot(inventory, 34, 8 + 7 * 18, 2 * 18 - o) {
            @Override
            public boolean isEnabled() {
                return propertyDelegate.get(0) >= 0;
            }
        });

        // Lewis Crafting Table Inventory (1 slot for erlenmeyer)
        this.addSlot(new LewisErlenmeyerSlot(inventory, 35, 8 + 7 * 18 - 27, 2 * 18 - o + 36) {
            @Override
            public boolean isEnabled() {
                return propertyDelegate.get(0) >= 0;
            }
        });

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

    public void craftingAnimation(ItemStack itemStack) {
        System.out.println("starting");
        if (getPropertyDelegate(1) >= 0) {
            return;
        }
        setPropertyDelegate(1, 0);
        this.output = itemStack;
    }

    public void setOutput() {
        // if the crafting animation is over
        if (propertyDelegate.get(1) > 23) {
            // reset the crafting animation so it can start over later
            propertyDelegate.set(1, -1);
            inventory.setStack(34, output);
        }
    }

    @Override
    public void onSlotClick(int slotIndex, int button, @NotNull SlotActionType actionType, PlayerEntity player) {
        if (actionType.equals(SlotActionType.QUICK_CRAFT)) {
            this.endQuickCraft();
            inventory.markDirty();
        } else if (actionType.equals(SlotActionType.QUICK_MOVE)) {
            Scicraft.LOGGER.info("");
            if (slotIndex < 0) return;
            Slot slot = slots.get(slotIndex);
            if (!slot.canTakeItems(player)) return;

            for (int i = 25; i < inventory.size(); i++) {
                Slot inventorySlot = this.getSlot(i);
                if (!slot.isEnabled()) continue;
                if (inventorySlot instanceof LewisGridSlot) continue;
                if (inventorySlot instanceof LewisInputSlot inputSlot && slot.getStack().getItem() instanceof AtomItem) {
                    int insertCount = Math.min(inputSlot.canInsertCount(slot.getStack()), slot.getStack().getCount());
                    if (insertCount <= 0) continue;
                    inputSlot.getStack().setCount(inputSlot.getStack().getCount() + insertCount);
                    if (slot.getStack().getCount() <= insertCount) {
                        slot.setStack(Items.AIR.getDefaultStack());
                        break;
                    } else {
                        slot.getStack().setCount(slot.getStack().getCount() - insertCount);
                        continue;
                    }
                }
                if (inventorySlot instanceof LewisErlenmeyerSlot erlenmeyerSlot && slot.getStack().getItem().equals(be.uantwerpen.scicraft.item.Items.ERLENMEYER)) {
                    int insertCount = Math.min(slot.getStack().getCount(), erlenmeyerSlot.getStack().getCount() >= erlenmeyerSlot.getMaxItemCount(slot.getStack())
                            ? 0 : erlenmeyerSlot.getMaxItemCount(slot.getStack()) - erlenmeyerSlot.getStack().getCount());
                    if (insertCount <= 0) continue;
                    if (erlenmeyerSlot.getStack().isEmpty()) {
                        erlenmeyerSlot.setStack(slot.getStack().copy());
                        erlenmeyerSlot.getStack().setCount(insertCount);
                    } else {
                        erlenmeyerSlot.getStack().setCount(erlenmeyerSlot.getStack().getCount() + insertCount);
                    }
                    if (slot.getStack().getCount() <= insertCount) {
                        slot.setStack(Items.AIR.getDefaultStack());
                        break;
                    } else {
                        slot.getStack().setCount(slot.getStack().getCount() - insertCount);
                        //continue;
                    }
                }
            }
        } else if (slotIndex < 25 && actionType == SlotActionType.PICKUP) {
            if (slotIndex < 0) return;
            Slot slot = this.slots.get(slotIndex);
            if (this.getCursorStack().isEmpty())
                this.getSlot(slotIndex).setStack(Items.AIR.getDefaultStack());
            else if (slot instanceof LewisGridSlot)
                slot.canInsert(getCursorStack());
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

        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            stacks.add(inventory.getStack(i));
        }
        Scicraft.LOGGER.info(stacks);

        Scicraft.LOGGER.info("atoms: " + Arrays.deepToString(atoms));
        Scicraft.LOGGER.info("ingredients: " + ingredients);

        LewisCraftingGrid grid = new LewisCraftingGrid(stacks.toArray(new ItemStack[0]));
        grid.markDirty();


        // TODO update to recipeManager
//        Molecule molecule = RecipeManager.getMolecule(ingredients);
//        Scicraft.LOGGER.info("molecule: " + molecule);
//        if (molecule == null) {
//            if (isInputOpen()) closeInputSlots();
//            return;
//        }
//
//        if (!RecipeManager.isCorrectMolecule(molecule, atoms)) {
//            if (isInputOpen()) closeInputSlots();
//            return;
//        }
//
//        if (!isInputOpen()) {
//            System.out.println("Opening input");
//            openInputSlots(molecule.getIngredients().values().stream().reduce(0, Integer::sum));
//            // TODO: Fix Slot#getBackgroundSprite
//        }
//
//        if (hasCorrectInput(molecule)) {
//            if (this.getPropertyDelegate(1) == -1)
//                this.craftingAnimation(new ItemStack(molecule.getItem()));
//        } else {
//            //arrow is running but input is no longer valid
//            if (this.getPropertyDelegate(1) >= 0 && this.getPropertyDelegate(1) < 23)
//                //stop crafting animation
//                this.setPropertyDelegate(1, -1);
//        }
    }

    protected boolean hasCorrectInput(Molecule molecule) {
        List<Atom> ingredients = this.getIngredients(molecule);
        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack stack = inventory.getStack(i + 25);
            if (stack == null || stack.getItem().equals(net.minecraft.item.Items.AIR)
                    || !ingredients.get(i).getItem().equals(stack.getItem())
                    || stack.getCount() != 10)
                return false;
        }
        return true;
    }

    public List<Atom> getIngredients(@NotNull Molecule molecule) {
        return new ArrayList<>(molecule.getIngredients());
    }

    /**
     * code to open slots (by type)
     */
    protected void openGridSlots() {
        for (int i = 0; i < 25; i++) {
            ((LewisGridSlot) this.getSlot(i)).setLocked(false);
        }
    }

    protected void openInputSlots(int amount) {
        setPropertyDelegate(0, 1);
        for (int i = 25; i < 25 + amount; i++) {
            // ((LewisInputSlot) this.getSlot(i)).setValid(true); TODO: Fix this
            this.sendContentUpdates();
        }
        openErlenmeyer();
    }

    protected void openOutputSlot() {
        ((LewisCraftingResultSlot) this.getSlot(34)).setReady(true);
    }

    protected void openErlenmeyer() {
        ((LewisErlenmeyerSlot) this.getSlot(35)).setValid(true);
    }


    /**
     * code to close slots (by type)
     */
    protected void closeGridSlots() {
        for (int i = 0; i < 25; i++) {
            ((LewisGridSlot) this.getSlot(i)).setLocked(true);
        }
    }

    protected void closeInputSlots() {
        setPropertyDelegate(0, 0);
        for (int i = 25; i < 34; i++) {
            ((LewisInputSlot) this.getSlot(i)).setAllowedItem(null);
            this.sendContentUpdates();
        }
        //closeErlenmeyer();
    }

    protected void closeOutputSlot() {
        ((LewisCraftingResultSlot) this.getSlot(34)).setReady(false);
    }

    protected void closeErlenmeyer() {
        ((LewisErlenmeyerSlot) this.getSlot(35)).setValid(false);
    }

    protected boolean isInputOpen() {
        return this.getSlot(25).isEnabled();
    }
}