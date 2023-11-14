package be.minelabs.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

class BohrPlateParticle extends SpriteBillboardParticle {
    protected BohrPlateParticle(ClientWorld clientWorld, double x, double y, double z, double vx, double vy, double vz, float angle) {
        super(clientWorld, x, y, z, vx, vy, vz);
        this.velocityMultiplier = 0.94f;
        this.velocityX = velocityX * 0.01f + vx;
        this.velocityY = velocityY * 0.01f + vy;
        this.velocityZ = velocityZ * 0.01f + vz;
        this.angle = angle;
        this.prevAngle = angle;
        setAlpha(0.4f);
        maxAge = 15;
        scale *= 0.5f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
            // Note: usually vx, vy and vz should be kept at zero and this factory provides the correct velocity.
            Random random = world.getRandom();
            Vector3f offset = new Vector3f(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f, random.nextFloat() - 0.5f).normalize().mul(0.3f);
            Vector3f velocity = new Vector3f(offset).mul(-0.05f);

            // random angles
//            float angle = (float) (random.nextFloat() * 2 * Math.PI);

            // Heuristic to have the particle face the center
            float offsetX = Math.abs(offset.x) > Math.abs(offset.y) ? offset.x : offset.y;
            float angle = (float) (-Math.atan2(offsetX, offset.y));

            BohrPlateParticle particle = new BohrPlateParticle(world, x + offset.x, y + offset.y, z + offset.z, vx + velocity.x, vy + velocity.y, vz + velocity.z, angle);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }

}
