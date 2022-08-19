package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.QuantumFieldBlockEntity;
import be.uantwerpen.scicraft.util.QuantumFieldSpawner;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import static be.uantwerpen.scicraft.block.entity.QuantumFieldBlockEntity.*;


public class QuantumfieldBlock extends AbstractGlassBlock implements BlockEntityProvider {
    public static final BooleanProperty MASTER = BooleanProperty.of("is_master");

    public QuantumfieldBlock() {
        // Properties of all quantumfield blocks
        // Change the first value in strength to get the wanted mining speed
        super(FabricBlockSettings.of(Material.METAL).strength(0.5f, 2.0f).ticksRandomly().luminance(state -> (max_age - state.get(AGE)) / max_age * max_light));
        this.setDefaultState(getDefaultState().with(AGE, 0).with(MASTER, false));
    }

    public boolean isMaster(BlockState state) {
        return state.get(MASTER);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
//        world.getEntitiesByType(this,)

        return BlockEntityProvider.super.getTicker(world, state, type);
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return super.isTranslucent(state, world, pos);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!isMaster(state)) return;

        int age = state.get(AGE);

        if (age + decayrate >= max_age) {
            QuantumFieldSpawner.breakCluster(world, pos);
        } else {
            age += decayrate;
            state = state.with(AGE, age);
            world.setBlockState(pos, state);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.getBlock() == neighborState.getBlock() && state.get(AGE) < (neighborState.get(AGE))) {
            return state.with(AGE, neighborState.get(AGE));
        }
        return super.getStateForNeighborUpdate(state,direction,neighborState,world,pos,neighborPos);
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE).add(MASTER);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, state));
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
            world.setBlockState(pos, state);
        }
    }
}
