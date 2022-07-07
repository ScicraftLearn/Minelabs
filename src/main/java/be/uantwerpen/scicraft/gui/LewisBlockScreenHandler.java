package be.uantwerpen.scicraft.gui;

import be.uantwerpen.scicraft.lewisrecipes.Atom;
import be.uantwerpen.scicraft.lewisrecipes.Molecule;
import be.uantwerpen.scicraft.lewisrecipes.RecipeManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LewisBlockScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    PropertyDelegate propertyDelegate;


    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public LewisBlockScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(36), new ArrayPropertyDelegate(1));
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
        int o = 11-29;

        // Lewis Crafting Table Inventory (5x5 grid)
        for (m = 0; m < 5; ++m) {
            for (l = 0; l < 5; ++l) {
                this.addSlot(new LewisGridSlot(inventory, l + m * 5, 8 + l * 18,  m * 18-o));
            }
        }
        // Lewis Crafting Table Inventory (9 input slots)
        for (m = 0; m < 9; ++m) {
            this.addSlot(new LewisInputSlot(inventory, m + 25, 8 + m * 18,5 * 18-o+5) {
                @Override
                public boolean isEnabled() {
                    return propertyDelegate.get(0) == 1;
                }
            });
        }

        // Lewis Crafting Table Inventory (1 output slot)
        this.addSlot(new LewisCraftingResultSlot(inventory, 34, 8 + 7 * 18, 2 * 18-o) {
            @Override
            public boolean isEnabled() {
                return propertyDelegate.get(0) == 1;
            }
        });

        // Lewis Crafting Table Inventory (1 slot for erlenmeyer)
        this.addSlot(new LewisErlenmeyerSlot(inventory, 35, 8 + 7 * 18 - 27, 2 * 18-o + 18) {
            @Override
            public boolean isEnabled() {
                return propertyDelegate.get(0) == 1;
            }
        });

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

    public int getPropertyDelegate(){
        return propertyDelegate.get(0);
    }

    public void setPropertyDelegate(int a) {
        propertyDelegate.set(0, a);
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

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);

        /*boolean on = false;
        for (int i = 0; i < 25; i++) {
            if(this.inventory.getStack(i).toString().equals("1 air")) {
                continue;
            }
            on = true;
        }

        if(on) {
            setPropertyDelegate(1);
        } else {
            setPropertyDelegate(0);
        }

        // TEMPORARY!!!!

        if (true) return;*/

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

        Atom[] atoms = new Atom[25];
        for (int i = 0; i < 25; i++) {
            atoms[i] = Atom.getByItem(this.inventory.getStack(i).getItem());
        }

        Map<Atom, Integer> ingredients = new HashMap<>();
        for (Atom atom : atoms)
            if (atom != null)
                ingredients.put(atom, ingredients.getOrDefault(atom, 0) + 1);

        Molecule molecule = RecipeManager.getMolecule(ingredients);
        if (molecule == null) {
            if (isInputOpen()) closeInputSlots();
            return;
        }

        if (!RecipeManager.isCorrectMolecule(molecule, atoms)) {
            if (isInputOpen()) closeInputSlots();
            return;
        }

        if (!isInputOpen()) {
            System.out.println("Opening input");
            openInputSlots(molecule.getIngredients().values().stream().reduce(0, Integer::sum));
            // TODO: Fix Slot#getBackgroundSprite
        }

        if (hasCorrectInput(molecule)) {
            // TODO: if (arrow is niet bezig) -> start arrow
        } else {
            // TODO: if (arrow is bezig) -> stop arrow
        }
    }

    protected boolean hasCorrectInput(Molecule molecule) {
        List<Atom> ingredients = this.getIngredients(molecule);
        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack stack = inventory.getStack(i + 25);
            if (stack == null || stack.getItem().equals(Items.AIR)
                    || ingredients.get(i).getItem().equals(stack.getItem())
                    || stack.getCount() != 10)
                return false;
        }
        return true;
    }

    public List<Atom> getIngredients(@NotNull Molecule molecule) {
        List<Atom> ingr = new ArrayList<>();
        molecule.getIngredients().keySet().forEach(atom -> {
            for (int i = 0; i < molecule.getIngredients().get(atom); i++)
                ingr.add(atom);
        });
        return ingr;
    }

    protected void openGridSlots() {
        for (int i = 0; i < 25; i++) {
            ((LewisGridSlot) this.getSlot(i)).setValid(true);
        }
    }

    protected void openInputSlots(int amount) {
        setPropertyDelegate(1);
        for (int i = 25; i < 25+amount; i++) {
            ((LewisInputSlot) this.getSlot(i)).setValid(true);
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

    protected void closeGridSlots() {
        for (int i = 0; i < 25; i++) {
            ((LewisGridSlot) this.getSlot(i)).setValid(false);
        }
    }

    protected void closeInputSlots() {
        setPropertyDelegate(0);
        for (int i = 25; i < 34; i++) {
            ((LewisInputSlot) this.getSlot(i)).setValid(false);
            this.sendContentUpdates();
        }
        closeErlenmeyer();
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