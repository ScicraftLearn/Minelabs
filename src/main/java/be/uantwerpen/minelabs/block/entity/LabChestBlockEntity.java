package be.uantwerpen.minelabs.block.entity;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.gui.lab_chest_gui.LabChestScreenHandler;
import be.uantwerpen.minelabs.inventory.ImplementedInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.*;
import net.minecraft.sound.SoundEvents;
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

    private boolean open = false;

    private final ViewerCountManager stateManager = new ViewerCountManager(){

        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
//            ChestBlockEntity.playSound(world, pos, state, SoundEvents.BLOCK_CHEST_OPEN);
        }

        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
//            ChestBlockEntity.playSound(world, pos, state, SoundEvents.BLOCK_CHEST_CLOSE);
        }

        @Override
        protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
            LabChestBlockEntity.this.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
        }

        @Override
        protected boolean isPlayerViewing(PlayerEntity player) {
            if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
                Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
                return inventory == LabChestBlockEntity.this || inventory instanceof DoubleInventory && ((DoubleInventory)inventory).isPart(LabChestBlockEntity.this);
            }
            return false;
        }
    };

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        Minelabs.LOGGER.info("viewcount: " + Integer.toString(data));
        if (type == 1) {
            this.open = (data > 0);
            return true;
        }
        return super.onSyncedBlockEvent(type, data);
    }

    public LabChestBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LAB_CHEST_BLOCK_ENTITY, pos, state);
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

    public static void tick(World world, BlockPos pos, BlockState state) {

    }

    protected void onInvOpenOrClose(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        Block block = state.getBlock();
        world.addSyncedBlockEvent(pos, block, 1, newViewerCount);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<LabChestBlockEntity>
                (this, "controllerOpen", 20, this::predicateOpen));
        animationData.addAnimationController(new AnimationController<LabChestBlockEntity>
                (this, "controllerClose", 20, this::predicateClose));
    }

    //TODO TRIGGERS/EVENTS
    private <E extends IAnimatable> PlayState predicateOpen(AnimationEvent<E> event) {
        if (this.open){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("open"));
            return PlayState.CONTINUE;
        }else{
            event.getController().markNeedsReload();
            return PlayState.STOP;
        }
    }

    private <E extends IAnimatable> PlayState predicateClose(AnimationEvent<E> event) {
        if (!this.open){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("close"));
            return PlayState.CONTINUE;
        }else{
            event.getController().markNeedsReload();
            return PlayState.STOP;
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
