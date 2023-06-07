package be.minelabs.screen;

import be.minelabs.item.Items;
import be.minelabs.screen.slot.FilteredSlot;
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

        addFilterdSlots();
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

    private void addFilterdSlots(){
        this.addSlot(new FilteredSlot(inventory, 0, -77, -47, itemStack -> itemStack.getItem() == Items.ATOMS.get(0)));
        this.addSlot(new FilteredSlot(inventory, 1, 237, -47, itemStack -> itemStack.getItem() == Items.ATOMS.get(1)));

        int index = 2;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 18; j++) {
                int offset = -77;

                if (j > 1 && j <= 16){
                    offset += 4;
                } else if (j > 16) {
                    offset += 8;
                }
                if (i < 2 && j > 1 && j < 12)
                    continue;

                int finalIndex = index;
                this.addSlot(new FilteredSlot(inventory, index, offset + j * 18, -29 + i * 18, itemStack -> itemStack.getItem() == Items.ATOMS.get(finalIndex)));

                if (index == 56 || index == 88){
                    for (int k = 0; k < 14; k++) {
                        index++;
                        int finalIndex1 = index;
                        this.addSlot(new FilteredSlot(inventory, index, -37 + k * 18, index < 88 ? 83 : 101, itemStack -> itemStack.getItem() == Items.ATOMS.get(finalIndex1)));
                    }
                }
                index++;
            }
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, -1 + l * 18, 138 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, -1 + i * 18, 196));
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

}
