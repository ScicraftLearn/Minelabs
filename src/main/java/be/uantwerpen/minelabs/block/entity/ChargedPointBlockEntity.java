package be.uantwerpen.minelabs.block.entity;

import be.uantwerpen.minelabs.gui.charged_point_gui.ChargedPointBlockScreenHandler;
import be.uantwerpen.minelabs.inventory.ImplementedInventory;
import be.uantwerpen.minelabs.util.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ChargedPointBlockEntity extends ChargedBlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public ChargedPointBlockEntity(BlockPos pos, BlockState state,
                                   double charge,
                                   Block anit_block,
                                   double decay_time,
                                   ArrayList<ItemStack> decay_drop,
                                   Block decay_replace) {
        super(BlockEntities.CHARGED_POINT_BLOCK_ENTITY, pos, state, charge, anit_block, decay_time, decay_drop, decay_replace);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        Inventories.writeNbt(tag, inventory);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        Inventories.readNbt(tag, inventory);
        super.readNbt(tag);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    //BLOCK HOPPER
    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        return false;
    }

    //BLOCK HOPPER
    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        return false;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ChargedPointBlockScreenHandler(syncId, inv, this);
    }

    public void setInventory(ItemStack inventoryIn) {
        inventory.set(0, inventoryIn);
        inventoryChanged();
    }

    @Override
    public ItemStack getInventory() {
        return inventory.get(0);
    }

    private void inventoryChanged() {
        if (!inventory.get(0).isEmpty()) {
            int count = inventory.get(0).getCount();
            if (inventory.get(0).isIn(Tags.Items.POSITIVE_CHARGE)) {
                this.charge = count;
            } else if (inventory.get(0).isIn(Tags.Items.NEGATIVE_CHARGE)) {
                this.charge = -count;
            } else {
                this.charge = 0;
            }
        } else {
            this.charge = 0;
        }
        this.updateField(world, pos);
        needsUpdate(true);
    }

    @Override
    public void onClose(PlayerEntity player) {
        inventoryChanged();
    }
}
