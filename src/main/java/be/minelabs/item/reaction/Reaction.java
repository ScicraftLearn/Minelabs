package be.minelabs.item.reaction;

import be.minelabs.util.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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

    protected abstract void react(World world, Vec3d position);

    public abstract void react(LivingEntity entity);

    public abstract Text getTooltipText();

    public MutableText getTooltipText(String reaction){
        return Text.translatable("reaction.minelabs." + reaction);
    }

    protected static class Utils {

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

    }
}
