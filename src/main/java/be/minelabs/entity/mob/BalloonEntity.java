package be.minelabs.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.io.Console;

public class BalloonEntity extends MobEntity {
    private static final double MAX_DISTANCE = 10.0;
    private static final double MAX_HEIGHT = 320.0;

    private final float rotationY;
    private boolean helium = true;

    private double max_mob_distance = 2.0;
    private Entity target = null;

    public BalloonEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
        rotationY = getRandom().nextFloat() * 360.0F;
        setHealth(1.0f);
        setNoGravity(true);
    }

    // GENERAL

    public float getRotationY() {
        return rotationY;
    }

    @Override
    public void onDeath(DamageSource source) {
        detachLeash(true, false);
        dropItem(Items.RABBIT_HIDE);
        super.onDeath(source);
    }

    public void setHelium(boolean new_value) {
        helium = new_value;
    }

    public boolean getHelium() {
        return helium;
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
        this.detachLeash(true, false);
    }

    @Override
    public void tick() {
        super.tick();

        if(helium) {
            addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 10, 3, false, false));
        }
        Entity target = this.target;  // Prevent crashes when the target becomes null during the tick
        if(target != null) {
            if(!target.isAlive()) {
                kill();
            }
            Vec3d mpos = getPos();
            if(mpos.getY() >= MAX_HEIGHT) {
                // POP GOES THE BALLOON
                kill();
                return;
            }
            Vec3d tpos = target.getPos();
            double distance = tpos.distanceTo(mpos);
            if(distance >= MAX_DISTANCE) {
                // Fixes Issue #406
                kill();
                return;
            }
            if(distance >= max_mob_distance) {
                double x = (tpos.getX() - this.getX()) / (double)distance;
                double z = (tpos.getZ() - this.getZ()) / (double)distance;

                // Fixes Issue #401
                Vec3d xz = new Vec3d(x, 0.0, z).normalize().multiply(0.1);
                this.setVelocity(xz);

                if(!(target instanceof LeashKnotEntity) && !(target instanceof MobEntity && ((MobEntity) target).isAiDisabled())) {
                    // The target might move around and should follow the levitation

                    if(this.target != null) {
                        // So apparently untamed rideables cannot be lifted in the air once ridden
                        //  There is no reason for this, they just... can't...
                        //  Let's just call this a feature :)

                        this.target.setVelocity(new Vec3d(-xz.x, 0.11, -xz.z));
                    }
                }
            }
        }
    }

    // LEASH
    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Override
    public void attachLeash(Entity entity, boolean sendPacket) {
        // By attaching a leash to the target, all positions appear to be valid
        // Fixes Issues #404 and #399
        target = entity;
        if(target instanceof LeashKnotEntity) {
            max_mob_distance = 2.0;
        } else if (target instanceof MobEntity tgt) {
            if(!tgt.isLeashed()) {
                tgt.attachLeash(this, sendPacket);
            }

            // Longer leashes for larger mobs
            Box bbox = entity.getBoundingBox();
            double a = bbox.getXLength();
            double b = bbox.getYLength();
            double c = bbox.getZLength();
            max_mob_distance = Math.sqrt(a * a + b * b + c * c);
        }
    }

    @Override
    public void detachLeash(boolean sendPacket, boolean dropItem) {
        super.detachLeash(sendPacket, dropItem);
        target = null;
    }

    public Vec3d getLeashOffset() {
        return new Vec3d(0.0D, (double)(0.15F * this.getHeight()), 0.01D);
    }

    @Override
    public boolean isLeashed() {
        return true;
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("CanLiftOff", helium);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if(nbt.contains("CanLiftOff")) {
            helium = nbt.getBoolean("CanLiftOff");
        }
    }
}