package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.util.QuantumFieldSpawner;
import net.minecraft.block.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class AtomicFloor extends AbstractGlassBlock {
    public final static int AtomicFloorLayer = 64;

    public AtomicFloor() {
        super(Settings.of(Material.AMETHYST).hardness(200f).strength(200f).nonOpaque().ticksRandomly());

    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
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

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (world instanceof ClientWorld clientWorld){
            PlayerEntity player = clientWorld.getClosestPlayer(pos.getX(),pos.getY(),pos.getZ(),10,false);
            if (player != null && player.getMainHandStack() != null){
                var item = player.getMainHandStack().getItem();
                if (item == Items.BOHR_BLOCK){
                    return VoxelShapes.fullCube();
                }
            }
        }
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }
}