package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.ChargedBlockEntity;
import be.uantwerpen.scicraft.block.entity.ChargedBlockEntityNEW;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ChargedBlockNEW extends BlockWithEntity{
	private Supplier<BlockEntityType<? extends ChargedBlockEntityNEW>> lazy;

	public ChargedBlockNEW(Settings settings, Supplier<BlockEntityType<? extends ChargedBlockEntityNEW>> lazy) {
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
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof ChargedBlockEntityNEW charged) {
			charged.makeField(world, pos);
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof ChargedBlockEntityNEW charged) {
			System.out.println("remove");
			charged.removeField(world, pos);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, this.lazy.get(), ChargedBlockEntityNEW::tick);
	}
}
