package be.minelabs.block.entity;

import be.minelabs.recipe.laser.LaserInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AutomatedLaserBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    private final PropertyDelegate propertyDelegate;

    private final LaserInventory inventory = new LaserInventory();

    private int progress;
    private int max_progress;

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
                };
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }


    public void tick(World world, BlockPos pos, BlockState state) {
        //todo
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
        return null;
    }
}
