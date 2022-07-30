package be.uantwerpen.scicraft.block;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AtomicFloor extends Block {
    private static int fields;

    public AtomicFloor() {
        super(Settings.of(Material.AMETHYST).hardness(200f).strength(200f).nonOpaque());
    }


    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        java.util.Random r = new java.util.Random();
        int rvalue = r.nextInt(90000);
        int rheight = r.nextInt(44);
        rheight=rheight-12;
        if(rheight!=pos.getY()){
            pos=pos.up(rheight);
            //System.out.println("height quantum:"+rheight);
            //Only change air blocks so other fields don't get replaced
            if (world.getBlockState(pos) == net.minecraft.block.Blocks.AIR.getDefaultState() && (rvalue >= 1500 && rvalue < 1520) && fields < 2500) {
                if (rvalue >= 1500 && rvalue < 1502) {
                    //System.out.println("Naar ELECTRON");
                    spawnCloud(world,pos, Blocks.ELECTRON_QUANTUMFIELD.getDefaultState());
                } else if (rvalue >= 1502 && rvalue < 1504) {
                    //System.out.println("Naar DOWNQUARK");
                    spawnCloud(world,pos, Blocks.DOWNQUARK_QUANTUMFIELD.getDefaultState());
                } else if (rvalue >= 1504 && rvalue < 1508) {
                    //System.out.println("Naar GLUON");
                    spawnCloud(world,pos,Blocks.GLUON_QUANTUMFIELD.getDefaultState());
                } else if (rvalue >= 1508 && rvalue < 1510) {
                    //System.out.println("Naar PHOTON");
                    spawnCloud(world,pos, Blocks.PHOTON_QUANTUMFIELD.getDefaultState());
                } else if (rvalue >= 1510 && rvalue < 1512) {
                    //System.out.println("Naar UPQUARK");
                    spawnCloud(world,pos, Blocks.UPQUARK_QUANTUMFIELD.getDefaultState());
                } else if (rvalue >= 1512 && rvalue < 1514) {
                    //System.out.println("Naar WEAK BOSON");
                    spawnCloud(world,pos, Blocks.WEAK_BOSON_QUANTUMFIELD.getDefaultState());
                } else if (rvalue >= 1514 && rvalue < 1520) {
                    //System.out.println("Naar NEUTRINO");
                    spawnCloud(world,pos, Blocks.NEUTRINO_QUANTUMFIELD.getDefaultState());
                }
                fields += 1;
                //System.out.println("fields:" + fields);
            }
        }
    }

    public static void decreaseFields() {
        fields -= 1;
    }

    public static int getFields() {
        return fields;
    }

    public static void resetFields() {
        fields = 0;
    }

    //Spawning clouds will happen with this algorithm
    private void spawnCloud(World world,BlockPos pos,BlockState state){
        if(!checkFields(pos,world)){
            java.util.Random r=new java.util.Random();
            int cloudSize=r.nextInt(7,246);
            world.setBlockState(pos,state);
            for (int i=1;i<=cloudSize;i++){
                int cloudx=r.nextInt(7)+pos.getX();
                int cloudy=r.nextInt(5)+pos.getY();
                int cloudz=r.nextInt(7)+pos.getZ();
                if(!world.getBlockState(new BlockPos(cloudx,cloudy,cloudz)).getBlock().equals(Blocks.ATOM_FLOOR)){
                    world.setBlockState(new BlockPos(cloudx,cloudy,cloudz),state);
                }
            }
        }
        //world.setBlockState(pos,state);
    }
    //Checking if there are no other clouds near
    private Boolean checkFields(BlockPos pos,World world){
        pos=pos.south(10).west(10);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if(world.getBlockState(new BlockPos(i,pos.getY(),j)).getBlock() instanceof QuantumfieldBlock){
                    return true;
                }
            }
        }
        return false;
    }
}