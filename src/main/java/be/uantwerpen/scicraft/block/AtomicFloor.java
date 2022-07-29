package be.uantwerpen.scicraft.block;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AtomicFloor extends Block {

    public AtomicFloor(Settings settings) {
        super(Settings.of(Material.BARRIER).nonOpaque());
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        //System.out.println("random ding");
        java.util.Random r = new java.util.Random();
        int rvalue = r.nextInt(10000);
        //Only change air blocks so other fields don't get replaced
        if (world.getBlockState(pos.up(2)) == net.minecraft.block.Blocks.AIR.getDefaultState()) {
            if (rvalue >= 1500 && rvalue < 1502) {
                System.out.println("Naar ELECTRON");
                world.setBlockState(pos.up(2), Blocks.ELECTRON_QUANTUMFIELD.getDefaultState());
            } else if (rvalue >= 1502 && rvalue < 1504) {
                System.out.println("Naar DOWNQUARK");
                world.setBlockState(pos.up(2), Blocks.DOWNQUARK_QUANTUMFIELD.getDefaultState());
            } else if (rvalue >= 1504 && rvalue < 1508) {
                System.out.println("Naar GLUON");
                world.setBlockState(pos.up(2), Blocks.GLUON_QUANTUMFIELD.getDefaultState());
            } else if (rvalue >= 1508 && rvalue < 1510) {
                System.out.println("Naar PHOTON");
                world.setBlockState(pos.up(2), Blocks.PHOTON_QUANTUMFIELD.getDefaultState());
            } else if (rvalue >= 1510 && rvalue < 1512) {
                System.out.println("Naar UPQUARK");
                world.setBlockState(pos.up(2), Blocks.UPQUARK_QUANTUMFIELD.getDefaultState());
            } else if (rvalue >= 1512 && rvalue < 1514) {
                System.out.println("Naar WEAK BOSON");
                world.setBlockState(pos.up(2), Blocks.WEAK_BOSON_QUANTUMFIELD.getDefaultState());
            } else if (rvalue >= 1514 && rvalue < 1520) {
                System.out.println("Naar NEUTRINO");
                world.setBlockState(pos.up(2), Blocks.NEUTRINO_QUANTUMFIELD.getDefaultState());
            }
        }else{
            if (rvalue < 1500 && world.getBlockState(pos.up(2)) != net.minecraft.block.Blocks.AIR.getDefaultState()) {
                System.out.println("Naar air!");
                world.setBlockState(pos.up(2), net.minecraft.block.Blocks.AIR.getDefaultState());
            }
        }
    }
}