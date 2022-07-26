package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.item.inventory.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PortalBlockEntity extends BlockEntity implements NamedScreenHandlerFactory , ImplementedInventory {
    private final DefaultedList<ItemStack> inventory =
    DefaultedList.ofSize(4,ItemStack.EMPTY);

    public PortalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.PORTAL_BLOCK,pos, state);
    }

    public static void tick(World world,BlockPos pos,BlockState state,PortalBlockEntity entity){
        System.out.println("big iron");
    }

    @Override
    public Text getDisplayName() {
        return Text.of("Atomic Portal");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return null;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    //Reading inventory
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt,inventory);
    }
    //Writing inventory
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt,inventory);
    }
}
