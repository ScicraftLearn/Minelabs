package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.entity.Entities;
import be.uantwerpen.minelabs.item.AtomItem;
import be.uantwerpen.minelabs.item.ItemGroups;
import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.util.MinelabsProperties;
import be.uantwerpen.minelabs.util.NuclidesTable;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class BohrBlock extends Block {
    private static final VoxelShape OUTLINE_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 5, 16);

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    //    status: 0 = normal, 1 = atom collectible, 2 = atom unstable
    public static final IntProperty STATUS = MinelabsProperties.STATUS;

    public BohrBlock() {
        super(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1f).nonOpaque().luminance(100));
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(STATUS, 0).with(FACING, Direction.NORTH));
    }

    /**
     * Instead of using a block entity, we have a regular entity for this block. Use this function to fetch it.
     */
    @Nullable
    private BohrBlueprintEntity getEntity(World world, BlockPos pos){
        List<BohrBlueprintEntity> entities = world.getEntitiesByType(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE, Box.from(Vec3d.of(pos.up())), e -> true);
        if (entities.size() != 1){
            Minelabs.LOGGER.warn("Expected one entity connected to bohr plate at " + pos + ", found: " + entities.size());
            return null;
        }

        return entities.get(0);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATUS).add(FACING);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        // TODO: client/server checking

        BohrBlueprintEntity entity = getEntity(world, pos);
        if (entity == null)
            return ActionResult.FAIL;

        ItemStack stack = player.getStackInHand(hand);
        if (stack.isEmpty()) {
            if (!player.isSneaking()) {
                entity.dropContents();
                return ActionResult.SUCCESS;
            }

            // try crafting atom
            ItemStack resultStack = entity.craftAtom();
            if (resultStack.isEmpty()){
                return ActionResult.FAIL;
            }
            player.getInventory().offerOrDrop(resultStack);
            return ActionResult.SUCCESS;
        }

        // stack not empty
        Item item = stack.getItem();
        if (entity.addItem(item)){
            if (!player.getAbilities().creativeMode)
                stack.decrement(1);
            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (world.isClient)
            return;

        BohrBlueprintEntity entity = new BohrBlueprintEntity(world, pos.up());
        world.emitGameEvent(placer, GameEvent.ENTITY_PLACE, entity.getPos());
        world.spawnEntity(entity);
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
        return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }
}

