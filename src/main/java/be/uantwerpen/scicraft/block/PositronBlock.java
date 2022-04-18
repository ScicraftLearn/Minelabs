package be.uantwerpen.scicraft.block;

import org.jetbrains.annotations.Nullable;

import be.uantwerpen.scicraft.block.entity.ChargedBlockEntity;
import be.uantwerpen.scicraft.block.entity.PositronBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PositronBlock extends BlockWithEntity {

    public PositronBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PositronBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        ChargedBlockEntity.placed(world, pos, state);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        ChargedBlockEntity.removed(world, pos, state);
        super.onBreak(world, pos, state, player);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, null, PositronBlockEntity::tick);
    }
}
