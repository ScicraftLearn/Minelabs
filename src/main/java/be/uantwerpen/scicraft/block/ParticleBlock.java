package be.uantwerpen.scicraft.block;

import java.util.function.Supplier;

import be.uantwerpen.scicraft.block.entity.ParticleBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleBlock extends BlockWithEntity{

	private Supplier<BlockEntityType<? extends ParticleBlockEntity>> lazy;

	public ParticleBlock(Settings settings, Supplier<BlockEntityType<? extends ParticleBlockEntity>> lazy) {
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
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, this.lazy.get(), ParticleBlockEntity::tick);
    }

}
