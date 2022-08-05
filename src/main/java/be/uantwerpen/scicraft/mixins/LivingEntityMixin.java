package be.uantwerpen.scicraft.mixins;

import be.uantwerpen.scicraft.dimension.ModDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    final private float atom_movement_multiplier = 2.5f;

    @Shadow
    private float movementSpeed;
    @Shadow
    public float airStrafingSpeed;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    public float getAtomDimensionMovementMultiplier() {
        return atom_movement_multiplier;
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    public float getJumpVelocity() {
        float jumpvelocity = 0.42F * this.getJumpVelocityMultiplier();
        if (this.world.getRegistryKey().equals(ModDimensions.SUBATOM_KEY)) {
            jumpvelocity *= this.getAtomDimensionMovementMultiplier();
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
            speed *= this.getAtomDimensionMovementMultiplier();
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
            speed *= this.getAtomDimensionMovementMultiplier();
        return speed;
    }


    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect jumpBoost);

    @Shadow
    public abstract StatusEffectInstance getStatusEffect(StatusEffect jumpBoost);

}
