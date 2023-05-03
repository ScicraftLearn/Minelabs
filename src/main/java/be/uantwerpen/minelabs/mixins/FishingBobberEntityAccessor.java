package be.uantwerpen.minelabs.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FishingBobberEntity.class)
public interface FishingBobberEntityAccessor {

    @Invoker("pullHookedEntity")
    void invokePullHookedEntity(@Nullable Entity entity);
}
