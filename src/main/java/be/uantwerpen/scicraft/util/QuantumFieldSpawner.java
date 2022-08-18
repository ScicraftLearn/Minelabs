package be.uantwerpen.scicraft.util;

import be.uantwerpen.scicraft.block.AtomicFloor;
import be.uantwerpen.scicraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static be.uantwerpen.scicraft.block.QuantumfieldBlock.MASTER;
import static be.uantwerpen.scicraft.block.entity.QuantumFieldBlockEntity.AGE;
import static be.uantwerpen.scicraft.block.entity.QuantumFieldBlockEntity.max_age;

public class QuantumFieldSpawner {
    public static final IntProperty ACTIVE_FIELDS = IntProperty.of("active_fields", 0, 100);

    static java.util.Random r = new java.util.Random();
    static private int nr_fields = 0;
    static private final int passes = 0;


    public static void tryToSpawnCloud(World world, BlockPos pos) {
        BlockState field_to_spawn;
        int max_fields = 15;
        float chance = (float) 1 / 1_000;

        if (shouldSpawnCloud(chance)) {
            field_to_spawn = pickField(world, pos);
            if (field_to_spawn != null) {
                spawnCloud(world, pos, field_to_spawn);
            }
        }

    }

    public static Boolean shouldSpawnCloud(float chance) {
        java.util.Random r = new java.util.Random();
        return r.nextFloat() <= chance;
    }

    public static BlockState pickField(World world, BlockPos pos) {
        BlockState quantumfield = null;
        int type = r.nextInt(7);
        //Only change air blocks so other fields don't get replaced
        switch (type) {
            case 0 -> quantumfield = Blocks.ELECTRON_QUANTUMFIELD.getDefaultState();
            case 1 -> quantumfield = Blocks.DOWNQUARK_QUANTUMFIELD.getDefaultState();
            case 2 -> quantumfield = Blocks.GLUON_QUANTUMFIELD.getDefaultState();
            case 3 -> quantumfield = Blocks.PHOTON_QUANTUMFIELD.getDefaultState();
            case 4 -> quantumfield = Blocks.UPQUARK_QUANTUMFIELD.getDefaultState();
            case 5 -> quantumfield = Blocks.WEAK_BOSON_QUANTUMFIELD.getDefaultState();
            case 6 -> quantumfield = Blocks.NEUTRINO_QUANTUMFIELD.getDefaultState();
        }


        return quantumfield;
    }

    public static void breakCluster(World world, BlockPos pos) {
        nr_fields = Math.max(nr_fields - 1, 0);
        breakCluster(world, pos, false);

    }


    public static void breakCluster(World world, BlockPos pos, boolean first) {
        BlockPos nextPos = null;
        Block nextBlock = null;
        Block block = null;

        block = world.getBlockState(pos).getBlock();
        world.setBlockState(pos, block.getDefaultState().with(AGE, max_age), Block.NO_REDRAW);
        world.removeBlock(pos, false);
        if (pos.getY() == AtomicFloor.AtomicFloorLayer) {
            world.setBlockState(pos, Blocks.ATOM_FLOOR.getDefaultState());
        }

        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    nextPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    nextBlock = world.getBlockState(nextPos).getBlock();
                    if (block.equals(nextBlock)) {
                        breakCluster(world, nextPos);
                    }
                }
            }
        }

    }

    private static void spawnCloud(World world, BlockPos pos, BlockState state) {
        nr_fields += 1;
        int x_size = r.nextInt(5, 25);
        int y_size = r.nextInt(4, 15);
        int z_size = r.nextInt(5, 25);
        int max_cloud_height = 100;
        int cloudHeight = r.nextInt(0, max_cloud_height) - max_cloud_height / 2;

        BlockPos blockPos;
        pos = pos.up(cloudHeight);
        float a = x_size / 2.0f;
        float b = y_size / 2.0f;
        float c = z_size / 2.0f;
        float x_sub, y_sub, z_sub;
        BlockState tempState;

//      only replace air and not too close to other fields
        /**
         * Elipse:   x^2/a^2 + y^2/b^2 + z^2/c^2 = 1
         * (a,0,0); (0,b,0),(0,0,c)
         */
        if (world.getBlockState(pos).isAir() && !checkFields(pos, world, x_size, y_size, z_size)) {
            world.setBlockState(pos, state.getBlock().getDefaultState().with(MASTER, true));
            for (int x = -x_size / 2; x <= x_size / 2; x++) {
//                x_sub = x^2/a^2
                x_sub = x * x / (a * a);
                for (int y = -y_size / 2; y <= y_size / 2; y++) {
//                y_sub = y^2/b^2
                    y_sub = y * y / (b * b);
                    for (int z = -z_size / 2; z <= z_size / 2; z++) {
//                z_sub = z^2/b^2
                        z_sub = z * z / (c * c);
                        if (x_sub + z_sub + y_sub <= 1.0f) {
                            blockPos = new BlockPos(x + pos.getX(), y + pos.getY(), z + pos.getZ());
                            tempState = world.getBlockState(blockPos);
                            if (tempState.isAir() || tempState.getBlock().equals(Blocks.ATOM_FLOOR)) {
                                world.setBlockState(blockPos, state.getBlock().getDefaultState());
                            }
                        }
                    }
                }
            }

        }
    }

    //Checking if there are no other clouds near
    private static Boolean checkFields(BlockPos pos, World world, int x_size, int y_size, int z_size) {
        int blocks_between = 5;
        pos = pos.south(x_size / 2 + blocks_between).west(z_size / 2 + blocks_between).down(y_size / 2 + blocks_between);
        BlockState tempState;

        for (int k = 0; k < x_size + blocks_between; k++) {
            for (int i = 0; i < x_size + blocks_between; i++) {
                for (int j = 0; j < x_size + blocks_between; j++) {
                    tempState = world.getBlockState(new BlockPos(i, pos.getY(), j));
                    if (!(tempState.isAir() || tempState.getBlock().equals(Blocks.ATOM_FLOOR))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
