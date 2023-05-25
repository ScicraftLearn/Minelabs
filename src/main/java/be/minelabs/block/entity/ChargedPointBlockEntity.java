package be.minelabs.block.entity;

import be.minelabs.screen.ChargedPointBlockScreenHandler;
import be.minelabs.util.Tags;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ChargedPointBlockEntity extends ChargedBlockEntity implements NamedScreenHandlerFactory, SidedStorageBlockEntity {

//    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private final SimpleInventory inventory = new SimpleInventory(1);

    public ChargedPointBlockEntity(BlockPos pos, BlockState state,
                                   double charge,
                                   Block anit_block,
                                   double decay_time,
                                   ArrayList<ItemStack> decay_drop,
                                   Block decay_replace) {
        super(BlockEntities.CHARGED_POINT_BLOCK_ENTITY, pos, state, charge, anit_block, decay_time, decay_drop, decay_replace);
        inventory.addListener(this::inventoryChanged);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.put("Content", inventory.toNbtList());
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        if (tag.contains("Content"))
            inventory.readNbtList(tag.getList("Content", NbtElement.COMPOUND_TYPE));
    }

    public Inventory getInventory(){
        return inventory;
    }

    @Nullable
    @Override
    public Storage<ItemVariant> getItemStorage(Direction side) {
        return InventoryStorage.of(inventory, null);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ChargedPointBlockScreenHandler(syncId, inv, inventory);
    }

    /**
     * Sets the inventory content. Discards any already existing inventory.
     */
    public void setContents(ItemStack inventoryIn) {
        inventory.setStack(0, inventoryIn);
    }

    @Override
    public ItemStack getContents() {
        return inventory.getStack(0);
    }

    private void inventoryChanged(Inventory inventory) {
        ItemStack stack = inventory.getStack(0);
        if (!stack.isEmpty()) {
            int count = stack.getCount();
            if (stack.isIn(Tags.Items.POSITIVE_CHARGE)) {
                this.charge = count;
            } else if (stack.isIn(Tags.Items.NEGATIVE_CHARGE)) {
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
}
