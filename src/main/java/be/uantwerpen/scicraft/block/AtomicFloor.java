package be.uantwerpen.scicraft.block;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AtomicFloor extends Block {
    private static int fields=0;

    public AtomicFloor(Settings settings) {
        super(Settings.of(Material.BARRIER).nonOpaque());
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if(pos.getX()==0 && pos.getZ()==0 && !(world.getBlockState(pos.up(1)).getBlock() instanceof PortalBlock)){
            pos=pos.up(1);
            world.setBlockState(pos,Blocks.ATOM_PORTAL.getDefaultState());
        }
        //System.out.println("random ding");
        java.util.Random r = new java.util.Random();
        int rvalue = r.nextInt(100000);
        int rheight = r.nextInt(32);
        pos = pos.up(rheight);
        //Only change air blocks so other fields don't get replaced
        if (world.getBlockState(pos) == net.minecraft.block.Blocks.AIR.getDefaultState() && (rvalue>=1500 && rvalue <1520) && fields <64) {
            if (rvalue >= 1500 && rvalue < 1502) {
                System.out.println("Naar ELECTRON");
                world.setBlockState(pos, Blocks.ELECTRON_QUANTUMFIELD.getDefaultState());
            } else if (rvalue >= 1502 && rvalue < 1504) {
                System.out.println("Naar DOWNQUARK");
                world.setBlockState(pos, Blocks.DOWNQUARK_QUANTUMFIELD.getDefaultState());
            } else if (rvalue >= 1504 && rvalue < 1508) {
                System.out.println("Naar GLUON");
                world.setBlockState(pos, Blocks.GLUON_QUANTUMFIELD.getDefaultState());
            } else if (rvalue >= 1508 && rvalue < 1510) {
                System.out.println("Naar PHOTON");
                world.setBlockState(pos, Blocks.PHOTON_QUANTUMFIELD.getDefaultState());
            } else if (rvalue >= 1510 && rvalue < 1512) {
                System.out.println("Naar UPQUARK");
                world.setBlockState(pos, Blocks.UPQUARK_QUANTUMFIELD.getDefaultState());
            } else if (rvalue >= 1512 && rvalue < 1514) {
                System.out.println("Naar WEAK BOSON");
                world.setBlockState(pos, Blocks.WEAK_BOSON_QUANTUMFIELD.getDefaultState());
            } else if (rvalue >= 1514 && rvalue < 1520) {
                System.out.println("Naar NEUTRINO");
                world.setBlockState(pos, Blocks.NEUTRINO_QUANTUMFIELD.getDefaultState());
            }
            fields+=1;
            System.out.println("fields:"+fields);
        }
    }
    public static void decreaseFields(){
        fields-=1;
    }

    public static int getFields() {
        return fields;
    }
}