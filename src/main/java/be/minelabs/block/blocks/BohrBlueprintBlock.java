package be.minelabs.block.blocks;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.entity.BohrBlueprintEntity;
import be.minelabs.entity.Entities;
import be.minelabs.item.items.AtomItem;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


@SuppressWarnings("deprecation")
public class BohrBlueprintBlock extends Block {
    private static final VoxelShape OUTLINE_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0, 3, 0, 16, 5, 16),        // top
            Block.createCuboidShape(-1, 1, -1, 17, 3, 17),      // middle
            Block.createCuboidShape(-2, 0, -2, 18, 1, 18)       // bottom
    );

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public enum Status implements StringIdentifiable {
        EMPTY,
        CRAFTABLE,
        UNSTABLE;

        @Override
        public String asString() {
            return toString().toLowerCase();
        }
    }

    public static final EnumProperty<Status> STATUS = EnumProperty.of("status", Status.class);

    public BohrBlueprintBlock() {
        super(FabricBlockSettings.of(new Material.Builder(MapColor.LAPIS_BLUE).blocksPistons().build()).requiresTool().strength(1f).nonOpaque());
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(STATUS, Status.EMPTY).with(FACING, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    /**
     * Instead of using a block entity, we have a regular entity for this block. Use this function to fetch it.
     */
    @Nullable
    public BohrBlueprintEntity getEntity(World world, BlockPos pos){
        return getEntity(world, pos, true);
    }

    /**
     * Also checks whether exactly one entity is found. If not removes the block.
     */
    @Nullable
    public BohrBlueprintEntity getEntity(World world, BlockPos pos, boolean removeBlock){
        // position of entity is center of bottom. It is placed one block above the bohr plate
        Box box = Box.from(Vec3d.of(pos)).contract(0.4, 0.4, 0.4).offset(0, 0.5, 0);
        List<BohrBlueprintEntity> entities = world.getEntitiesByType(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE, box, e -> true);
        if (entities.size() != 1){
            if (removeBlock) {
                Minelabs.LOGGER.warn("Expected one entity connected to bohr blueprint at " + pos + ", found: " + entities.size());
                Minelabs.LOGGER.warn("Removing the bohr blueprint");
                world.removeBlock(pos, false);
            }
            return null;
        }

        return entities.get(0);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATUS).add(FACING);
    }

    public static void updateStatus(World world, BlockPos pos, Status status){
        if (!world.getBlockState(pos).isOf(Blocks.BOHR_BLUEPRINT)) return;
        BlockState newState = world.getBlockState(pos).with(BohrBlueprintBlock.STATUS, status);
        world.setBlockState(pos, newState);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BohrBlueprintEntity entity = getEntity(world, pos);
        if (entity == null)
            return ActionResult.FAIL;

        // We don't know whether the action is successful on the client because it doesn't know the inventory of the entity.
        // Just return a success state for everything so the hand swing is performed, even if it didn't do anything, it will at least indicate that the block can be interacted with.
        if (world.isClient)
            return ActionResult.CONSUME_PARTIAL;

        ItemStack stack = player.getStackInHand(hand);

        // Stack not empty -> try to insert
        // Only Atoms can be inserted by right-clicking. Other elements need to be thrown.
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            if (item instanceof AtomItem && entity.addItem(item, (ServerPlayerEntity) player)) {
                if (!player.getAbilities().creativeMode)
                    stack.decrement(1);
                return ActionResult.SUCCESS;
            }
        }

        // Otherwise try to craft atom
        ItemStack resultStack = entity.craftAtom((ServerPlayerEntity) player);
        if (!resultStack.isEmpty()){
            player.getInventory().offerOrDrop(resultStack);
            return ActionResult.SUCCESS;
        }

        // We didn't do anything but still want the hand swing and prevent other actions from happening.
        // Returning FAIL will allow the item to perform an action still. For example blockitems will be placed on top which is annoying.
        return ActionResult.CONSUME_PARTIAL;
    }

    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        // While it contains content don't start breaking progress. Items are dropped in onBlockBreakStart.
        BohrBlueprintEntity entity = getEntity(player.world, pos);
        if (entity != null && !entity.isEmpty()){
            return 0;
        }

        // Otherwise break as usual
        return super.calcBlockBreakingDelta(state, player, world, pos);
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        super.onBlockBreakStart(state, world, pos, player);

        if (world.isClient)
            return;

        // While it contains content, drop them one by one. Block break progress is stopped in calcBlockBreakingDelta.
        BohrBlueprintEntity entity = getEntity(player.world, pos);
        if (entity != null && !entity.isEmpty()){
            ItemStack stack = entity.dropLastItem((ServerPlayerEntity) player);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);

        if (world.isClient)
            return;

        if (oldState.isOf(state.getBlock()))
            return;

        if (getEntity(world, pos, false) == null){
            BohrBlueprintEntity entity = new BohrBlueprintEntity(world, pos.up());
            world.spawnEntity(entity);
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);

        if (state.isOf(newState.getBlock()))
            return;

        // block was removed, also remove entity
        BohrBlueprintEntity entity = getEntity(world, pos);
        if (entity != null)
            entity.discard();
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        if (!world.getBlockState(blockPos).canReplace(ctx) || !world.getWorldBorder().contains(blockPos)) {
            return null;
        }
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }
}

