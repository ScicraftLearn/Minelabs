package be.minelabs.item.reaction;

import be.minelabs.mixin.ExplosionAccessor;
import be.minelabs.util.Tags;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class Reaction {
    // TODO CHECK VEC3D pos


    private final List<TagKey> whiteList = new ArrayList<>();
    private final List<TagKey> blackList = new ArrayList<>(List.of(Tags.Blocks.REACTION_DEFAULT_BLACKLIST));

    public Reaction addToBlackList(TagKey key) {
        blackList.add(key);
        return this;
    }

    public Reaction addToWhiteList(TagKey key) {
        whiteList.add(key);
        return this;
    }

    protected boolean canReact(BlockState state) {
        Objects.requireNonNull(state);
        return whiteList.stream().allMatch(state::isIn) &&
                blackList.stream().noneMatch(state::isIn);
    }

    public void react(World world, Vec3d position, HitResult hitResult) {
        Objects.requireNonNull(world);
        Objects.requireNonNull(position);
        Objects.requireNonNull(hitResult);
        if (hitResult instanceof BlockHitResult blockHitResult)
            react(world, blockHitResult.getPos());
        else if (hitResult instanceof EntityHitResult entityHitResult)
            react(world, entityHitResult.getEntity().getPos());
    }

    protected abstract void react(World world, Vec3d sourcePos);

    public abstract void react(LivingEntity entity);

    public abstract Text getTooltipText();

    public MutableText getTooltipText(String reaction){
        return Text.translatable("reaction.minelabs." + reaction);
    }

    public static class Utils {

        static boolean isFlammable(BlockState state) {
            return state.isIn(Tags.Blocks.FLAMMABLE_BLOCKS)
                    || (state.getProperties().contains(Properties.LIT) && state.get(Properties.LIT))
                    && !state.isIn(Tags.Blocks.FLAMMABLE_BLACKLIST);
        }

        public static void applyRadius(Vec3d centerPos, int radius, Consumer<Vec3d> blockFunction) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        Vec3d vec = new Vec3d(centerPos.x + x, centerPos.y + y, centerPos.z + z);
                        if (Math.sqrt(centerPos.squaredDistanceTo(vec)) <= radius) {
                            blockFunction.accept(vec);
                        }
                    }
                }
            }
        }

        public static void applyRadius(World world, Vec3d position, int radius, Consumer<LivingEntity> entityFunction) {
            double x = position.getX(), y = position.getY(), z = position.getZ();
            for (Entity entity : world.getOtherEntities(null, new Box(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius)))
                if (entity instanceof LivingEntity livingEntity)
                    entityFunction.accept(livingEntity);
        }

        /**
         * Calls blockFunction for blocks based on explosion with radius.
         * Note: does not check canReact!
         */
        public static void applyBlocksRadiusTraced(World world, Vec3d centerPos, int radius, Consumer<BlockPos> blockFunction){
            Explosion.DestructionType destructionType = world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.KEEP;
            Explosion explosion = new Explosion(world, null, null, null, centerPos.x, centerPos.y, centerPos.z, radius, false, destructionType);
            getAffectedBlocks(world, (ExplosionAccessor) explosion).forEach(blockFunction);
        }

        public static void applyEntitiesRadiusTraced(World world, Vec3d centerPos, int radius, Consumer<LivingEntity> entityFunction){
            Explosion.DestructionType destructionType = world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.KEEP;
            Explosion explosion = new Explosion(world, null, null, null, centerPos.x, centerPos.y, centerPos.z, radius, false, destructionType);
            getAffectedEntities(world, (ExplosionAccessor) explosion).forEach(entityFunction);
        }

        public static boolean isFlameNearby(World world, Vec3d source, int radius) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (isFlammable(world.getBlockState(
                                BlockPos.ofFloored(source.x + x, source.y + y, source.z + z)))) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        /**
         * Based on Explosion::collectBlocksAndDamageEntities
         * Get all the affected blocks in the explosion radius
         *
         * @param explosion ExplosionAccessor
         * @return Set of {@link BlockPos}
         */
        public static Set<BlockPos> getAffectedBlocks(World world, ExplosionAccessor explosion) {
            Set<BlockPos> blocks = Sets.newHashSet();
            for (int j = 0; j < 16; ++j) {
                for (int k = 0; k < 16; ++k) {
                    for (int l = 0; l < 16; ++l) {
                        if (j != 0 && j != 15 && k != 0 && k != 15 && l != 0 && l != 15) continue;
                        double d = (float) j / 15.0f * 2.0f - 1.0f;
                        double e = (float) k / 15.0f * 2.0f - 1.0f;
                        double f = (float) l / 15.0f * 2.0f - 1.0f;
                        double g = Math.sqrt(d * d + e * e + f * f);
                        d /= g;
                        e /= g;
                        f /= g;
                        double m = explosion.getX();
                        double n = explosion.getY();
                        double o = explosion.getZ();
                        for (float h = explosion.getPower() * (0.7f + world.random.nextFloat() * 0.6f); h > 0.0f; h -= 0.22500001f) {
                            BlockPos blockPos = BlockPos.ofFloored(m, n, o);
                            BlockState blockState = world.getBlockState(blockPos);
                            FluidState fluidState = world.getFluidState(blockPos);
                            if (!world.isInBuildLimit(blockPos)) continue;
                            Optional<Float> optional = explosion.getBehavior().getBlastResistance((Explosion) explosion, world, blockPos, blockState, fluidState);
                            if (optional.isPresent()) {
                                h -= (optional.get() + 0.3f) * 0.3f;
                            }
                            if (h > 0.0f && explosion.getBehavior().canDestroyBlock((Explosion) explosion, world, blockPos, blockState, h)) {
                                blocks.add(blockPos);
                            }
                            m += d * (double) 0.3f;
                            n += e * (double) 0.3f;
                            o += f * (double) 0.3f;
                        }
                    }
                }
            }
            return blocks;
        }

        /**
         * Based on Explosion::collectBlocksAndDamageEntities
         * Get all the affected entities in the explosion radius
         *
         * @param explosion ExplosionAccessor
         * @return List of {@link LivingEntity}
         */
        public static List<LivingEntity> getAffectedEntities(World world, ExplosionAccessor explosion) {
            List<LivingEntity> entities = new ArrayList<>();
            float j = explosion.getPower() * 2.0f;
            int k = MathHelper.floor(explosion.getX() - (double) j - 1.0);
            int l = MathHelper.floor(explosion.getX() + (double) j + 1.0);
            int d = MathHelper.floor(explosion.getY() - (double) j - 1.0);
            int q = MathHelper.floor(explosion.getY() + (double) j + 1.0);
            int e = MathHelper.floor(explosion.getZ() - (double) j - 1.0);
            int r = MathHelper.floor(explosion.getZ() + (double) j + 1.0);
            List<Entity> f = world.getOtherEntities(null, new Box(k, d, e, l, q, r));
            Vec3d vec3d = new Vec3d(explosion.getX(), explosion.getY(), explosion.getZ());

            Predicate<Entity> predicate = EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR;
            for (Entity entity : f) {
                double u;
                double t;
                double s;
                if (entity.isImmuneToExplosion() || !((Math.sqrt(entity.squaredDistanceTo(vec3d)) / (double) j) <= 1.0) || (Math.sqrt((s = entity.getX() - explosion.getX()) * s + (t = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - explosion.getY()) * t + (u = entity.getZ() - explosion.getZ()) * u)) == 0.0)
                    continue;
                if (entity instanceof LivingEntity && predicate.test(entity)) {
                    entities.add((LivingEntity) entity);
                }
            }
            return entities;
        }


    }
}
