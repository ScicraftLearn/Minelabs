package be.uantwerpen.minelabs.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BohrBlueprintEntity extends AbstractDecorationEntity {
    public BohrBlueprintEntity(EntityType<? extends BohrBlueprintEntity> entityType, World world) {
        super(entityType, world);
        setFacing(Direction.UP);
    }

    public BohrBlueprintEntity(World world, BlockPos pos) {
        super(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE, world, pos);
        setFacing(Direction.UP);
    }

    @Override
    public int getWidthPixels() {
        return 16;
    }

    @Override
    public int getHeightPixels() {
        return 16;
    }

    @Override
    public void onBreak(@Nullable Entity entity) {

    }

    @Override
    public void onPlace() {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, this.facing.getId(), this.getDecorationBlockPos());
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.setFacing(Direction.byId(packet.getEntityData()));
    }

}
