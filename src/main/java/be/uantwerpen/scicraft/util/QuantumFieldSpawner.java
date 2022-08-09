package be.uantwerpen.scicraft.util;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.block.QuantumfieldBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class QuantumFieldSpawner {

    static java.util.Random r = new java.util.Random();
    static private int nr_fields = 0;
    static private int passes = 0;


    public static void tryToSpawnCloud(World world, BlockPos pos) {
        BlockState field_to_spawn;
        int max_fields = 15;
//        passes++;
//        if (passes >= 100){
//            nr_fields = 0;
//        }
        float chance = ((float) (max_fields - nr_fields ))/max_fields;
//        if (!world.isClient()){
//            passes +=1;
//        }
        if (shouldSpawnCloud(chance)) {
            passes = 0;
            field_to_spawn = pickField(world, pos);
            if (field_to_spawn != null){
                spawnCloud(world, pos,field_to_spawn);
            }
        }

    }

//    public static void spawnCloud(World world, BlockPos pos) {
//        tryToSpawnCloud(world, pos, 1);
//    }
    public static Boolean shouldSpawnCloud(float chance) {
        java.util.Random r = new java.util.Random();
        return r.nextFloat() < chance;
    }

    public static BlockState pickField(World world, BlockPos pos) {
        BlockState quantumfield = null;
        int type = r.nextInt(7);
        //Only change air blocks so other fields don't get replaced
        switch (type) {
            case 0 ->
                    quantumfield = Blocks.ELECTRON_QUANTUMFIELD.getDefaultState();
            case 1 ->
                    quantumfield = Blocks.DOWNQUARK_QUANTUMFIELD.getDefaultState();
            case 2 ->
                    quantumfield = Blocks.GLUON_QUANTUMFIELD.getDefaultState();
            case 3 ->
                    quantumfield = Blocks.PHOTON_QUANTUMFIELD.getDefaultState();
            case 4 ->
                    quantumfield = Blocks.UPQUARK_QUANTUMFIELD.getDefaultState();
            case 5 ->
                    quantumfield = Blocks.WEAK_BOSON_QUANTUMFIELD.getDefaultState();
            case 6 ->
                    quantumfield = Blocks.NEUTRINO_QUANTUMFIELD.getDefaultState();
        }




        return quantumfield;
    }

    public static void breakCluster(World world, BlockPos pos) {
        nr_fields = Math.max(nr_fields-1,0);
        breakCluster(world,pos,false);

    }
    public static void breakCluster(World world, BlockPos pos,boolean first) {
        BlockPos nextPos = null;
        Block nextBlock = null;
        Block block = null;

        block = world.getBlockState(pos).getBlock();
        world.setBlockState(pos, block.getDefaultState().with(QuantumfieldBlock.AGE,25), Block.NO_REDRAW);
        world.removeBlock(pos, false);

        for (int x = -2; x < 3; x++) {
            for (int y = -2; y < 3; y++) {
                for (int z = -2; z < 3; z++) {
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
        int x_size = r.nextInt(2,20);
        int y_size = r.nextInt(2,10);
        int z_size = r.nextInt(2,20);
        int cloudSize = x_size * y_size * z_size;
        int cloudHeight = r.nextInt(0,8);
        int blocksToSpawn = r.nextInt(Math.round(0.1f * cloudSize),Math.round((0.9F * cloudSize)+0.5f));

        int cloudx, cloudy, cloudz;

        pos = pos.up(5-pos.getY()+cloudHeight);


//      only replace air and not too close to other fields
        if (world.getBlockState(pos) == net.minecraft.block.Blocks.AIR.getDefaultState() && !checkFields(pos, world)) {
//            world.setBlockState(pos, state);
            for (int i = 0; i < blocksToSpawn; i++) {
                cloudx = r.nextInt(0,x_size) - x_size/2 + pos.getX();
                cloudy = r.nextInt(0,y_size) + pos.getY();
                cloudz = r.nextInt(0,z_size) - z_size/2 + pos.getZ();
                if (!world.getBlockState(new BlockPos(cloudx, cloudy, cloudz)).getBlock().equals(Blocks.ATOM_FLOOR)&& cloudy > 5) {
//                    world.setBlockBreakingInfo();
                    world.setBlockState(new BlockPos(cloudx, cloudy, cloudz), state.getBlock().getDefaultState());

                }
            }
        }
        //world.setBlockState(pos,state);
    }

    //Checking if there are no other clouds near
    private static Boolean checkFields(BlockPos pos, World world) {
        pos = pos.south(20).west(20).down(20);
        for (int k = pos.getY(); k < 40; k++) {
            for (int i = 0; i < 40; i++) {
                for (int j = 0; j < 40; j++) {
                    if (world.getBlockState(new BlockPos(i, pos.getY(), j)).getBlock() instanceof QuantumfieldBlock) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}