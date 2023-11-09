package be.minelabs.block.blocks;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.block.entity.QuantumFieldBlockEntity;
import be.minelabs.item.Items;
import be.minelabs.state.property.Properties;
import be.minelabs.world.MinelabsGameRules;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuantumfieldBlock extends Block implements BlockEntityProvider {
    public static final int MAX_AGE = 10;
    public static final int DECAYRATE = 1;

    //    max 15 in minecraft
    protected static final int MAX_LIGHT = 15;
    protected static final int MIN_LIGHT = 0;

    public static final BooleanProperty MASTER = Properties.MASTER;
    public static final IntProperty AGE = IntProperty.of("age", 0, MAX_AGE);
    public static final IntProperty DROP_KIND = IntProperty.of("kind", 0, 2);


    public QuantumfieldBlock() {
        // Properties of all quantumfield blocks
        // Change the first value in strength to get the wanted mining speed
        // Made material not solid so portals can't spawn on it.
        super(FabricBlockSettings
                .of(Material.POWDER_SNOW)
                .strength(0.5f, 2.0f)
                .ticksRandomly()
                .luminance(state -> (int) Math.ceil(MathHelper.clampedLerp(MAX_LIGHT, MIN_LIGHT, (float) getAge(state) / MAX_AGE)))
        );
        this.setDefaultState(getDefaultState().with(AGE, 0).with(MASTER, false).with(DROP_KIND, 0));

    }

    public static boolean isMaster(BlockState state) {
        return state.get(MASTER);
    }

    public static int getAge(BlockState state) {
        return state.get(AGE);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return isMaster(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!isMaster(state)) return;

        int age = getAge(state);
        age = Math.min(age + DECAYRATE, MAX_AGE);
        if (age == MAX_AGE) {
            world.scheduleBlockTick(pos, this, 5);
        }
        state = state.with(AGE, age);
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        int age = getAge(state);
        if (neighborState.isOf(state.getBlock()) && age < getAge(neighborState)) {
            age = getAge(neighborState);
            if (age == MAX_AGE) {
                world.scheduleBlockTick(pos, this, 5);
            }
            return state.with(AGE, age);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE, MASTER, DROP_KIND);
    }

    public void removeQuantumBlockIfNeeded(BlockState state, ServerWorld world, BlockPos pos) {
        if (MAX_AGE == getAge(state)) {
            world.removeBlock(pos, false);
            if (pos.getY() == AtomicFloor.ATOMIC_FLOOR_LAYER) {
                world.setBlockState(pos, Blocks.ATOM_FLOOR.getDefaultState(), Block.NO_REDRAW);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        removeQuantumBlockIfNeeded(state, world, pos);
        super.scheduledTick(state, world, pos, random);
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
            int drop = state.get(DROP_KIND);
            drop++;
            if (drop % 3 == 0) {
                drop = 0;
            }
            world.setBlockState(pos, state.with(DROP_KIND, drop), Block.NOTIFY_ALL);
        }
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        if (builder.getWorld().getGameRules().getBoolean(MinelabsGameRules.RANDOM_QUANTUM_DROPS)) {
            return super.getDroppedStacks(state, builder);
        } else {
            boolean down = getTranslationKey().contains("downquark");
            int kind = state.get(DROP_KIND);
            return List.of(
                    new ItemStack(down ? Items.down_stacks.get(kind) : Items.up_stacks.get(kind)),
                    new ItemStack(down ? Items.down_stacks.get(kind + 3) : Items.up_stacks.get(kind + 3)));
        }
    }
}
