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
        // Radius halved because Ine said so
        Utils.applyBlocksRadiusTraced(world, pos, radius / 2, block -> {
            if(!world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK))
                return;
            if (world.getBlockState(block).getBlock() != Blocks.AIR)
                return;
            if (this.pyrophoric || Utils.isFlameNearby(world, block.toCenterPos(), 3)
                    || world.getBiome(BlockPos.ofFloored(pos.x, pos.y, pos.z)).isIn(Tags.Biomes.FLAMMABLE_BIOMES))
                world.setBlockState(block, Blocks.FIRE.getDefaultState().with(FireBlock.AGE, 1));
        });
    }

    @Override
    public void react(LivingEntity entity) {
        // Radius halved because Ine said so
        Utils.applyEntitiesRadiusTraced(entity.getWorld(), entity.getPos(), radius / 2, e -> {
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
