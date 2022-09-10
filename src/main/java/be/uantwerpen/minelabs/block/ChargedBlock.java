package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.block.entity.ChargedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ChargedBlock extends BlockWithEntity{
	private final Supplier<BlockEntityType<? extends ChargedBlockEntity>> lazy;

	public ChargedBlock(Settings settings, Supplier<BlockEntityType<? extends ChargedBlockEntity>> lazy) {
		super(settings);
		this.lazy = lazy;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos arg0, BlockState arg1) {
		return lazy.get().instantiate(arg0, arg1);
	}
	
	@Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof ChargedBlockEntity charged && !newState.isOf(Blocks.ANIMATED_CHARGED)) {
			charged.removeField(world, pos);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof ChargedBlockEntity charged) {
			charged.needsUpdate(true);
		}
		super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, this.lazy.get(), ChargedBlockEntity::tick);
	}
}
