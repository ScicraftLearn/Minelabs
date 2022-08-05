package be.uantwerpen.scicraft.block;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AtomicFloor extends Block {
    //private static int fields;

    public AtomicFloor() {
        super(Settings.of(Material.AMETHYST).hardness(200f).strength(200f).nonOpaque());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    public static Boolean shouldSpawnCloud(float chance) {
        java.util.Random r = new java.util.Random();
        return r.nextFloat() < chance;
    }

    ;

    public static void tryToSpawnCloud(World world, BlockPos pos, float chance) {
        BlockState field_to_spawn = null;
        if (shouldSpawnCloud(chance)) {

            field_to_spawn = pickField(world, pos);
            if (field_to_spawn != null){
                spawnCloud(world, pos,field_to_spawn);
            }
        }
    }

    public static void spawnCloud(World world, BlockPos pos) {
        tryToSpawnCloud(world, pos, 1);
    }

    ;

    public static BlockState pickField(World world, BlockPos pos) {
        java.util.Random r = new java.util.Random();
        BlockState quantumfield = null;

        int type = r.nextInt(7);


            //Only change air blocks so other fields don't get replaced
                switch (type) {
                    case 0 ->
                            quantumfield = Blocks.ELECTRON_QUANTUMFIELD.getDefaultState();
                        //System.out.println("Naar ELECTRON");
//                            spawnCloud(world, pos, ELECTRON_QUANTUMFIELD.getDefaultState());
                    case 1 ->
                            quantumfield = Blocks.DOWNQUARK_QUANTUMFIELD.getDefaultState();
                        //System.out.println("Naar DOWNQUARK");
//                            spawnCloud(world, pos, Blocks.DOWNQUARK_QUANTUMFIELD.getDefaultState());
                    case 2 ->
                            quantumfield = Blocks.GLUON_QUANTUMFIELD.getDefaultState();
                        //System.out.println("Naar GLUON");
//                            spawnCloud(world, pos, Blocks.GLUON_QUANTUMFIELD.getDefaultState());
                    case 3 ->
                            quantumfield = Blocks.PHOTON_QUANTUMFIELD.getDefaultState();
                        //System.out.println("Naar PHOTON");
//                            spawnCloud(world, pos, Blocks.PHOTON_QUANTUMFIELD.getDefaultState());
                    case 4 ->
                            quantumfield = Blocks.UPQUARK_QUANTUMFIELD.getDefaultState();

                    //System.out.println("Naar UPQUARK");
//                            spawnCloud(world, pos, Blocks.UPQUARK_QUANTUMFIELD.getDefaultState());
                    case 5 ->
                            quantumfield = Blocks.WEAK_BOSON_QUANTUMFIELD.getDefaultState();

                    //System.out.println("Naar WEAK BOSON");
//                            spawnCloud(world, pos, Blocks.WEAK_BOSON_QUANTUMFIELD.getDefaultState());
                    case 6 ->
                            quantumfield = Blocks.NEUTRINO_QUANTUMFIELD.getDefaultState();
                        //System.out.println("Naar NEUTRINO");
//                            spawnCloud(world, pos, Blocks.NEUTRINO_QUANTUMFIELD.getDefaultState());
                }




        return quantumfield;
    }


    //fields += 1;
    //System.out.println("fields:" + fields);



    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        tryToSpawnCloud(world, pos,(float) 1/25000);
    }
/*
    public static void decreaseFields() {
        fields -= 1;
    }

    public static int getFields() {
        return fields;
    }

    public static void resetFields() {
        fields = 0;
    }*/

    //Spawning clouds will happen with this algorithm
    private static void spawnCloud(World world, BlockPos pos, BlockState state) {
        java.util.Random r = new java.util.Random();
        int rheight = r.nextInt(10);
//        rheight = rheight - 4;
        pos = pos.up(rheight);

//      only replace air and not too close to other fields
        if (world.getBlockState(pos) == net.minecraft.block.Blocks.AIR.getDefaultState() && !checkFields(pos, world)) {
            int cloudSize = r.nextInt(7,246);
            world.setBlockState(pos, state);
            for (int i = 1; i <= cloudSize; i++) {
                int cloudx = r.nextInt(7) + pos.getX();
                int cloudy = r.nextInt(5) + pos.getY();
                int cloudz = r.nextInt(7) + pos.getZ();
                if (!world.getBlockState(new BlockPos(cloudx, cloudy, cloudz)).getBlock().equals(Blocks.ATOM_FLOOR)) {
                    world.setBlockState(new BlockPos(cloudx, cloudy, cloudz), state);
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