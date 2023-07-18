package be.minelabs.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CorrosiveEntity extends Entity {

    // Hold a reference to a blockpos
    private BlockPos blockPos;
    private int ticks = 0;
    private int blockHash = 0;
    private int posHash = 0;
    private double distance = 0;

    public CorrosiveEntity(EntityType<? extends Entity> type, World world) {
        super(type, world);
    }

    private CorrosiveEntity(World world, BlockPos blockPos, double distance) {
        this(Entities.CORROSIVE_ENTITY, world);
        this.blockPos = blockPos;
        this.blockHash = world.getBlockState(blockPos).hashCode();
        this.posHash = blockPos.hashCode();
        this.distance = distance;
    }

    public static CorrosiveEntity create(World world, BlockPos blockPos, double distance) {
        return new CorrosiveEntity(world, blockPos, distance);
    }


    // On every tick, set the blockbreakinginfo of the block at the blockpos to 5
    @Override
    public void tick() {
        super.tick();
        // If the block at the blockpos has turned into air, remove the entity
        if (this.blockPos != null && this.world.getBlockState(blockPos).isOf(net.minecraft.block.Blocks.AIR)) {
            this.world.setBlockBreakingInfo(this.getId(), this.blockPos, 0);
            this.remove(RemovalReason.DISCARDED);
        }
        if (this.blockPos != null) {
            ticks++;
            int progress = (int) (ticks / (3*(1+distance)));
            // If progress is 10, destroy the entity and the block, otherwise set the progress
            if (progress == 10) {
                this.remove(RemovalReason.DISCARDED);
                this.world.setBlockBreakingInfo(this.getId(), this.blockPos, 0);
                this.world.breakBlock(this.blockPos, true);
            } else {
                this.world.setBlockBreakingInfo(this.getId(), this.blockPos, progress);
            }
        }
    }

    @Override
    protected void initDataTracker() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        // TODO Auto-generated method stub
    }
}
