package be.minelabs.item.items;

import be.minelabs.util.Tags;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowyBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class LaserTool extends MiningToolItem {
    protected final Random random = new Random();

    public LaserTool(float attackDamage, float attackSpeed, ToolMaterial material, Settings settings) {
        super(attackDamage, attackSpeed, material, Tags.Blocks.LASERTOOL_MINEABLE, settings);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        // Make all mineable blocks instant mineable
        return state.isIn(Tags.Blocks.LASERTOOL_MINEABLE) ? 9000.f : 1.0f;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        // If block is not mineable by lasertool don't put it back into the world
        if (state.isIn(Tags.Blocks.LASERTOOL_MINEABLE) && random.nextFloat() < 0.95) {
            if (state.contains(SnowyBlock.SNOWY)) {
                world.setBlockState(pos, state.with(SnowyBlock.SNOWY, false));
            } else {
                world.setBlockState(pos, state);
            }
        }
        return super.postMine(stack, world, state, pos, miner);
    }
}
