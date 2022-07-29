package be.uantwerpen.scicraft.gui;

import be.uantwerpen.scicraft.block.entity.LewisBlockEntity;
import be.uantwerpen.scicraft.inventory.slot.LewisCraftingResultSlot;
import be.uantwerpen.scicraft.inventory.slot.LewisErlenmeyerSlot;
import be.uantwerpen.scicraft.inventory.slot.LewisGridSlot;
import be.uantwerpen.scicraft.item.AtomItem;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.lewisrecipes.*;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LewisBlockScreenHandler extends ScreenHandler {

    private final ScreenHandlerContext context;
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private LewisBlockEntity lewis;
    private Molecule currentMolecule;
    private final LewisCraftingResultSlot outputSlot;
    private final LewisErlenmeyerSlot erlenmeyerSlot;


    /**
     * This constructor gets called on the client when the server wants it to open the screenHandler<br>
     * The client will call the other constructor with an empty Inventory and the screenHandler will automatically
     * sync this empty inventory with the inventory on the server.
     *
     * @param syncId
     * @param playerInventory
     * @param buf
     */
    public LewisBlockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY, new SimpleInventory(36), new ArrayPropertyDelegate(2), buf.readBlockPos());
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
    public LewisBlockScreenHandler(int syncId, @NotNull PlayerInventory playerInventory, ScreenHandlerContext context, Inventory inventory, PropertyDelegate propertyDelegate, BlockPos pos) {
        super(Screens.LEWIS_SCREEN_HANDLER, syncId);
        checkSize(inventory, 36);
        this.context = context;
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        BlockEntity be = playerInventory.player.world.getBlockEntity(pos);
        if (be instanceof LewisBlockEntity lewis) {
            this.lewis = lewis;
        }

        //Register properties for syncing
        this.addProperties(propertyDelegate);

        //Register slot listener, to reset the recipe if the items change
        this.addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                if (slotId < 25) {
                    lewis.resetRecipe();
                }
                handler.onContentChanged(inventory);
            }

            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                //maybe use later
                handler.updateToClient();
            }
        });

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
                this.addSlot(new LewisGridSlot(inventory, l + m * 5, 8 + l * 18, m * 18 - o) {
                    @Override
                    public boolean isLocked() {
                        return !isInputEmpty();
                    }
                });
            }
        }
        // Lewis Crafting Table Inventory (9 input slots)
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(inventory, m + 25, 8 + m * 18, 5 * 18 - o + 5) {
                @Override
                public boolean isEnabled() {
                    return getDensity() > 0;
                }

                @Override
                public int getMaxItemCount(ItemStack stack) {
                    if (lewis.getIngredients().size() > this.getIndex()-25) {
                        return getDensity();
                    }
                    return 0;
                }

                @Override
                public boolean canInsert(ItemStack stack) {
                    if (getIngredients().size() > this.getIndex()-25) {
                        return getIngredients().get(this.getIndex()-25).test(stack);
                    }
                    return false;
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
        if (invSlot < 25) {
            return ItemStack.EMPTY;
        }
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
     * @return Returns true if the input slots are empty
     */
    protected boolean isInputEmpty() {
        for (int i = 0; i < 9; i++) {
            if (!this.getSlot(i + 25).getStack().isEmpty()) return false;
        }
        return true;
    }

    public int getProgress() {
        return propertyDelegate.get(0);
    }

    public DefaultedList<Ingredient> getIngredients() {
        return lewis.getIngredients();
    }

    public int getDensity() {
        return propertyDelegate.get(1);
    }

    public LewisCraftingGrid getLewisCraftingGrid() {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            stacks.add(inventory.getStack(i));
        }
//        Scicraft.LOGGER.info("stacks: " + stacks);

        return new LewisCraftingGrid(stacks.toArray(new ItemStack[0]));
    }
}
