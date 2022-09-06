package be.uantwerpen.minelabs.mixins;

import be.uantwerpen.minelabs.block.entity.ICampfireBlockEntity;
import be.uantwerpen.minelabs.item.IFireReaction;
import be.uantwerpen.minelabs.util.Tags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin implements ICampfireBlockEntity, BlockEntityAccessorMixin {

    @Shadow
    @Final
    private DefaultedList<ItemStack> itemsBeingCooked;

    @Inject(method = "litServerTick", at = @At(value = "TAIL"))
    private static void injectServerTick(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {
        //TODO WHERE ??? or not NECESSARY (Tail for now)


    }

    @Inject(method = "clientTick", at = @At("TAIL"))
    private static void injectClientTick(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {
        for (int i = 0; i < campfire.getItemsBeingCooked().size(); i++) {
            if (campfire.getItemsBeingCooked().get(i).isIn(Tags.Items.FIRE_CHANGER)) {
                IFireReaction item = (IFireReaction) campfire.getItemsBeingCooked().get(i).getItem();
                world.setBlockState(pos, state.with(IntProperty.of("fire_color", 0, 10), item.getFireColor()), 1);
            }
        }
        world.setBlockState(pos, state.with(IntProperty.of("fire_color", 0, 10), 0));
    }

    @Inject(method = "addItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/world/event/GameEvent;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V"))
    public void injectAddItems(Entity user, ItemStack stack, int cookTime, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isIn(Tags.Items.FIRE_CHANGER)) {
            IFireReaction item = (IFireReaction) stack.getItem();
            user.sendMessage(Text.literal("fire color: " + item.getFireColor()));
            setCachedState(getCachedState().with(IntProperty.of("fire_color", 0, 10), item.getFireColor()));
        }
    }

    public boolean hasFireChanger() {
        for (ItemStack stack : this.itemsBeingCooked) {
            if (stack.isIn(Tags.Items.FIRE_CHANGER)) {
                return true;
            }
        }
        return false;
    }

}
