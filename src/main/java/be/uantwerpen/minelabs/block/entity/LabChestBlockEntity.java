package be.uantwerpen.minelabs.block.entity;

import be.uantwerpen.minelabs.gui.lab_chest_gui.LabChestScreenHandler;
import be.uantwerpen.minelabs.inventory.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestLidAnimator;
import net.minecraft.block.entity.LidOpenable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class LabChestBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory, LidOpenable {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(21, ItemStack.EMPTY);
    protected final PropertyDelegate propertyDelegate = new ArrayPropertyDelegate(0);
    private final ChestLidAnimator lidAnimator;

    public LabChestBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LAB_CHEST_BLOCK_ENTITY, pos, state);
        lidAnimator = new ChestLidAnimator();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new LabChestScreenHandler(syncId, inv, this, propertyDelegate);
    }

    @Override
    public float getAnimationProgress(float tickDelta) {
        return this.lidAnimator.getProgress(tickDelta);
    }
}
