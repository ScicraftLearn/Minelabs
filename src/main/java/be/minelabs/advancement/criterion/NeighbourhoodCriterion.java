package be.minelabs.advancement.criterion;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.function.Predicate;

public abstract class NeighbourhoodCriterion<T extends AbstractCriterionConditions> extends net.minecraft.advancement.criterion.AbstractCriterion<T> {
    public void trigger(ServerWorld world, BlockPos origin, int radius, Predicate<T> predicate) {
        List<PlayerEntity> players = world.getPlayers(TargetPredicate.createNonAttackable(), null, Box.of(Vec3d.of(origin), radius*2, radius*2, radius*2));
        trigger(players, predicate);
    }

    public void trigger(List<PlayerEntity> players, Predicate<T> predicate) {
        for(PlayerEntity player: players){
            if (player instanceof ServerPlayerEntity serverPlayer)
                trigger(serverPlayer, predicate);
        }
    }

}
