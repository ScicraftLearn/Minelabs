package be.minelabs.client.particle;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

class BohrPlateParticle extends SpriteBillboardParticle {
    protected BohrPlateParticle(ClientWorld clientWorld, double x, double y, double z, double vx, double vy, double vz, float angle, float scale) {
        super(clientWorld, x, y, z, vx, vy, vz);
        this.velocityMultiplier = 0.94f;
        this.velocityX = velocityX * 0.01f + vx;
        this.velocityY = velocityY * 0.01f + vy;
        this.velocityZ = velocityZ * 0.01f + vz;
        this.angle = angle;
        this.prevAngle = angle;
        setAlpha(0.4f);
        maxAge = 15;
        this.scale *= scale;
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
            Random random = Random.create();
            Vector3f offset = new Vector3f(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f, random.nextFloat() - 0.5f).normalize().mul(0.3f);
            Vector3f velocity = new Vector3f(offset).mul(-0.05f);

            // Get the position of the camera
            Vector3f cameraPos = MinecraftClient.getInstance().player.getClientCameraPosVec(0).toVector3f();

            // Get the position of the center of the HUD
            Vector3f hudPos = new Vector3f((float) x, (float) y, (float) z);

            // Get the normal on the plane on which to project the particle
            Vector3f normalPlane = hudPos.sub(cameraPos).normalize();

            // Reference, what is 'up' on the plane, this - better to use Pitch/Yaw and all those things
            Vector3f yVec = new Vector3f(0,1,0);

            // Project on to the plane
            Vector3f projy = yVec.sub(normalPlane.mul(yVec.dot(normalPlane), new Vector3f()), new Vector3f()).normalize();

            // Obtain the x-axis on the plane by some cross product
            Vector3f projx = normalPlane.cross(projy, new Vector3f());

            // Project the particle on the plane
            Vector3f projp = offset.sub(normalPlane.mul(offset.dot(normalPlane), new Vector3f()), new Vector3f()).normalize();

            // random angles
//            float angle = (float) (random.nextFloat() * 2 * Math.PI);

            // Heuristic to have the particle face the center
            //float offsetX = Math.abs(offset.x) > Math.abs(offset.y) ? offset.x : offset.y;
            //float angle = (float) (-Math.atan2(offsetX, offset.y));

            // we can maybe adjust the scale with the z-position
            float scale = (offset.dot(normalPlane) + 1f) * .25f;

            // Obtain the angle: the dot product can't get the sign, that's why the x-part is also there  to give the sign
            float angle = (float) Math.acos(projy.dot(projp)) * Math.signum(projp.dot(projx));

            BohrPlateParticle particle = new BohrPlateParticle(world, x + offset.x, y + offset.y, z + offset.z, vx + velocity.x, vy + velocity.y, vz + velocity.z, angle, scale);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }

}
