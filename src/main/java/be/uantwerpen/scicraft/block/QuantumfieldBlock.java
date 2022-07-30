package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.dimension.ModDimensions;
import be.uantwerpen.scicraft.event.ModEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class QuantumfieldBlock extends Block {
    //Ticklife for subatomic dimension
    //Randomizing will give it a unique property
    java.util.Random r = new java.util.Random();
    private int tickLife = r.nextInt(400,6000);

    public QuantumfieldBlock() {
        // Properties of all quantumfield blocks
        // Change the first value in strength to get the wanted mining speed
        super(FabricBlockSettings.of(Material.METAL).noCollision().strength(0.5f, 2.0f));
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        world.setBlockState(pos, state);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        //Get random destruction so every field acts differently
        int destruction = r.nextInt(20);
        tickLife -= destruction;
        if (tickLife <= 0) {
            world.breakBlock(pos,true);
            AtomicFloor.decreaseFields();
        }
        //If there are too many quantumfields, near death ones will be removed so new ones can spawn
        if(AtomicFloor.getFields()>=64 && tickLife<=400){
            world.breakBlock(pos, true);
        }
    }
}
