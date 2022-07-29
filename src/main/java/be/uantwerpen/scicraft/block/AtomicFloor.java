package be.uantwerpen.scicraft.block;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AtomicFloor extends Block{

    public AtomicFloor(Settings settings) {
        super(Settings.of(Material.BARRIER).nonOpaque());
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        //System.out.println("random ding");
        java.util.Random r=new java.util.Random();
        if(r.nextInt(100000)>99990){
            System.out.println("50 procent?");
        }
    }
}