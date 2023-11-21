package be.minelabs.mixin;

import be.minelabs.item.Items;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {
    @Inject(method="interactWithItem", at=@At(value="HEAD"), cancellable = true)
    private void interactItemInject(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
        // Makes sure that Untamed Horses, Donkeys, Llamas and Camels are also balloonable
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.BALLOON)) {
            ActionResult ar = itemStack.getItem().useOnEntity(itemStack, player, ((MobEntity)((Object)(this))), hand);
            ci.setReturnValue(ar);
        }
    }
}
