package be.minelabs.mixin;

import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow public abstract ItemStack getStack();

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V"))
    public void injectItemPickup(PlayerEntity player, CallbackInfo ci){
        if (this.getStack().getItem() instanceof AtomItem && player.getInventory().containsAny(Set.of(Items.ATOM_PACK))){
            // TODO TRY TO INSERT INTO ATOM PACK
        }
    }
}
