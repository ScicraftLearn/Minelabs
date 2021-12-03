package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.Scicraft;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.List;

public class EntropyCreeperEntity extends CreeperEntity {

    private final int explosionRadius = 5;

    public EntropyCreeperEntity(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Mixin Injected explosion method
     * Make sure to "kill" the entity
     *
     * Shuffle blocks in a 5 block radius
     *
     * @return boolean: cancel default explosion or not
     */
    public boolean preExplode() {
        this.dead = true;
        this.discard();
        //Scicraft.LOGGER.info("Entropy creeper exploded");
        Iterable<BlockPos> blockpos = BlockPos.iterateRandomly(this.random, (explosionRadius * explosionRadius * explosionRadius * 8),
                this.getBlockPos(), explosionRadius);

        List<BlockPos> blockPosList = Lists.newArrayList(blockpos);
        if (!this.world.isClient) {
            PlayerEntity player = world.getClosestPlayer(this.getX(), this.getY(), this.getZ(), explosionRadius, true);
            if (player != null) {
                BlockPos teleportpos = blockPosList.get(random.nextInt(blockPosList.size()));
                //Teleport player inside the explosionRadius
                player.teleport(teleportpos.getX(), teleportpos.getY(), teleportpos.getZ());
            }

            // Shuffle blocks in a 5 block radius
            if (world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                blockPosList.clear();
                for (BlockPos pos : blockpos) {
                    if (isShuffleable(pos)) {
                        blockPosList.add(pos.toImmutable());
                    }
                }
                for (BlockPos pos : blockPosList) {
                    if (isShuffleable(pos)) {
                        // Don't use blocks like barrier, air, bedrock
                        BlockPos newPos = blockPosList.get(random.nextInt(blockPosList.size()));
                        Scicraft.LOGGER.debug(world.getBlockState(pos) + " <-> " + world.getBlockState(newPos));
                        BlockState shuffle = world.getBlockState(pos);
                        world.setBlockState(pos, world.getBlockState(newPos));
                        world.setBlockState(newPos, shuffle);
                    }
                }
            }
        }
        return false;   // make sure the original 'explode' function doesn't run.
    }

    /**
     * Check if the block at pos is a shuffleable block
     *
     * @param pos : {@link BlockPos} position of block in the world
     * @return boolean, Block is not AIR or BEDROCK or BARRIER
     */
    private boolean isShuffleable(BlockPos pos) {
        return world.getBlockState(pos).getPistonBehavior() != PistonBehavior.BLOCK &&
                !world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK) &&
                !world.getBlockState(pos).isAir();
    }
}