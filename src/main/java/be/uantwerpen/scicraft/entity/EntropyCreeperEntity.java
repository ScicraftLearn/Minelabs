package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.Scicraft;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.ArrayList;
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
        Scicraft.LOGGER.debug("Entropy creeper exploded");
        BlockPos center = this.getBlockPos();
        Iterable<BlockPos> blockpos = BlockPos.iterateRandomly(this.random, (explosionRadius * explosionRadius * explosionRadius * 8),
                center, explosionRadius);

        List<BlockPos> blockPosList = new ArrayList<>();
        for (BlockPos pos : blockpos) {
            if (isShuffleable(world.getBlockState(pos))) {
                blockPosList.add(pos.toImmutable());
            }
        }

        Box area = new Box(center.getX() - explosionRadius, center.getY() - explosionRadius, center.getZ() - explosionRadius,
                center.getX() + explosionRadius, center.getY() + explosionRadius, center.getZ() + explosionRadius);

        if (!this.world.isClient) {
            List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, area, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR);
            for(LivingEntity entity: entities) {
                if (entity != null) {
                    BlockPos teleportpos = blockPosList.get(random.nextInt(blockPosList.size()));
                    //Teleport mob inside the explosionRadius
                    entity.teleport(teleportpos.getX(), teleportpos.getY(), teleportpos.getZ());
                }
            }

            // Shuffle blocks in a 5 block radius
            if (world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                for (BlockPos pos : blockPosList) {
                    BlockPos newPos = blockPosList.get(random.nextInt(blockPosList.size()));
                    Scicraft.LOGGER.debug(world.getBlockState(pos) + " <-> " + world.getBlockState(newPos));
                    BlockState shuffle = world.getBlockState(pos);
                    world.setBlockState(pos, world.getBlockState(newPos));
                    world.setBlockState(newPos, shuffle);
                }
            }
        }
        return false;   // make sure the original 'explode' function doesn't run.
    }

    /**
     * Check if the block is a shuffleable block
     *
     * @param blockState: block
     * @return whether the block should be movable.
     */
    private boolean isShuffleable(BlockState blockState) {
        return !BlockTags.DRAGON_IMMUNE.contains(blockState.getBlock());
    }
}