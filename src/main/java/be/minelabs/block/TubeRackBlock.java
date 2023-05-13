package be.minelabs.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class TubeRackBlock extends CosmeticBlock {
    public TubeRackBlock(Settings settings) {
        super(settings);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        float offset = getYOffset(state);
        return switch (state.get(FACING)) {
            case NORTH -> VoxelShapes.cuboid(0.1875f, (0f - offset), 0.3125f, 0.8125f, (0.4f - offset), 0.6250f);//
            case SOUTH -> VoxelShapes.cuboid(0.1875f, (0f - offset), 0.3750f, 0.8125f, (0.4f - offset), 0.6875f);//
            case EAST -> VoxelShapes.cuboid(0.3750f, (0f - offset), 0.1875f, 0.6875f, (0.4f - offset), 0.8125f);//
            case WEST -> VoxelShapes.cuboid(0.3125f, (0f - offset), 0.1875f, 0.6250f, (0.4f - offset), 0.8125f);
            default -> VoxelShapes.fullCube();
        };
    }
}
