package be.uantwerpen.minelabs.potion.reactions;

import be.uantwerpen.minelabs.util.Tags;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Consumer;

public abstract class Reaction {

    private final List<TagKey> whiteList = new ArrayList<>();
    private final List<TagKey> blackList = List.of(Tags.Blocks.REACTION_DEFAULT_BLACKLIST);

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
        return whiteList.stream().allMatch(tag -> state.isIn(tag)) &&
                blackList.stream().noneMatch(tag -> state.isIn(tag));
    }

    public void react(World world, Vec3d position, HitResult hitResult) {
        Objects.requireNonNull(world);
        Objects.requireNonNull(position);
        Objects.requireNonNull(hitResult);
        if (hitResult instanceof BlockHitResult blockHitResult)
            react(world, position, blockHitResult.getBlockPos());
        else if (hitResult instanceof EntityHitResult entityHitResult)
            react(world, position, entityHitResult.getEntity().getBlockPos());
    }

    protected abstract void react(World world, Vec3d position, BlockPos blockPos);

    public abstract void react(LivingEntity entity);

    protected class Utils {

        final static List<BlockState> FLAMMABLE_BLOCKS = Arrays.asList(
                Blocks.TORCH.getDefaultState(),
                Blocks.CAMPFIRE.getDefaultState(),
                Blocks.CANDLE.getDefaultState().with(CandleBlock.LIT, true),
                Blocks.FIRE.getDefaultState(),
                Blocks.LAVA.getDefaultState()
        );

        public static void applyRadius(BlockPos centerPos, int radius, Consumer<BlockPos> blockFunction) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos blockPos = centerPos.add(x, y, z);
                        if (Math.sqrt(centerPos.getSquaredDistance(blockPos)) <= radius) {
                            blockFunction.accept(blockPos);
                        }
                    }
                }
            }
        }

        public static void applyRadius(World world, Vec3d position, int radius, Consumer<LivingEntity> entityFunction) {
            double x = position.x, y = position.y, z = position.z;
            for (Entity entity : world.getOtherEntities(null, new Box(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius)))
                if (entity instanceof LivingEntity livingEntity)
                    entityFunction.accept(livingEntity);

        }

        public static boolean isFlameNearby(World world, BlockPos source, int radius) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (FLAMMABLE_BLOCKS.contains(world.getBlockState(source.add(x, y, z)))) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

    }
}
