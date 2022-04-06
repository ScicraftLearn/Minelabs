package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.ChargedBlockEntity;
import be.uantwerpen.scicraft.block.entity.PionPlusBlockEntity;
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

public class ChargedPionBlock<T extends  ChargedBlockEntity> extends ChargedBlock<T> {
    public static final IntProperty COLOUR = IntProperty.of("age", 0, 2);
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(COLOUR);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public ChargedPionBlock(Settings settings, double charge, BlockEntityType<T> NAME) {
        super(settings, charge, NAME);
        setDefaultState(getStateManager().getDefaultState().with(COLOUR, getRandomNumber(0, 2)));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        double R = Math.random();
        int i = 0;
        if ((R <= 0.333333333333333)){
            i = 1;
        } else if ((R <= 0.66666666666666)){
            i = 2;
        }
        world.setBlockState(pos, state.with(COLOUR, i));
    }
}