package be.minelabs.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BalloonEntity extends MobEntity {
    private static final double MAX_DISTANCE = 10.0;
    private static final double MAX_HEIGHT = 320.0;
    public static final double LEVITATION_SPEED = 0.11;

    private boolean helium = true;

    private double max_mob_distance = 2.0;
    private Entity owner = null;

    @Nullable
    private NbtCompound ownerNbt;

    public BalloonEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
        setHealth(1.0F);
        setNoGravity(true);
    }

    // GETTERS AND SETTERS
    public void setHelium(boolean new_value) {
        helium = new_value;
    }

    public boolean getHelium() {
        return helium;
    }

    public Entity getOwner() {
        return owner;
    }


    // GENERAL
    public void heal(float amount) {}

    @Override
    public void onDeath(DamageSource source) {
        detachOwner();
        super.onDeath(source);
    }

    protected void initGoals() {
        // Fixes #398
        this.goalSelector.add(0, new SwimGoal(this));
    }

    @Override
    public boolean canBreatheInWater() {
        // Fixes issue #397
        return true;
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        // Fixes Issue #407
        return false;
    }

    @Override
    protected void removeFromDimension() {
        super.removeFromDimension();
        this.detachOwner();
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isClient) {
            updateOwner();
        }

        if(helium) {
            setVelocity(new Vec3d(0.0, LEVITATION_SPEED, 0.0));
        }
        Entity target = owner;  // Prevent crashes when the target becomes null during the tick
        if(target != null) {
            if(getY() >= MAX_HEIGHT) {
                // POP GOES THE BALLOON
                kill();
                return;
            }
            Vec3d tpos = target.getPos();
            double distance = tpos.distanceTo(getPos());
            if(distance >= MAX_DISTANCE) {
                // Fixes Issue #406
                kill();
                return;
            }

            // Stop the jiggle
            double x = tpos.getX() - this.getX();
            double z = tpos.getZ() - this.getZ();
            double d2 = Math.sqrt(x*x+z*z);
            double factor;
            if(d2 < 0.2) {
                factor = 0;
            } else if(d2 <= 1) {
                factor = d2 * d2;
            } else {
                factor = Math.sqrt(d2);
            }
            Vec3d xz = new Vec3d(x, 0.0, z).normalize().multiply(factor/10);
            addVelocity(xz);

            if(distance >= max_mob_distance) {
                if(!(target instanceof LeashKnotEntity) && !(target instanceof MobEntity && ((MobEntity) target).isAiDisabled())) {
                    // So apparently untamed rideables cannot be lifted in the air once saddled
                    //  There is no reason for this, they just... can't...
                    //  Let's just call this a feature for now...
                    if(getY() - tpos.getY() > max_mob_distance / 2.0) {
                        target.setVelocity(new Vec3d(0.0, LEVITATION_SPEED, 0.0));
                        target.onLanding();  // prevent infinite fall damage accumulation
                    }
                } else {
                    // Don't go up anymore
                    Vec3d vel = getVelocity();
                    setVelocity(vel.x, 0.0, vel.z);
                }
            }
        } else {
            kill();
        }
    }

    // LEASH & OWNER
    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    public Vec3d getLeashOffset() {
        return new Vec3d(0.0D, (double)(0.15F * this.getHeight()), 0.01D);
    }

    @Override
    public boolean isLeashed() {
        return true;
    }

    protected void updateOwner() {
        if (ownerNbt != null) {
            readOwnerNbt();
        }

        if(getHoldingEntity() != null) {
            owner = getHoldingEntity();
            updateLeash();
        }

        if (owner != null) {
            if (!isAlive() || !owner.isAlive()) {
                detachOwner();
                kill();
            }
        }
    }

    public void attachOwner(Entity entity) {
        // By attaching a leash to the target, all positions appear to be valid
        // Fixes Issues #404 and #399
        owner = entity;
        if (!this.world.isClient && world instanceof ServerWorld) {
            // Send this to update all data?
            attachLeash(owner, true);
        }

        if(owner != null) {
            ownerNbt = null;
            if(owner instanceof LeashKnotEntity) {
                max_mob_distance = 2.0;
            } else if (owner instanceof MobEntity) {
                // Longer leashes for larger mobs
                Box bbox = entity.getBoundingBox();
                double a = bbox.getXLength();
                double b = bbox.getYLength();
                double c = bbox.getZLength();
                max_mob_distance = 1.3 * Math.sqrt(a * a + b * b + c * c);
            }
        }
    }

    public void detachOwner() {
        detachLeash(true, false);
        if(owner != null && owner instanceof LeashKnotEntity) {
            owner.discard();
        }
        owner = null;
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("CanLiftOff", helium);
        if(owner != null) {
            NbtCompound nbtCompound = new NbtCompound();
            if(owner instanceof LeashKnotEntity) {
                BlockPos blockPos = ((LeashKnotEntity)owner).getDecorationBlockPos();
                nbtCompound.putInt("X", blockPos.getX());
                nbtCompound.putInt("Y", blockPos.getY());
                nbtCompound.putInt("Z", blockPos.getZ());
            } else {
                UUID uUID = owner.getUuid();
                nbtCompound.putUuid("UUID", uUID);
            }

            nbt.put("Owner", nbtCompound);
        } else if (ownerNbt != null) {
            nbt.put("Owner", ownerNbt.copy());
        }
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if(nbt.contains("CanLiftOff")) {
            helium = nbt.getBoolean("CanLiftOff");
        }
        if(nbt.contains("Owner", 10)) {
            ownerNbt = nbt.getCompound("Owner");
        }
    }

    private void readOwnerNbt() {
        if (ownerNbt != null && this.world instanceof ServerWorld) {
            if (ownerNbt.containsUuid("UUID")) {
                UUID uUID = ownerNbt.getUuid("UUID");
                Entity entity = ((ServerWorld)this.world).getEntity(uUID);
                if (entity != null) {
                    attachOwner(entity);
                }
            } else if (ownerNbt.contains("X", 99) && ownerNbt.contains("Y", 99) && ownerNbt.contains("Z", 99)) {
                BlockPos blockPos = NbtHelper.toBlockPos(ownerNbt);
                attachOwner(LeashKnotEntity.getOrCreate(this.world, blockPos));
            }
        }
    }
}