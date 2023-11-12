package be.minelabs.item.reaction;

import be.minelabs.entity.CorrosiveEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CorrosiveReaction extends Reaction {

    private final int radius;

    public CorrosiveReaction(int radius) {
        this.radius = radius;
    }

    @Override
    protected void react(World world, Vec3d pos) {
        Utils.applyBlocksRadiusTraced(world, pos, this.radius, block -> {
            BlockState blockState = world.getBlockState(BlockPos.ofFloored(pos));
            if (blockState.getBlock() == net.minecraft.block.Blocks.WATER)
                world.addParticle(ParticleTypes.CLOUD, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
            else
                if(canReact(world.getBlockState(block))) {
                    // Calculate the distance between pos and block
                    double distance = pos.distanceTo(Vec3d.ofCenter(block));
                    // Create a new CorrosiveEntity at the given blockpos
                    CorrosiveEntity entity = CorrosiveEntity.create(world, block, distance);
                    // Spawn the entity
                    world.spawnEntity(entity);
                }
        });
        Utils.applyEntitiesRadiusTraced(world, pos, radius, this::react);
    }

    @Override
    public void react(LivingEntity entity) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
    }

    @Override
    public Text getTooltipText() {
        return getTooltipText("corrosive").formatted(Formatting.GRAY);
    }
}
