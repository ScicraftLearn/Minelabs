package be.uantwerpen.minelabs.block.entity;

import be.uantwerpen.minelabs.gui.lab_chest_gui.LabChestScreenHandler;
import be.uantwerpen.minelabs.inventory.ImplementedInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class LabChestBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory, IAnimatable {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(21, ItemStack.EMPTY);
    protected final PropertyDelegate propertyDelegate = new ArrayPropertyDelegate(0);
    private final AnimationFactory factory = new AnimationFactory(this);
    private final ViewerCountManager viewerCount;
    private Stages stage;

    public LabChestBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LAB_CHEST_BLOCK_ENTITY, pos, state);
        this.viewerCount = new ViewerCountManager() {
            @Override
            protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
                stage = Stages.OPENING;
                // Play open sound
            }

            @Override
            protected void onContainerClose(World world, BlockPos pos, BlockState state) {
                stage = Stages.CLOSING;
                // Play close sound
            }

            @Override
            protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
                onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
            }

            @Override
            protected boolean isPlayerViewing(PlayerEntity player) {
                if (!(player.currentScreenHandler instanceof GenericContainerScreenHandler)) {
                    return false;
                } else {
                    Inventory inventory = ((GenericContainerScreenHandler) player.currentScreenHandler).getInventory();
                    return inventory == LabChestBlockEntity.this;
                }
            }
        };
        stage = Stages.IDLE;
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

    public void tick() {
        //blockEntity.updateAnimation(world, pos, state);
        if (!removed) {
            this.viewerCount.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.viewerCount.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        //TODO WHY IS IT NOT CALLED
        if (!this.removed && !player.isSpectator()) {
            this.viewerCount.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<LabChestBlockEntity>
                (this, "controllerOpen", 0, this::predicateOpen));
        animationData.addAnimationController(new AnimationController<LabChestBlockEntity>
                (this, "controllerClose", 0, this::predicateClose));

    }

    //TODO TRIGGERS/EVENTS
    private <E extends IAnimatable> PlayState predicateOpen(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("open"));
        if (stage == Stages.OPENING) {
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateClose(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("close"));
        if (stage == Stages.CLOSING) {
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected void onInvOpenOrClose(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        Block block = state.getBlock();
        world.addSyncedBlockEvent(pos, block, 1, newViewerCount);
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (type == 1) {
            return true;
        } else {
            return super.onSyncedBlockEvent(type, data);
        }
    }

    public enum Stages {
        IDLE,
        OPENING,
        CLOSING;
    }
}
