package be.minelabs.screen;

import be.minelabs.item.Items;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

public class AtomStorageScreenHandler extends ScreenHandler {

    private final Inventory inventory;

    public AtomStorageScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new SimpleInventory(Items.ATOMS.size()));
    }

    public AtomStorageScreenHandler(int syncId, @NotNull PlayerInventory playerInventory, Inventory inventory) {
        super(ScreenHandlers.ATOM_STORAGE_SCREEN_HANDLER, syncId);
        checkSize(inventory, Items.ATOMS.size());
        this.inventory = inventory;
        this.inventory.onOpen(playerInventory.player);

        this.addSlot(new Slot(inventory, 0, 8, -10));
        this.addSlot(new Slot(inventory, 1, 152, -10));

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (j==2) // skipping a column
                    continue;
                this.addSlot(new Slot(inventory, 2+j+i*8, 8 + j * 18, 8 + i * 18));
            }
        }

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
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        inventory.onClose(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 112 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 170));
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

}
