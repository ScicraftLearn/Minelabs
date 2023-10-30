package be.minelabs.block.entity;

import be.minelabs.item.Items;
import be.minelabs.recipe.laser.LaserInventory;
import be.minelabs.recipe.laser.LaserRecipe;
import be.minelabs.screen.AutomatedLaserScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AutomatedLaserBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    private final PropertyDelegate propertyDelegate;

    private final LaserInventory inventory = new LaserInventory();

    private int progress = 0;
    private int max_progress = 28;

    public AutomatedLaserBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.AUTOMATED_LASER_BLOCK_ENTITY, pos, state);
        propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AutomatedLaserBlockEntity.this.progress;
                    case 1 -> AutomatedLaserBlockEntity.this.max_progress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AutomatedLaserBlockEntity.this.progress = value;
                    case 1 -> AutomatedLaserBlockEntity.this.max_progress = value;
                }
                ;
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.progress = nbt.getInt("progress");
        this.max_progress = nbt.getInt("max_progress");
        Inventories.readNbt(nbt, inventory.stacks);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("progress", progress);
        nbt.putInt("max_progress", max_progress);
        Inventories.writeNbt(nbt, inventory.stacks);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new AutomatedLaserScreenHandler(syncId, playerInventory, inventory, propertyDelegate);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        //todo
        if (canOutput() && hasRecipe()) {
            this.progress++;
            markDirty(world, pos, state);

            if (progress >= max_progress) {
                craftItem();
                resetProgress();
            }

        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private boolean canOutput() {
        return true;
    }

    public boolean hasRecipe() {
        Optional<LaserRecipe> recipe = getCurrentRecipe();
        return !inventory.getInputStack().isEmpty();
    }

    private Optional<LaserRecipe> getCurrentRecipe() {
        return world.getRecipeManager().getFirstMatch(LaserRecipe.Type.INSTANCE, inventory, this.getWorld());
    }

    private void craftItem() {
        inventory.removeStack(0, 1);
        inventory.setStack(1, new ItemStack(Items.ATOMS.get(2)));
        // TODO output(s)
    }

}
