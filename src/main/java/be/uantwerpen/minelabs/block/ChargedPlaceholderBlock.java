package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.block.entity.ChargedPlaceholderBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class ChargedPlaceholderBlock extends BlockWithEntity{
	private final Supplier<BlockEntityType<? extends ChargedPlaceholderBlockEntity>> lazy;

	public ChargedPlaceholderBlock(Settings settings, Supplier<BlockEntityType<? extends ChargedPlaceholderBlockEntity>> lazy) {
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
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		return VoxelShapes.cuboid(0f, 0f, 0f, 0f, 0f, 0f);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, this.lazy.get(), ChargedPlaceholderBlockEntity::tick);
	}
}
