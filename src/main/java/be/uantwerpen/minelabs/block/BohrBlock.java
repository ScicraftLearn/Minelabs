package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.block.entity.BlockEntities;
import be.uantwerpen.minelabs.block.entity.BohrBlockEntity;
import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.entity.SubatomicParticle;
import be.uantwerpen.minelabs.item.AtomItem;
import be.uantwerpen.minelabs.item.ItemGroups;
import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.util.MinelabsProperties;
import be.uantwerpen.minelabs.util.NuclidesTable;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;


public class BohrBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    //    status: 0 = normal, 1 = atom collectible, 2 = atom unstable
    public static final IntProperty STATUS = MinelabsProperties.STATUS;

    public BohrBlock() {
        super(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1f).nonOpaque().luminance(100));
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(STATUS, 0).with(FACING, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof BohrBlockEntity bohrBlockEntity) {
            bohrBlockEntity.scatterParticles();
        }
        super.onBreak(world, pos, state, player);
        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, world.getBlockState(pos)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATUS).add(FACING);
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        super.onProjectileHit(world, state, hit, projectile);
        BlockEntity blockEntity = world.getBlockEntity(hit.getBlockPos());
        Item item;
        boolean changedState = false;
        if (!world.isClient()) {
            if (projectile instanceof SubatomicParticle subatomicParticle && blockEntity instanceof BohrBlockEntity bohrBlockEntity) {
                item = subatomicParticle.getStack().getItem();

                if (item == Items.ELECTRON || item == Items.NEUTRON || item == Items.PROTON) {
                    changedState = bohrBlockEntity.insertParticle(item) == ActionResult.SUCCESS;

                } else if (item == Items.ANTI_NEUTRON || item == Items.ANTI_PROTON || item == Items.POSITRON) {
                    changedState = bohrBlockEntity.removeParticle(item) == ActionResult.SUCCESS;
                }
                if (changedState) {

                    world.updateNeighbors(hit.getBlockPos(), be.uantwerpen.minelabs.block.Blocks.BOHR_BLOCK);
                    state.updateNeighbors(world, hit.getBlockPos(), Block.NOTIFY_ALL);
                    world.updateListeners(hit.getBlockPos(), state, state, Block.NOTIFY_LISTENERS);

                }
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BohrBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        ItemStack stack = player.getStackInHand(hand);
        Item item = stack.getItem();

        if (blockEntity instanceof BohrBlockEntity bohrBlockEntity) {
            if (item == Items.NEUTRON || item == Items.PROTON || item == Items.ELECTRON) {
                if (bohrBlockEntity.insertParticle(item) == ActionResult.SUCCESS) {
                    if (!player.getAbilities().creativeMode) {
                        player.getStackInHand(hand).decrement(1);
                    }
                }
            } else if (item == Items.ANTI_NEUTRON || item == Items.ANTI_PROTON || item == Items.POSITRON) {
                if (bohrBlockEntity.removeParticle(item) == ActionResult.SUCCESS) {
                    if (!player.getAbilities().creativeMode) {
                        player.getStackInHand(hand).decrement(1);
                    }
                }
            } else if (item.getGroup() == ItemGroups.ATOMS) {

                int protonAmount = ((AtomItem) item).getAtom().getAtomNumber();
                int neutronAmount = NuclidesTable.findNextStableAtom(protonAmount, true);

                boolean isInserted = false;
                for (int p = 0; p < protonAmount; p++) {
                    if (bohrBlockEntity.insertParticle(Items.PROTON) == ActionResult.SUCCESS) {
                        isInserted = true;
                    }
                    if (bohrBlockEntity.insertParticle(Items.ELECTRON) == ActionResult.SUCCESS) {
                        isInserted = true;
                    }
                }
                for (int n = 0; n < neutronAmount; n++) {
                    if (bohrBlockEntity.insertParticle(Items.NEUTRON) == ActionResult.SUCCESS) {
                        isInserted = true;
                    }
                }

                if (isInserted) {
                    if (!player.getAbilities().creativeMode) {
                        player.getStackInHand(hand).decrement(1);
                    }
                }

            } else if (stack.isEmpty()) {
                // creating the atom
                if (player.isSneaking()) {
                    bohrBlockEntity.createAtom(world, pos);
                }
                // empty the bohrblock
                else {
                    bohrBlockEntity.scatterParticles();
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(0, 0, 0, 16, 5, 16);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (world.isClient)
            return;

        BohrBlueprintEntity entity = new BohrBlueprintEntity(world, pos);
        entity.onPlace();
        world.emitGameEvent(placer, GameEvent.ENTITY_PLACE, entity.getPos());
        world.spawnEntity(entity);
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

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BlockEntities.BOHR_BLOCK_ENTITY, BohrBlockEntity::tick);
    }

}

