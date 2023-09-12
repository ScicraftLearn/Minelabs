package be.minelabs.entity;

import be.minelabs.block.Blocks;
import be.minelabs.item.Items;
import be.minelabs.sound.SoundEvents;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ChargedEntity extends ThrownEntity {
    private static final TrackedData<Integer> CHARGE = DataTracker.registerData(ChargedEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private float mass;

    private final static int e_radius = 12;

    public ChargedEntity(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Summon/Spawn a new Entity
     *
     * @param world  : in what world should we make the entity
     * @param pos    : position in the world to spawn the entity
     * @param charge : what charge should it have
     * @param mass   : what's the mass
     */
    public ChargedEntity(World world, BlockPos pos, int charge, float mass) {
        this(Entities.CHARGED_ENTITY, world);
        setPosition(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
        setCharge(charge);
        this.mass = mass;
    }

    /**
     * Make a new Thrown Entity
     *
     * @param owner  : who threw the Entity/Item
     * @param world  : what world did we do this in
     * @param charge : what charge did the item have (and should the entity have)
     * @param mass   : what's the mass of the item (and should the entity have)
     */
    public ChargedEntity(LivingEntity owner, World world, int charge, float mass) {
        super(Entities.CHARGED_ENTITY, owner, world);
        setCharge(charge);
        this.mass = mass;
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }

    @Override
    public boolean canAvoidTraps() {
        // so it ignores tripwires and pressure plates.
        return true;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        this.discard();
        return true;
    }

    @Override
    public void tick() {
        if (world.isClient) {
            super.tick();
            return;
        }

        Iterable<BlockPos> positions = BlockPos.iterateOutwards(getBlockPos(), e_radius, e_radius, e_radius);
        for (BlockPos pos : positions) {
            if (world.getBlockState(pos).isOf(Blocks.TIME_FREEZE_BLOCK)) {
                //"Force" a stop
                updateVelocity(5f, Vec3d.ZERO);

                super.tick();
                return;
            }
        }

        List<Entity> entities = world.getOtherEntities(this,
                Box.of(this.getPos(), e_radius, e_radius, e_radius), entity -> entity instanceof ChargedEntity);

        for (Entity entity : entities) {
            if (entity instanceof ChargedEntity chargedEntity) {
                double force = 8.987f * getCharge() * chargedEntity.getCharge() / squaredDistanceTo(chargedEntity);
                Vec3d vector = getPos().subtract(chargedEntity.getPos()).normalize(); // Vector between entities
                vector = vector.multiply(force / mass); //scale vector with Force and mass of atom
                vector = vector.multiply(0.0001);
                if (getVelocity().length() < 10) {
                    addVelocity(vector);
                }
            }
        }
        tryCheckBlockCollision();
        super.tick();
    }

    /**
     * Entity collision
     *
     * @param entityHitResult
     */
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof ChargedEntity charged) {
            // Could do way more with this!
            // do annihilation : gives 2 photons per charge
            if (charged.getCharge() == -this.getCharge()) {
                this.discard();
                charged.discard();
                ItemScatterer.spawn(getWorld(), getBlockPos(),
                        DefaultedList.copyOf(ItemStack.EMPTY, new ItemStack(Items.PHOTON, 2 * getCharge())));

            }
        }
    }

    @Override
    protected void initDataTracker() {
        this.setNoGravity(true);
        dataTracker.startTracking(CHARGE, 0);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        dataTracker.set(CHARGE, nbt.getInt("charge"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("charge", dataTracker.get(CHARGE));
    }

    public int getCharge() {
        return dataTracker.get(CHARGE);
    }

    public void setCharge(int charge) {
        dataTracker.set(CHARGE, charge);
    }
}
