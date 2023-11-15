package be.minelabs.client.particle;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
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
        maxAge = 17;
        scale *= 0.45f;
        updateAlpha();
    }

    private void updateAlpha(){
        alpha = 0.8f * (float) Math.exp(-Math.pow((age-7f)/6f, 2));
    }

    @Override
    public void tick() {
        super.tick();
        updateAlpha();
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

            // Offset is random position in projected plane around camera
            Vector3f offset = new Vector3f(0, 0.25f, 0);
            Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
            Quaternionf rotation = new Quaternionf(camera.getRotation());
            float angle = (float) (random.nextFloat() * 2 * Math.PI);
            rotation.rotateZ(angle);
            offset.rotate(rotation);

            // Move towards center
            Vector3f velocity = new Vector3f(offset).mul(-0.06f);

            BohrPlateParticle particle = new BohrPlateParticle(world, x + offset.x, y + offset.y, z + offset.z, vx + velocity.x, vy + velocity.y, vz + velocity.z, angle);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }

}
