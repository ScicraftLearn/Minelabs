package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.block.entity.QuantumFieldBlockEntity;
import be.uantwerpen.minelabs.util.MinelabsProperties;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class QuantumfieldBlock extends Block implements BlockEntityProvider {
    public static final int MAX_AGE = 10;
    public static final int decayrate = 1;
    //    max 15 in minecraft
    public static final int MAX_LIGHT = 15;
    public static final int MIN_LIGHT = 0;
    public static final BooleanProperty MASTER = MinelabsProperties.MASTER;
    public static final IntProperty AGE = IntProperty.of("age", 0, 10);


    public QuantumfieldBlock() {
        // Properties of all quantumfield blocks
        // Change the first value in strength to get the wanted mining speed
        super(FabricBlockSettings.of(Material.METAL).strength(0.5f, 2.0f)
                .ticksRandomly().luminance(state ->
                        Math.round(MIN_LIGHT + ((float) (MAX_AGE - state.get(AGE) - 1) / MAX_AGE) * (MAX_LIGHT - MIN_LIGHT))));
        this.setDefaultState(getDefaultState().with(AGE, 0).with(MASTER, false));
    }

    public boolean isMaster(BlockState state) {
        return state.get(MASTER);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!isMaster(state)) return;

        int age = state.get(AGE);
        age = Math.min(age + decayrate, MAX_AGE);
        if (age == MAX_AGE) {
            world.createAndScheduleBlockTick(pos, this, 5);
        }
        state = state.with(AGE, age);
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        int age = state.get(AGE);
        if (state.getBlock() == neighborState.getBlock() && age < neighborState.get(AGE)) {
            age = neighborState.get(AGE);
            if (age == MAX_AGE) {
                world.createAndScheduleBlockTick(pos, this, 5);
            }
            return state.with(AGE, age);
        }
        return super.getStateForNeighborUpdate(state,direction,neighborState,world,pos,neighborPos);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE).add(MASTER);
    }

    public void removeQuantumBlockIfNeeded(BlockState state, ServerWorld world, BlockPos pos){
        if (MAX_AGE == state.get(AGE)) {
            world.removeBlock(pos, false);
            if (pos.getY() == AtomicFloor.AtomicFloorLayer) {
                world.setBlockState(pos, Blocks.ATOM_FLOOR.getDefaultState(), Block.NO_REDRAW);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        removeQuantumBlockIfNeeded(state,world,pos);
        super.scheduledTick(state, world, pos, random);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
//        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, state));
        super.onBreak(world,pos,state,player);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return isMaster(state) ? createMasterBlockEntity(pos, state) : null;
    }

    public BlockEntity createMasterBlockEntity(BlockPos pos, BlockState state) {
        return new QuantumFieldBlockEntity(pos, state);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        if (!world.isClient()) {
            world.setBlockState(pos, state,Block.NOTIFY_ALL);
        }
    }
}
