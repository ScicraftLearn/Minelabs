package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.util.QuantumFieldSpawner;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class AtomicFloor extends Block {
//    private static QuantumFieldSpawner generator;

    public AtomicFloor() {
        super(Settings.of(Material.AMETHYST).hardness(200f).strength(200f).nonOpaque().ticksRandomly());

    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        if (world.isClient()) {
            return;
        }
        QuantumFieldSpawner.tryToSpawnCloud(world, pos);
    }

//    @Override
//    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
//        return VoxelShapes.empty();
//    }
}