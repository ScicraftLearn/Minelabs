package be.uantwerpen.minelabs.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BalloonEntity extends MobEntity {
    private static final double MAX_DISTANCE = 2.0;
    public static Set<UUID> balloons = new HashSet<>();

    private boolean liftoff = false;
    private int ballooned_id = 0;
    private LivingEntity ballooned = null;
    private UUID ballooned_uuid = null;
    private boolean helium = true;
    private float rotationY;

    public BalloonEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
        rotationY = getRandom().nextFloat() * 360.0F;
        setHealth(1.0f);
        // inanimate = true;
        setNoGravity(true);
        // TODO: For some reason, it allows multiple balloons after relogging
        balloons.add(getUuid());
    }

    public float getRotationY() {
        return rotationY;
    }

    public Vec3d getLeashOffset() {
        return new Vec3d(0.0D, (double)(0.15F * this.getHeight()), 0.01D);
    }

    public LivingEntity getBallooned() {
        if(ballooned == null && ballooned_id != 0 && world.isClient) {
            ballooned = (LivingEntity) world.getEntityById(ballooned_id);
        }
        if(ballooned == null && world instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.world).getEntity(ballooned_uuid);
            if(entity != null) {
                setBallooned((LivingEntity) entity, true);
            }
        }
        return ballooned;
    }

    public void unsetBallooned(boolean sendPacket) {
        ballooned = null;
        ballooned_id = 0;
        detachLeash(sendPacket, false);
        discard();
    }

    @Override
    public void onDeath(DamageSource source) {
        detachLeash(true, false);
        balloons.remove(getUuid());
        super.onDeath(source);
    }

    public void setBallooned(LivingEntity target, boolean sendUpdate) {
        ballooned_id = target.getId();
        ballooned_uuid = target.getUuid();
        ballooned = target;
        attachLeash((Entity) target, sendUpdate);
    }

    public void setHelium(boolean new_value) {
        helium = new_value;
    }

    public boolean getHelium() {
        return helium;
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (ballooned != null) {
            nbt.putUuid("Ballooned", ballooned.getUuid());
        }
        nbt.putBoolean("CanLiftOff", helium);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Ballooned")) {
            if(world instanceof ServerWorld) {
                ballooned_uuid = nbt.getUuid("Ballooned");
                System.out.println("NBT: " + getUuid());
                Entity entity = ((ServerWorld)this.world).getEntity(ballooned_uuid);
                if(entity != null) {
                    setBallooned((LivingEntity) entity, true);
                }
            }
        }
        if(nbt.contains("CanLiftOff")) {
            helium = nbt.getBoolean("CanLiftOff");
        }
    }

    @Override
    public boolean cannotDespawn() {
        // Prevents the balloon from disappearing when you're not looking
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        rotationY += 0.01F;
        addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 10, 3, false, false));
        LivingEntity target = getBallooned();
        if(target != null) {
            Vec3d mpos = getPos();
            if(mpos.getY() >= 300) {
                // POP GOES THE BALLOON
                unsetBallooned(true);
                return;
            }
            Vec3d tpos = target.getPos();
            double distance = tpos.distanceTo(mpos);
            if(distance >= MAX_DISTANCE) {
                double d = (tpos.getX() - this.getX()) / (double)distance;
                double e = (tpos.getY() - this.getY()) / (double)distance;
                double g = (tpos.getZ() - this.getZ()) / (double)distance;
                this.setVelocity(this.getVelocity().add(Math.copySign(d * d * 0.4D, d), Math.copySign(e * e * 0.4D, e), Math.copySign(g * g * 0.4D, g)));
//                Vec3d direction = new Vec3d(tpos.x - mpos.x, tpos.y - mpos.y, tpos.z - mpos.z).normalize();
//                setVelocity(direction.multiply(0.1));
                liftoff = true;
            }
            if(liftoff && helium) {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 3, 1, false, false));
            }
        }
    }
}