package be.uantwerpen.scicraft.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class AtomPortalScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    public AtomPortalScreenHandler(int syncId, PlayerInventory inv) {
        this(syncId,inv,new SimpleInventory(4));
    }
    public AtomPortalScreenHandler(int syncId, PlayerInventory inv, Inventory inventory) {
        super(ModScreenHandler.ATOM_TYPE_HANDLER(),syncId);
        checkSize(inventory,4);
        this.inventory=inventory;
        inventory.onOpen(inv.player);

        this.addSlot(new Slot(inventory,0,10,50));
        this.addSlot(new Slot(inventory,0,10,60));
        this.addSlot(new Slot(inventory,0,10,70));
        this.addSlot(new Slot(inventory,0,10,80));

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
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

    //Show player inventory on screen
    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    //Show player hotbar on screen
    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
