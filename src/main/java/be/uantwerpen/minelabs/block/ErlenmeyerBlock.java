package be.uantwerpen.minelabs.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ErlenmeyerBlock extends CosmeticBlock {

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
}
