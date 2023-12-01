package be.minelabs.item.reaction;

import net.minecraft.block.AbstractCandleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ExtinguishReaction extends Reaction {

    private final int radius;

    public ExtinguishReaction(int radius) {
        this.radius = radius;
    }

    @Override
    protected void react(World world, Vec3d sourcePos) {
        Utils.applyBlocksRadiusTraced(world, sourcePos, this.radius, pos -> {
            BlockState block = world.getBlockState(pos);
            if (block.isIn(BlockTags.FIRE)) {
                world.removeBlock(pos, false);
            } else if (AbstractCandleBlock.isLitCandle(block)) {
                AbstractCandleBlock.extinguish(null, block, world, pos);
            } else if (CampfireBlock.isLitCampfire(block)) {
                world.syncWorldEvent(null, 1009, pos, 0);
                CampfireBlock.extinguish(null, world, pos, block);
                world.setBlockState(pos, block.with(CampfireBlock.LIT, false));
            }
        });

        Utils.applyEntitiesRadiusTraced(world, sourcePos, this.radius, this::react);
    }

    @Override
    public void react(LivingEntity entity) {
        entity.setFireTicks(0);
    }

    @Override
    public Text getTooltipText() {
        return getTooltipText("extinguish").formatted(Formatting.GRAY);
    }
}
