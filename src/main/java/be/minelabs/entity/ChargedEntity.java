package be.minelabs.entity;

import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ChargedEntity extends Entity {
    private int charge;

    public ChargedEntity(EntityType<?> type, World world) {
        super(type, world);
        charge = 0;
    }

    public ChargedEntity(World world, BlockPos pos, int charge) {
        this(Entities.CHARGED_ENTITY, world);
        setPosition(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
        this.charge = charge;
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
        return super.damage(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        //TODO move to/away other charged entities depending on the field
    }

    @Override
    protected void initDataTracker() {
        this.setNoGravity(true);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        charge = nbt.getInt("charge");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("charge", charge);
    }


}
