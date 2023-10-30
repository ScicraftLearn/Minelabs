package be.minelabs.screen;

import be.minelabs.screen.slot.CraftingResultSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

public class AutomatedLaserScreenHandler extends ScreenHandler {
    private final PropertyDelegate propertyDelegate;

    private final Inventory inventory;

    public AutomatedLaserScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new SimpleInventory(6), new ArrayPropertyDelegate(2));
    }

    public AutomatedLaserScreenHandler(int syncId, @NotNull PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ScreenHandlers.AUTOMATED_LASER_SCREEN_HANDLER, syncId);
        this.propertyDelegate = propertyDelegate;

        this.inventory = inventory;
        this.inventory.onOpen(playerInventory.player);
        addProperties(propertyDelegate);

        addSlots();

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
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
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    private void addSlots() {
        this.addSlot(new Slot(inventory, 0, 80, 18));

        for (int i = 0; i < 5; i++) {
            this.addSlot(new CraftingResultSlot(inventory, i + 1, 45 + i * 18, 78));
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 118 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 176));
        }
    }

    public int getScaledProgress() {
        int progress = propertyDelegate.get(0);
        int max_progress = propertyDelegate.get(1);
        int size = 32;
        return max_progress != 0 && progress != 0 ? progress * size / max_progress : 0;
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }
}
