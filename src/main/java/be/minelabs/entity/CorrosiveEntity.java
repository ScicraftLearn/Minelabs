package be.minelabs.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class CorrosiveEntity extends Entity {

    // Hold a reference to a blockpos
    private BlockPos blockPos;
    private int ticks = 0;
    private double distance = 0;

    public CorrosiveEntity(EntityType<? extends Entity> type, World world) {
        super(type, world);
    }

    private CorrosiveEntity(World world, BlockPos blockPos, double distance) {
        this(Entities.CORROSIVE_ENTITY, world);
        this.blockPos = blockPos;
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
            this.world.setBlockBreakingInfo(this.getId(), this.blockPos, -1);
            this.remove(RemovalReason.DISCARDED);
        }
        if (this.blockPos != null) {
            ticks++;
            // Get the hardness of the block at the blockpos
            float hardness = this.world.getBlockState(blockPos).getHardness(this.world, blockPos);
            // The formula below can be modified to weigh the progress more towards the distance, the hardness, or the general case
            int progress = (int) (ticks / ((1+distance)*(1+hardness)));
            // If progress is 10, destroy the entity and the block, otherwise set the progress
            if (progress == 10) {
                this.remove(RemovalReason.DISCARDED);
                this.world.setBlockBreakingInfo(this.getId(), this.blockPos, -1);
                this.world.breakBlock(this.blockPos, this.world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS));
            } else {
                this.world.setBlockBreakingInfo(this.getId(), this.blockPos, progress);
            }
        }
    }

    @Override
    protected void initDataTracker() {
        // No Data to be tracked
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        ticks = nbt.getInt("minelabs.time");
        distance = nbt.getDouble("minelabs.distance");
        blockPos = NbtHelper.toBlockPos(nbt.getCompound("minelabs.pos"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("minelabs.time", ticks);
        nbt.putDouble("minelabs.distance", distance);
        nbt.put("minelabs.pos", NbtHelper.fromBlockPos(blockPos));
    }
}
