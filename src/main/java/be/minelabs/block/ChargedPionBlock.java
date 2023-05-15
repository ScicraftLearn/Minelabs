package be.minelabs.block;

import be.minelabs.block.entity.ChargedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.Supplier;

public class ChargedPionBlock extends ChargedBlock {
    public static final IntProperty COLOUR = IntProperty.of("age", 0, 2);

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        super.appendProperties(stateManager);
        stateManager.add(COLOUR);
    }

    public int getRandomNumber(int min, int max) {
        return (new Random()).nextInt(max + 1 - min) + min;
    }

    public ChargedPionBlock(Settings settings, Supplier<BlockEntityType<? extends ChargedBlockEntity>> lazy) {
        super(settings, lazy);
        setDefaultState(getStateManager().getDefaultState().with(COLOUR, 0)); //TODO: Find a way to make this defaulState truely 'random', now the 'random' is only for when the game is loaded for one state (all the next blocks from getDefaultState have the same colour).
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        world.setBlockState(pos, state.with(COLOUR, getRandomNumber(0, 2)));
    }
}
