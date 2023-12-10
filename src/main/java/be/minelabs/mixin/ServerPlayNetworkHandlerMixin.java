package be.minelabs.mixin;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    // Note: should stay consistent with variable in ClientPlayerInteractionManagerMixin
    @Unique
    private static final float SUBATOMIC_REACH_DISTANCE = 20f;

    @Shadow @Final
    public static double MAX_BREAK_SQUARED_DISTANCE = MathHelper.square(SUBATOMIC_REACH_DISTANCE);
}
