package be.minelabs.entity.projectile.thrown;

import be.minelabs.entity.BohrBlueprintEntity;
import be.minelabs.entity.Entities;
import be.minelabs.item.Items;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public abstract class ChargedEntity extends ThrownItemEntity {
    private static final TrackedData<Integer> CHARGE = DataTracker.registerData(ChargedEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private Vec3d field = Vec3d.ZERO;

    private static final int MAX_FIELD = 5;

    public final static int e_radius = 12;
    public static final float DEFAULT_SPEED = 0.3f;


    public ChargedEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Summon/Spawn a new Entity
     *
     * @param world : in what world should we make the entity
     * @param pos   : position in the world to spawn the entity
     * @param stack : Item used for throwing
     */
    public ChargedEntity(EntityType<? extends ThrownItemEntity> type, World world, BlockPos pos, ItemStack stack) {
        this(type, world);
        setPosition(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
        setItem(stack);
    }

    /**
     * Make a new Thrown Entity
     *
     * @param owner : who threw the Entity/Item
     * @param world : what world did we do this in
     * @param stack : Item used for throwing
     */
    public ChargedEntity(EntityType<? extends ThrownItemEntity> entityType, LivingEntity owner, World world, ItemStack stack) {
        super(entityType, owner, world);
        setItem(stack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ELECTRON;
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
    public boolean canUsePortals() {
        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        this.kill();
        return true;
    }

    @Override
    public void kill() {
        if (getItem() == null || getItem().isOf(net.minecraft.item.Items.AIR)) {
            world.spawnEntity(new ItemEntity(world, getX(), getY() + 0.2, getZ(), new ItemStack(getDefaultItem())));
        } else {
            world.spawnEntity(new ItemEntity(world, getX(), getY() + 0.2, getZ(), getItem()));
        }
        super.kill();
    }

    @Override
    public void tick() {
        List<Entity> entities = world.getOtherEntities(this,
                Box.of(this.getPos(), e_radius, e_radius, e_radius), entity -> entity instanceof ChargedEntity);

        field = Vec3d.ZERO;

        for (Entity entity : entities) {
            if (entity instanceof ChargedEntity charged) {
                double force = 8.987f * getCharge() * charged.getCharge() / squaredDistanceTo(charged);
                Vec3d vector = getPos().subtract(charged.getPos()).normalize(); // Vector between entities
                vector = vector.multiply(force); //scale vector with Force
                vector = vector.multiply(0.0001);

                field = vector.multiply(MAX_FIELD / vector.length()); // SCALE TO MAX_FIELD
            }
        }

        super.tick();
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (world.isClient)
            return;
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof BohrBlueprintEntity bohr) {
            bohr.onParticleCollision(this);
        } else if (entity instanceof CreeperEntity creeperEntity) {
            creeperEntity.setInvulnerable(true);
            creeperEntity.onStruckByLightning((ServerWorld) world, new LightningEntity(EntityType.LIGHTNING_BOLT, world));
            creeperEntity.extinguish();
            creeperEntity.setInvulnerable(false);
            this.discard();
        }

        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(CHARGE, 0);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        dataTracker.set(CHARGE, nbt.getInt("charge"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("charge", dataTracker.get(CHARGE));
    }

    protected int getCharge() {
        return dataTracker.get(CHARGE);
    }

    protected void setCharge(int charge) {
        dataTracker.set(CHARGE, charge);
    }

    public Vec3d getField() {
        return field;
    }
}
