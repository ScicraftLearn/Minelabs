package be.uantwerpen.scicraft.mixins;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends LivingEntity{
    @ModifyVariable(method = "jump()V", at = @At("STORE"), ordinal = 1)
    private double injected(double d) {
        return d * 4D;
    }

    protected LivingEntityMixin(EntityType<?extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
}
