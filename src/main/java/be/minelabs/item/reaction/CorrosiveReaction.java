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
    protected void react(World world, Vec3d sourcePos) {
        Utils.applyBlocksRadiusTraced(world, sourcePos, this.radius, pos -> {
            BlockState blockState = world.getBlockState(pos);
            if (blockState.getBlock() == net.minecraft.block.Blocks.WATER)
                world.addParticle(ParticleTypes.CLOUD, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
            else
                if(canReact(world.getBlockState(pos))) {
                    // Calculate the distance between pos and block
                    double distance = sourcePos.distanceTo(Vec3d.ofCenter(pos));
                    // Create a new CorrosiveEntity at the given blockpos
                    CorrosiveEntity entity = CorrosiveEntity.create(world, pos, distance);
                    // Spawn the entity
                    world.spawnEntity(entity);
                }
        });
        Utils.applyEntitiesRadiusTraced(world, sourcePos, radius, this::react);
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
