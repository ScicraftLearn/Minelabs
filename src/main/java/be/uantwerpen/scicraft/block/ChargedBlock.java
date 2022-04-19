package be.uantwerpen.scicraft.block;

import java.util.function.Supplier;

import be.uantwerpen.scicraft.block.entity.ChargedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChargedBlock extends BlockWithEntity{
	// PLACEMENT=false -> placed by user, PLACEMENT=true-> placed by movement-algorithm
	public static final BooleanProperty PLACEMENT = BooleanProperty.of("placement");
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		super.appendProperties(stateManager);
		stateManager.add(PLACEMENT);
	}

	private Supplier<BlockEntityType<? extends ChargedBlockEntity>> lazy;

	public ChargedBlock(Settings settings, Supplier<BlockEntityType<? extends ChargedBlockEntity>> lazy) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(PLACEMENT, true));
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
        return checkType(type, this.lazy.get(), ChargedBlockEntity::tick);
    }

}
