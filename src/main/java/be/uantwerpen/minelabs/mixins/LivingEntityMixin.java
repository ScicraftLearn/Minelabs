package be.uantwerpen.minelabs.mixins;

import be.uantwerpen.minelabs.dimension.ModDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private final float atom_movement_multiplier = 2.5f;

    @Shadow
    private float movementSpeed;
    @Shadow
    public float airStrafingSpeed;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract Map<StatusEffect, StatusEffectInstance> getActiveStatusEffects();

    /**
     * @author
     * @reason
     */
    @Overwrite
    public float getJumpVelocity() {
        float jumpvelocity = 0.42F * this.getJumpVelocityMultiplier();
        if (this.world.getRegistryKey().equals(ModDimensions.SUBATOM_KEY)) {
            jumpvelocity *= atom_movement_multiplier;
        }
        return jumpvelocity;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public float getMovementSpeed() {
        float speed = this.movementSpeed;
        if (this.world.getRegistryKey().equals(ModDimensions.SUBATOM_KEY))
            speed *= atom_movement_multiplier;
        return speed;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private float getMovementSpeed(float slipperiness) {
        float speed = (this.onGround ? this.getMovementSpeed() * (0.21600002F / (slipperiness * slipperiness * slipperiness)) : this.airStrafingSpeed);
        if (this.world.getRegistryKey().equals(ModDimensions.SUBATOM_KEY))
            speed *= atom_movement_multiplier;
        return speed;
    }




    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean hasStatusEffect(StatusEffect effect) {
        if (this.world.getRegistryKey().equals(ModDimensions.SUBATOM_KEY)&&effect.equals(StatusEffects.SLOW_FALLING)){
            return true;
        }
        return getActiveStatusEffects().containsKey(effect);
    }
}
