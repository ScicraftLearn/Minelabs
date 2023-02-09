package be.uantwerpen.minelabs.potion;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Objects;

public enum Molecule {
    // TODO: add lore with "reacts with stone, is toxic, is poisonous, ..."
    O2("O2", ParticleTypes.SPLASH,
            (world, x, y, z, blockHitResult) -> {
        BlockState blockState = world.getBlockState(blockHitResult.getBlockPos());
        System.out.println(blockState.getBlock());
        if (blockState.getBlock() == net.minecraft.block.Blocks.WATER)
            world.createExplosion(null, x, y, z, 2, false, Explosion.DestructionType.DESTROY);
        if (blockState.getBlock() == net.minecraft.block.Blocks.STONE)
            world.createExplosion(null, x, y, z, 2, false, Explosion.DestructionType.DESTROY);
        },
            (world, x, y, z, entityHitResult) -> {}
    ),
    N2("N2", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {
        BlockState blockState = world.getBlockState(blockHitResult.getBlockPos());
        for(Entity entity: world.getOtherEntities(null,  new Box(x-2,y-2,z-2,x+2,y+2,z+2)))
            if (entity instanceof LivingEntity livingEntity)
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1));
        if (blockState.getBlock() == Blocks.IRON_BLOCK) {
            world.createExplosion(null, x, y, z, 2, false, Explosion.DestructionType.DESTROY);
        }},
            (world, x, y, z, entityHitResult) -> {
        if (entityHitResult.getEntity() instanceof LivingEntity livingEntity)
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 400, 1));
        }
    ),
    CH4("CH4", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    H2("H2", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    NO("NO", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    NO2("NO2", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    CL2("CL2", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    CO2("CO2", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    CO("CO", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    NH3("NH3", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    N2O("N2O", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    HCL("HCl", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    HE("He", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    NE("Ne", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    AR("Ar", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    CL2O("Cl2O", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    BH3("BH3", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    HF("HF", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    SIH4("SiH4", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    PH3("PH3", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    H2S("H2S", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    CF4("CF4", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    BF3("BF3", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    BCL3("BCl3", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    SO2("SO2", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    CLF("ClF", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    F2("F2", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    CH4S("CH4S", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    CH2O("CH2O", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    H2CO3("F2", ParticleTypes.SOUL_FIRE_FLAME,
            (world, x, y, z, blockHitResult) -> {},
            (world, x, y, z, entityHitResult) -> {}
    ),
    ;

    interface BlockHit {
        void react(World world, double x, double y, double z, BlockHitResult hitResult);
    }
    interface EntityHit {
        void react(World world, double x, double y, double z, EntityHitResult hitResult);
    }

    private final BlockHit blockHit;
    private final EntityHit entityHit;
    private final DefaultParticleType particleType;
    private final String name;

    Molecule(String name, DefaultParticleType particleType, BlockHit blockHit, EntityHit entityHit) {
        assert !name.isEmpty();
        Objects.requireNonNull(blockHit);
        Objects.requireNonNull(entityHit);
        this.name = name;
        this.particleType = particleType;
        this.blockHit = blockHit;
        this.entityHit = entityHit;
    }

    public void react(World world, Vec3d position, HitResult hitResult) {
        Objects.requireNonNull(world);
        Objects.requireNonNull(position);
        Objects.requireNonNull(hitResult);
        if (hitResult instanceof BlockHitResult blockHitResult)
            blockHit.react(world, position.x, position.y, position.z, blockHitResult);
        else if(hitResult instanceof EntityHitResult entityHitResult)
            entityHit.react(world, position.x, position.y, position.z, entityHitResult);
    }

    public DefaultParticleType getParticleType() {
        return particleType;
    }

    @Override
    public String toString() {
        return name;
    }

}
