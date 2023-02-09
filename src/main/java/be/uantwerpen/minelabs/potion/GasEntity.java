package be.uantwerpen.minelabs.potion;

import be.uantwerpen.minelabs.item.Items;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// TODO: Make "Fabric"-like
public class GasEntity extends PotionEntity { // TODO: better to extends from ThrownItemEntity, but the throw animation is buggy

    private final Molecule molecule;

    public GasEntity(World world, LivingEntity player, Molecule molecule) {
        super(world, player);
        this.molecule = molecule;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ERLENMEYER;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        molecule.react(this.world, this.getPos(), hitResult);
        if (!this.world.isClient) {
            for (int i = 0; i < 20; i++) {
                double offsetX = (Math.random() - 0.5) * 0.5;
                double offsetY = (Math.random() - 0.5) * 0.5;
                double offsetZ = (Math.random() - 0.5) * 0.5;
                // TODO: Add custom particles https://fabricmc.net/wiki/tutorial:particles
                // TODO: Add colors based on molecule
                MinecraftClient.getInstance().particleManager.addParticle(molecule.getParticleType(),
                        this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ,
                        0, 0, 0);
            }
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isTouchingWater()) {
            // TODO: Sketchy, replace this
            BlockHitResult blockHitResult = new BlockHitResult(this.getPos(), Direction.UP, this.getBlockPos(), false);
            molecule.react(this.world, this.getPos(), blockHitResult);
            this.kill();
        }
    }
}
