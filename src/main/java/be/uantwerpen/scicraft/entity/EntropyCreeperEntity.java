package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.mixins.CreeperEntityMixin;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
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
        if (!this.world.isClient) {
            this.dead = true;
            this.discard();
            Scicraft.LOGGER.info("Entropy creeper exploded");
            // TODO: Custom exposion
            // Shuffle blocks in a 5 block radius
            if (world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)){
                Iterable<BlockPos> blockpos = BlockPos.iterateRandomly(this.random, explosionRadius*explosionRadius*explosionRadius,
                        this.getBlockPos(), explosionRadius);
                List<BlockPos> blockPosList = Lists.newArrayList(blockpos);

                PlayerEntity player = world.getClosestPlayer(this.getX(), this.getY(), this.getZ(), explosionRadius, true);
                if (player != null){
                    BlockPos teleportpos = blockPosList.get(random.nextInt(blockPosList.size()));
                    //Teleport player inside the explosionRadius
                    player.teleport(teleportpos.getX(), teleportpos.getY(), teleportpos.getZ());
                }

                blockPosList.clear();
                for (BlockPos pos: blockpos) { //Supposed to only have shuffleable blocks
                    if (isShuffleable(pos)){
                        blockPosList.add(pos);
                    }
                }

                for (BlockPos pos: blockPosList) {
                    if (isShuffleable(pos)){
                        // Don't use blocks like barrier, air, bedrock and any block with BlockStates (furnace,chest,..)
                        // BOOM !!
                        BlockPos newPos = blockPosList.get(random.nextInt(blockPosList.size()));
                        //Scicraft.LOGGER.info(world.getBlockState(pos) + " -> " +world.getBlockState(newPos));
                        world.setBlockState(pos, world.getBlockState(newPos));
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
    private boolean isShuffleable(BlockPos pos){
        //TODO check for bedrock
        return  world.getBlockState(pos).getPistonBehavior() != PistonBehavior.BLOCK &&
                !world.getBlockState(pos).isAir();


    }
}