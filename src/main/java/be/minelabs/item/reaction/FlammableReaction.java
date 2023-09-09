package be.minelabs.item.reaction;

import be.minelabs.util.Tags;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class FlammableReaction extends Reaction {

    private final int duration;
    private final int radius;
    private final boolean pyrophoric;

    public FlammableReaction(int duration, int radius, boolean pyrophoric) {
        this.duration = duration;
        this.radius = radius;
        this.pyrophoric = pyrophoric;
    }

    @Override
    protected void react(World world, Vec3d pos) {
        Utils.applyRadius(pos, radius, block -> {
            if(!world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK))
                return;
            if (world.getBlockState(BlockPos.ofFloored(block)).getBlock() != Blocks.AIR)
                return;
            if (this.pyrophoric || Utils.isFlameNearby(world, block, 3)
                    || world.getBiome(BlockPos.ofFloored(pos.x, pos.y, pos.z)).isIn(Tags.Biomes.FLAMMABLE_BIOMES))
                world.setBlockState(BlockPos.ofFloored(block), Blocks.FIRE.getDefaultState().with(FireBlock.AGE, 1));
        });
    }

    @Override
    public void react(LivingEntity entity) {
        Utils.applyRadius(entity.getWorld(), entity.getPos(), radius, e -> {
            //if (this.pyrophoric || Utils.isFlameNearby(e.getWorld(), e.getPos(), 3)) {
            //    e.setFireTicks(duration);
            //}
        });
    }

    @Override
    public Text getTooltipText() {
        return getTooltipText("flammable").formatted(Formatting.RED);
    }
}
