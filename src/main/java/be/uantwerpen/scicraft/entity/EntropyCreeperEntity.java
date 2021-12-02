package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.mixins.CreeperEntityMixin;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
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
                Iterable<BlockPos> blockpos = BlockPos.iterateRandomly(this.random, (explosionRadius^2), this.getBlockPos(), explosionRadius);
                List<BlockPos> blockPosList = Lists.newArrayList(blockpos);

                PlayerEntity player = world.getClosestPlayer(this.getX(), this.getY(), this.getZ(), (explosionRadius^2), true);
                if (player != null){
                    BlockPos teleportpos = blockPosList.get(random.nextInt(explosionRadius^2));
                    //Teleport player inside the explosionRadius
                    player.teleport(teleportpos.getX(), teleportpos.getY(), teleportpos.getZ());
                }

                for (BlockPos pos: blockpos) {
                    if (world.getBlockState(pos).getBlock() != Blocks.BARRIER && 
                            world.getBlockState(pos).getBlock() != Blocks.AIR &&
                            world.getBlockState(pos).getBlock() != Blocks.BEDROCK && world.getBlockState(pos) == null){
                        Scicraft.LOGGER.info("Shuffleable block");
                        // Don't use blocks like barrier, air, bedrock and any block with BlockStates (furnace,chest,..)
                        // BOOM !!

                    }
                }
            }

        }
        return false;   // make sure the original 'explode' function doesn't run.
    }

}
