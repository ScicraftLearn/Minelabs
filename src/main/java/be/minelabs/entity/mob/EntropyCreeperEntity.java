package be.minelabs.entity.mob;

import be.minelabs.Minelabs;
import be.minelabs.item.reaction.Reaction;
import be.minelabs.mixin.ExplosionAccessor;
import be.minelabs.sound.SoundEvents;
import be.minelabs.util.Tags;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EntropyCreeperEntity extends CreeperEntity {

    // Total amount of ticks the animation runs for.
    private static final int ANIMATION_TICKS = 10;

    // Portion of affected blocks to shuffle
    private static final double SHUFFLE_PERCENTAGE = 0.25;

    private static final int EXPLOSION_RADIUS = 3;

    private final List<BlockPos> blocksToShuffle = new ArrayList<>();
    private final List<LivingEntity> entitiesToShuffle = new ArrayList<>();

    // After it exploded, how many ticks to shuffle for.
    private int ticksToGo = -1;

    public EntropyCreeperEntity(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ENTROPY_CREEPER_HURT;
    }

    /**
     * Called due to mixin
     */
    protected void playPrimedSound() {
        this.playSound(SoundEvents.ENTITY_ENTROPY_CREEPER_PRIMED, 1.0f, 1.0f);
    }

    /**
     * Override Tick()
     * What to do while exploding
     */
    @Override
    public void tick() {
        if (ticksToGo >= 0) {
            if (ticksToGo % 5 == 0) {
                shuffle();
            }
            if (ticksToGo == 0) {
                discard();
            }
            ticksToGo--;
        } else {
            super.tick();
        }
    }

    /**
     * Mixin Injected explosion method
     * Make sure to "kill" the entity
     * <p>
     * Shuffle blocks in the explosion radius around the entity.
     *
     * @return boolean: cancel default explosion or not
     */
    public boolean preExplode() {
        if (!this.world.isClient) {
            dead = true;
            this.setInvulnerable(true);
            setInvisible(true);     // so we can't see it (discard happens after animation)
            // TODO: entity can still be attacked in this state.
            //  Either prevent this or have the entropy creeper spawn an entropy bomb upon explode?

            this.playSound(SoundEvents.ENTITY_ENTROPY_CREEPER_EXPLODE, 1.0f, 1.0f);

            // Use explosion code to determine affected blocks
            Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.KEEP;
            // if creeper is charged
            float multiplier = this.shouldRenderOverlay() ? 2.0f : 1.0f;
            Explosion explosion = new Explosion(this.world, this, null, null, getX(), getY(), getZ(), EXPLOSION_RADIUS * multiplier, false, destructionType);

            // Adapted from the Explosion class
            Set<BlockPos> blockposSet = Reaction.Utils.getAffectedBlocks(world, (ExplosionAccessor) explosion).stream()
                    .filter(pos -> isShuffleable(world.getBlockState(pos)))
                    .collect(Collectors.toSet());
            blocksToShuffle.addAll(blockposSet);

            Collection<LivingEntity> entities = Reaction.Utils.getAffectedEntities(world, (ExplosionAccessor) explosion);
            entitiesToShuffle.addAll(entities);

            shuffle();
            ticksToGo = ANIMATION_TICKS - 1;
        }
        return false;  // make sure the original 'explode' function doesn't run.
    }

    /**
     * Actual shuffle of the Entropy Creeper
     * <p>
     * Teleports the Entities (+ Colors sheep in random color)
     * Shuffles the blocks
     */
    private void shuffle() {
        if (this.world.isClient) return;

        // Teleport affected entities
        for (LivingEntity entity : entitiesToShuffle) {
            if (entity != null) {
                if (entity instanceof SheepEntity sheep) {
                    DyeColor old = sheep.getColor();
                    sheep.setColor(DyeColor.byId(random.nextInt(15)));
                    Minelabs.LOGGER.debug(old + " -> " + sheep.getColor());
                }
                BlockPos teleportpos = blocksToShuffle.get(random.nextInt(blocksToShuffle.size()));
                Minelabs.LOGGER.debug("teleport to: " + teleportpos);
                entity.teleport(teleportpos.getX(), teleportpos.getY(), teleportpos.getZ());
            }
        }

        // Shuffle blocks
        Minelabs.LOGGER.debug("size: " + blocksToShuffle.size());
        if (world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            for (int i = 0; i < blocksToShuffle.size() * SHUFFLE_PERCENTAGE; i++) {
                BlockPos pos1 = blocksToShuffle.get(random.nextInt(blocksToShuffle.size()));
                BlockPos pos2 = blocksToShuffle.get(random.nextInt(blocksToShuffle.size()));

                BlockState b1 = world.getBlockState(pos1);
                BlockState b2 = world.getBlockState(pos2);
                if (b1.getPistonBehavior() == PistonBehavior.DESTROY) {
                    world.breakBlock(pos1, true, this);
                    b1 = Blocks.AIR.getDefaultState();
                }
                if (b2.getPistonBehavior() == PistonBehavior.DESTROY) {
                    world.breakBlock(pos2, true, this);
                    b2 = Blocks.AIR.getDefaultState();
                }
                Minelabs.LOGGER.debug(b1 + " <-> " + b2);

                // prepare blockstates for their placement position
                b1 = Block.postProcessState(b1, world, pos2);
                b2 = Block.postProcessState(b2, world, pos1);
                world.setBlockState(pos1, b2, Block.NOTIFY_LISTENERS | Block.MOVED);
                world.setBlockState(pos2, b1, Block.NOTIFY_LISTENERS | Block.MOVED);
            }
            blocksToShuffle.forEach(pos -> world.updateNeighbors(pos, world.getBlockState(pos).getBlock()));
        }
    }

    /**
     * Check if the block is a shuffleable block
     *
     * @param blockState: block
     * @return whether the block should be movable.
     */
    private boolean isShuffleable(BlockState blockState) {
        return blockState.getBlock().getHardness() > -1 &&
                !blockState.isIn(BlockTags.DRAGON_IMMUNE) &&
                !blockState.isIn(Tags.Blocks.ENTROPY_IMMUNE);
    }
}