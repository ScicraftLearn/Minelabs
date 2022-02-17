package be.uantwerpen.scicraft.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class QuantumfieldBlock extends Block {
    public QuantumfieldBlock() {
        // Properties of all quantumfield blocks
        // Change the first value in strength to get the wanted mining speed
        super(FabricBlockSettings.of(Material.METAL).noCollision().strength(0.5f, 2.0f));
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        player.incrementStat(Stats.MINED.getOrCreateStat(this));
        player.addExhaustion(0.005f);
        Block.dropStacks(state, world, pos, blockEntity, player, stack);
        // Added code compared to minecraft block code from here
        world.setBlockState(pos, state);
    }
}
