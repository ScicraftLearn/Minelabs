package be.minelabs.block.blocks;

import be.minelabs.block.entity.ErlenmeyerBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ErlenmeyerBlock extends CosmeticBlock implements BlockEntityProvider {

    public ErlenmeyerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        float offset = getYOffset(state);
        return switch (state.get(FACING)) {
            case NORTH, SOUTH -> VoxelShapes.cuboid(0.3750f, (0f - offset), 0.3125f, 0.625f, (0.8f - offset), 0.6875f);
            case EAST, WEST -> VoxelShapes.cuboid(0.3125f, (0f - offset), 0.375f, 0.6875f, (0.8f - offset), 0.625f);
            default -> VoxelShapes.fullCube();
        };
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (!world.isClient) {
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(((ErlenmeyerBlockEntity) blockEntity).getItem()));
        }
        super.afterBreak(world, player, pos, state, blockEntity, tool);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        ((ErlenmeyerBlockEntity) world.getBlockEntity(pos)).setItem(itemStack.getItem());
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(((ErlenmeyerBlockEntity) world.getBlockEntity(pos)).getItem());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ErlenmeyerBlockEntity(pos, state);
    }
}
