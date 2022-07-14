package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.gui.MologramBlockScreenHandler;
import be.uantwerpen.scicraft.inventory.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class MologramBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int rot_x = 0;
    private int rot_y = 0;
    private int rot_z = 0;
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            if (index == 0) {
                return rot_x;
            } else if (index == 1) {
                return rot_y;
            } else if (index == 2) {
                return rot_z;
            }

            //the code may never reach this point
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                rot_x = value;
            } else if (index == 1) {
                rot_y = value;
            } else if (index == 2) {
                rot_z = value;
            }
        }

        //this is supposed to return the amount of integers you have in your delegate
        @Override
        public int size() {
            return 3;
        }
    };
    public MologramBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.MOLOGRAM_BLOCK_ENTITY, pos, state);
    }
    // Store the current value of the number

    // Serialize the BlockEntity
    @Override
    public void writeNbt(NbtCompound tag) { //should this be void? or not? https://fabricmc.net/wiki/tutorial:inventory
        // Save the current value of the number to the tag
        tag.putInt("rotate_x", rot_x);
        tag.putInt("rotate_y", rot_y);
        tag.putInt("rotate_z", rot_z);
        Inventories.writeNbt(tag, items);

        super.writeNbt(tag);
    }
    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        rot_x = tag.getInt("rotate_x");
        rot_y = tag.getInt("rotate_y");
        rot_z = tag.getInt("rotate_z");
        Inventories.readNbt(tag, items);
    }

    //sync data server client:
    //Warning: Need to call world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS); to trigger the update. //
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
//

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new MologramBlockScreenHandler(syncId, inv, this, propertyDelegate );
    }


}

