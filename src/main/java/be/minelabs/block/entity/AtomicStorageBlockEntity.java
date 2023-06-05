package be.minelabs.block.entity;

import be.minelabs.inventory.OrderedInventory;
import be.minelabs.item.Items;
import be.minelabs.screen.AtomStorageScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class AtomicStorageBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {
    private OrderedInventory inventory = new OrderedInventory(Items.ATOMS.size());

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
        this.inventory = new OrderedInventory(Items.ATOMS.size());
        Inventories.readNbt(nbt, inventory.stacks);
        super.readNbt(nbt);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        // DO we need this ?
    }
}
