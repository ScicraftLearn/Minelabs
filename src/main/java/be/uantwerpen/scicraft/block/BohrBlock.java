package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.entity.BohrBlockEntity;
import be.uantwerpen.scicraft.entity.SubatomicParticle;
import be.uantwerpen.scicraft.item.AtomItem;
import be.uantwerpen.scicraft.item.ItemGroups;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.util.NucleusState;
import be.uantwerpen.scicraft.util.NuclidesTable;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.List;

import static be.uantwerpen.scicraft.block.entity.BohrBlockEntity.*;


public class BohrBlock extends BlockWithEntity implements BlockEntityProvider {
    VoxelShape shape = Block.createCuboidShape(0, 0, 0, 16, 1, 16);


    public BohrBlock() {
        super(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1f).nonOpaque().luminance(100));
        this.setDefaultState(this.stateManager.getDefaultState().with(TIMER, 30).with(STATUS, 0).with(PART, BohrPart.BASE));
    }


    @Override
    public BlockRenderType getRenderType(BlockState state) {
//Only render the master block
        return isMaster(state) ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
    }

    public boolean isMaster(BlockState state) {
        return state.get(PART) == BohrPart.BASE;
    }



    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(getMasterPos(state, pos)) instanceof BohrBlockEntity bohrBlockEntity){
            bohrBlockEntity.scatterParticles();
        }
        super.onBreak(world, pos, state, player);
//        destroy the three other parts
        for (BlockPos blockPos : BohrBlockEntity.getBohrParts(state, pos, world)) {
            if (world.getBlockState(blockPos).getBlock() == this) {
                world.breakBlock(blockPos, false);
                world.emitGameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Emitter.of(player, world.getBlockState(blockPos)));
            }
        }
        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, world.getBlockState(pos)));

    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TIMER).add(STATUS).add(PART);
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
                bohrBlockEntity = bohrBlockEntity.getMaster(world);
                if (bohrBlockEntity == null) {
                    return;
                }

                if (item == Items.ELECTRON || item == Items.NEUTRON || item == Items.PROTON) {
                    changedState = bohrBlockEntity.insertParticle(item) == ActionResult.SUCCESS;

                } else if (item == Items.ANTI_NEUTRON || item == Items.ANTI_PROTON || item == Items.POSITRON) {
                    changedState = bohrBlockEntity.removeParticle(item) == ActionResult.SUCCESS;
                }
                if (changedState) {

                    world.updateNeighbors(hit.getBlockPos(), be.uantwerpen.scicraft.block.Blocks.BOHR_BLOCK);
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
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {

        super.scheduledTick(state, world, pos, random);
        int status = 0;

        BlockEntity blockEntity = world.getBlockEntity(pos);


        if (blockEntity instanceof BohrBlockEntity bohrBlockEntity && !world.isClient()) {
            bohrBlockEntity = bohrBlockEntity.getMaster(world);
            if (bohrBlockEntity == null) {
                return;
            }
            int nrOfProtons = bohrBlockEntity.getProtonCount();
            int nrOfNeutrons = bohrBlockEntity.getNeutronCount();
            int nrOfElectrons = bohrBlockEntity.getElectronCount();
            int remaining = state.get(TIMER);
            if (NuclidesTable.isStable(nrOfProtons, nrOfNeutrons, nrOfElectrons)) {
                remaining = 120; // max timer value
            } else {
                remaining = Math.max(0, remaining - 1);
            }
            if (remaining == 0) {
                NbtCompound nbtCompound = bohrBlockEntity.createNbt();
                bohrBlockEntity.writeNbt(nbtCompound);
                bohrBlockEntity.scatterParticles(3);
                remaining = 120;
            }
            state = state.with(TIMER, remaining);
            world.setBlockState(pos, state);
            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
            world.createAndScheduleBlockTick(pos, this, 20, TickPriority.VERY_HIGH);
        }
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {


        BlockEntity blockEntity = world.getBlockEntity(pos);
        ItemStack stack = player.getStackInHand(hand);
        Item item = stack.getItem();

        if (blockEntity instanceof BohrBlockEntity bohrBlockEntity) {
            boolean isActionResultSuccessful = false;
            bohrBlockEntity = bohrBlockEntity.getMaster(world);
            if (bohrBlockEntity == null) return ActionResult.FAIL;
            if (item == Items.NEUTRON || item == Items.PROTON || item == Items.ELECTRON) {
                if (bohrBlockEntity.insertParticle(item) == ActionResult.SUCCESS) {
                    player.getStackInHand(hand).decrement(1);
                    isActionResultSuccessful = true;
                }
            } else if (item == Items.ANTI_NEUTRON || item == Items.ANTI_PROTON || item == Items.POSITRON) {
                if (bohrBlockEntity.removeParticle(item) == ActionResult.SUCCESS) {
                    player.getStackInHand(hand).decrement(1);
                    isActionResultSuccessful = true;
                }
            } else if (item.getGroup() == ItemGroups.ATOMS) {

                int protonAmount = ((AtomItem) item).getAtom().getAtomNumber();
                int neutronAmount = NuclidesTable.findNextStableAtom(protonAmount);

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
                    player.getStackInHand(hand).decrement(1);
                    isActionResultSuccessful = true;
                }

            } else if (stack.isEmpty()) {
//                creating the atom
                if (player.isSneaking()) {
                    bohrBlockEntity.createAtom(world, pos);
                }
//                empty the bohrblock
                else {
                    bohrBlockEntity.scatterParticles();
                }
            }

//            if (isActionResultSuccessful) { // if we changed the amount of protons/neutrons/electrons
//
//                int nrOfProtons = bohrBlockEntity.getProtonCount();
//                int nrOfNeutrons = bohrBlockEntity.getNeutronCount();
//                NucleusState nucleus = NuclidesTable.getNuclide(nrOfProtons, nrOfNeutrons);
//                float halflife = 0f;
//                int remainingNew = 0;
//                if (nucleus != null) {
//                    halflife = nucleus.getHalflife();
//                    if (!nucleus.isStable()) {
//                        remainingNew = NuclidesTable.getHalflifeValues(halflife).get(1).intValue();
//                        state = state.with(TIMER, remainingNew);
//                        world.setBlockState(pos, state);
//                    }
//                    else {
//                        remainingNew = 600;
//                        state = state.with(TIMER, remainingNew);
//                        world.setBlockState(pos, state);
//                    }
//                }
//
//            }


        }
        return ActionResult.SUCCESS;
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shape;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            world.setBlockState(pos.south(), state.with(PART, BohrPart.SOUTH), Block.NOTIFY_ALL);
            world.setBlockState(pos.east(), state.with(PART, BohrPart.EAST), Block.NOTIFY_ALL);
            world.setBlockState(pos.south().east(), state.with(PART, BohrPart.SOUTH_EAST), Block.NOTIFY_ALL);
            world.updateNeighbors(pos, Blocks.AIR);
            world.createAndScheduleBlockTick(pos, this, 1);
        }
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        BlockPos blockPos2 = blockPos.offset(Direction.SOUTH);
        BlockPos blockPos3 = blockPos.offset(Direction.EAST);
        BlockPos blockPos4 = blockPos.south().east();
        World world = ctx.getWorld();
        for (BlockPos pos : List.of(blockPos, blockPos2, blockPos3, blockPos4)) {
            if (!world.getBlockState(pos).canReplace(ctx) || !world.getWorldBorder().contains(pos)) {
                return null;
            }
        }
        return getDefaultState();
    }
}

