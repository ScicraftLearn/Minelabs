package be.minelabs.block.entity;

import be.minelabs.inventory.SidedSimpleInventory;
import be.minelabs.item.IMoleculeItem;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MologramBlockEntity extends BlockEntity implements SidedStorageBlockEntity {
    private final SimpleInventory inventory = new SimpleInventory(1){
        @Override
        public int getMaxCountPerStack() {
            return 1;
        }

        @Override
        public boolean isValid(int slot, ItemStack stack) {
            // Only molecule items allowed
            return stack.getItem() instanceof IMoleculeItem;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            // No stacking allowed
            if (!getStack(0).isEmpty())
                return false;
            if (!isValid(0, stack))
                return false;

            return super.canInsert(stack);
        }
    };

    public MologramBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.MOLOGRAM_BLOCK_ENTITY, pos, state);
    }

    public SimpleInventory getInventory(){
        return inventory;
    }

    public ItemStack getContents(){
        return inventory.getStack(0);
    }

    public void setContents(ItemStack stack){
        inventory.setStack(0, stack);
    }

    @Override
    public @Nullable Storage<ItemVariant> getItemStorage(Direction side) {
        return InventoryStorage.of(inventory, null);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, MologramBlockEntity entity) {
        ItemStack stack = entity.getContents();
        if (!world.isClient) {
            // SERVER
            ServerWorld serverWorld = (ServerWorld) world;
            if (state.get(Properties.LIT) && stack.isEmpty()) { // hopper extracted
                world.setBlockState(blockPos, state.with(Properties.LIT, false));
                serverWorld.getChunkManager().markForUpdate(blockPos);
                world.updateListeners(blockPos, state,
                        state.with(Properties.LIT, false), Block.NOTIFY_LISTENERS);
            } else if (!state.get(Properties.LIT) && !stack.isEmpty()) {// hopper inserted
                world.setBlockState(blockPos, state.with(Properties.LIT, true));
                serverWorld.getChunkManager().markForUpdate(blockPos);
                world.updateListeners(blockPos, state,
                        state.with(Properties.LIT, true), Block.NOTIFY_LISTENERS);
            }
        } else {
            //CLIENT
            if (!state.get(Properties.LIT) && !stack.isEmpty()) {
                entity.setContents(ItemStack.EMPTY);
            }
        }
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

    //sync data server client:
    //Warning: Need to call world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS); to trigger the update. //
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

}