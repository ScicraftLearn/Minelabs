package be.minelabs.item.reaction;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DissolveReaction extends Reaction {

    @Override
    protected void react(World world, Vec3d sourcePos) {
        for (int i = 0; i < 10; i++) {
            world.addParticle(ParticleTypes.CLOUD, sourcePos.getX(), sourcePos.getY(), sourcePos.getZ(), 0, 0, 0);
        }
    }

    @Override
    public void react(LivingEntity entity) {

    }

    @Override
    public Text getTooltipText() {
        return getTooltipText("dissolve").formatted(Formatting.GRAY);
    }
}
