package be.uantwerpen.minelabs.potion.reactions;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReactionUtils {

    public static boolean isBlockNearby(World world, BlockPos source, BlockState blockState, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockState state = world.getBlockState(source.add(x, y, z));
                    if (state == blockState) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
