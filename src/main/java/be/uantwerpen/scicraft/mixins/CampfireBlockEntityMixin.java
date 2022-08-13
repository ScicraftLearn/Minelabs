package be.uantwerpen.scicraft.mixins;

import net.minecraft.block.entity.CampfireBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {

    //@Inject(method = "addItem", at = @At("HEAD"))
    //public void injectAddItem(Entity user, ItemStack stack, int cookTime, CallbackInfoReturnable<Boolean> cir){

    // }

}
