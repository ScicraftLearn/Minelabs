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
    protected void react(World world, Vec3d sourcePos) {
        boolean doFire = this.pyrophoric
                || Utils.isFlameNearby(world, sourcePos, radius)
                || world.getBiome(BlockPos.ofFloored(sourcePos.x, sourcePos.y, sourcePos.z)).isIn(Tags.Biomes.FLAMMABLE_BIOMES);

        Utils.applyBlocksRadiusTraced(world, sourcePos, radius / 2, pos -> {
            if(!world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK))
                return;
            if (!world.getBlockState(pos).isOf(Blocks.AIR))
                return;
            if (doFire)
                world.setBlockState(pos, Blocks.FIRE.getDefaultState().with(FireBlock.AGE, 1));
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
