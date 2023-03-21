package be.uantwerpen.minelabs.entity;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BohrBlueprintEntity extends Entity {

    private int obstructionCheckCounter = 0;

    public BohrBlueprintEntity(EntityType<? extends BohrBlueprintEntity> entityType, World world) {
        super(entityType, world);
    }

    public BohrBlueprintEntity(World world, BlockPos pos) {
        super(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE, world);
        setPosition(Vec3d.ofCenter(pos));
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.5f * dimensions.height;
    }

    @Override
    public void tick() {
        if (this.world.isClient)
            return;

        if (this.isRemoved())
            return;

        this.attemptTickInVoid();
        if (this.obstructionCheckCounter++ == 100) {
            this.obstructionCheckCounter = 0;
            if (!this.isAttachedToBlock()) {
                this.discard();
            }
        }
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    public boolean isAttachedToBlock() {
        return world.getBlockState(getBlockPos().down()).isOf(Blocks.BOHR_BLOCK);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

}
