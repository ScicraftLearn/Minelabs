package be.minelabs.block.entity;

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
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
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
        if (canOutput() && hasRecipe(world.getRegistryManager())) {
            this.progress++;
            markDirty(world, pos, state);

            if (progress >= max_progress) {
                craftItem(world);
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
        boolean empty = true;
        boolean max_count = true;
        for (int i = 1; i < 5; i++) {
            if (!inventory.getOutputStack(i).isEmpty()) {
                empty = false;
            }
            if (inventory.getOutputStack(i).getCount() >= inventory.getMaxCountPerStack()) {
                max_count = false;
            }
        }
        return empty || max_count;
    }

    private boolean hasRecipe(DynamicRegistryManager manager) {
        Optional<LaserRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        DefaultedList<ItemStack> stacks = recipe.get().getOutputs(manager);
        return canExport(stacks);
    }

    private boolean canExport(DefaultedList<ItemStack> stacks) {
        return inventory.isOutputEmpty() || canExportAmount(stacks);
    }

    private boolean canExportAmount(DefaultedList<ItemStack> stacks) {
        for (int i = 0; i < 5; i++) {
            if (stacks.isEmpty()) {
                continue;
            }
            if (!inventory.getOutputStack(i + 1).isOf(stacks.get(i).getItem()) && !inventory.getOutputStack(i + 1).isEmpty()) {
                return false;
            }

            if (inventory.getOutputStack(i + 1).getCount() + stacks.get(i).getCount() > inventory.getMaxCountPerStack()) {
                return false;
            }
        }

        return true;
    }


    private Optional<LaserRecipe> getCurrentRecipe() {
        return world.getRecipeManager().getFirstMatch(LaserRecipe.Type.INSTANCE, inventory, this.getWorld());
    }

    private void craftItem(World world) {
        LaserRecipe recipe = getCurrentRecipe().orElseThrow();
        inventory.removeStack(0, 1);

        DefaultedList<ItemStack> stacks = recipe.getOutput(world.getRandom()); // calculated random stacks
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i).isEmpty()) {
                continue;
            }
            inventory.setStack(i + 1, new ItemStack(stacks.get(i).getItem(),
                    inventory.getOutputStack(i + 1).getCount() + stacks.get(i).getCount()));
        }
    }

}
