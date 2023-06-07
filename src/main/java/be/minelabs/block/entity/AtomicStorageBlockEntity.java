package be.minelabs.block.entity;

import be.minelabs.inventory.AtomicInventory;
import be.minelabs.screen.AtomStorageScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class AtomicStorageBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    private AtomicInventory inventory = new AtomicInventory(512);

    public AtomicStorageBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.ATOMIC_STORAGE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new AtomStorageScreenHandler(syncId, playerInventory, inventory);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory.stacks);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.inventory = new AtomicInventory(512);
        Inventories.readNbt(nbt, inventory.stacks);
        super.readNbt(nbt);
    }
}
