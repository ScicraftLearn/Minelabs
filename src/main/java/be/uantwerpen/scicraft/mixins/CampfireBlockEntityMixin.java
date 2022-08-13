package be.uantwerpen.scicraft.mixins;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.item.IFireReaction;
import be.uantwerpen.scicraft.util.Tags;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {

    @Inject(method = "addItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/CampfireBlockEntity;updateListeners()V"))
    public void injectAddItem(Entity user, ItemStack stack, int cookTime, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isIn(Tags.Items.FIRE_CHANGER)) {
            IFireReaction item = (IFireReaction) stack.getItem();
            user.sendMessage(Text.literal("fire color: " + item.getFireColor()));
            Scicraft.LOGGER.info(item.getFireColor());
            System.out.println(item.getFireColor());
        }
    }

}
