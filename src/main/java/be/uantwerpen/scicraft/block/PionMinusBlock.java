package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.BlockEntities;
import be.uantwerpen.scicraft.block.entity.ChargedBlockEntity;
import be.uantwerpen.scicraft.block.entity.PionMinusBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PionMinusBlock extends BlockWithEntity {
    public static final IntProperty COLOUR = IntProperty.of("age", 0, 2);

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(COLOUR);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public PionMinusBlock(Settings settings) {
        super(settings);
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

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PionMinusBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        ChargedBlockEntity.removed(world, pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, BlockEntities.PION_MINUS_BLOCK_ENTITY, PionMinusBlockEntity::tick);
    }
}
