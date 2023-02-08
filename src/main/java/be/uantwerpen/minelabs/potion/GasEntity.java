package be.uantwerpen.minelabs.potion;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.item.Items;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

// TODO: Make "Fabric"-like
public class GasEntity extends PotionEntity { // TODO: better to extends from ThrownItemEntity, but the throw animation is buggy

    // TODO: opt to make this an enum that will handle the reactions
    private final String molecule;

    public GasEntity(World world, LivingEntity player, String molecule) {
//        super(EntityType.POTION, player, world);
        super(world, player);
        this.molecule = molecule;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ERLENMEYER;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        // TODO: action based on molecule
        BlockState blockState = this.world.getBlockState(blockHitResult.getBlockPos());
        if (blockState.getMaterial() == Material.STONE) {
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), 2, false, Explosion.DestructionType.DESTROY);
        }
        if (blockState.getMaterial() == Material.METAL) {
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), 4, true, Explosion.DestructionType.DESTROY);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        // This is from ProjectileEntity.onCollision (super.super.onCollision)
        if (hitResult instanceof BlockHitResult) {
            this.onBlockHit((BlockHitResult) hitResult);
        }
        if (!this.world.isClient) {
            for (int i = 0; i < 20; i++) {
                double offsetX = (Math.random() - 0.5) * 0.5;
                double offsetY = (Math.random() - 0.5) * 0.5;
                double offsetZ = (Math.random() - 0.5) * 0.5;
                // TODO: Add custom particles https://fabricmc.net/wiki/tutorial:particles
                // TODO: Add colors based on molecule
                MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.SPLASH,
                        this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ,
                        0, 0, 0);
            }
            this.discard();
        }
    }
}
