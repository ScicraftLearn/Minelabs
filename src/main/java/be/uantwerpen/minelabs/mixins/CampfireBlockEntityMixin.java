package be.uantwerpen.minelabs.mixins;

import be.uantwerpen.minelabs.block.entity.ICampfireBlockEntity;
import be.uantwerpen.minelabs.item.IFireReaction;
import be.uantwerpen.minelabs.util.Properties;
import be.uantwerpen.minelabs.util.Tags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin implements ICampfireBlockEntity, BlockEntityAccessorMixin {

    @Shadow
    @Final
    private DefaultedList<ItemStack> itemsBeingCooked;

    /**
     * REDUNDANT
     */
    @ModifyArg(method = "litServerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateListeners(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;I)V"), index = 2)
    private static BlockState injectServerTick(BlockState oldState) {
        //a fire changer was crafted, return to state with fire_color = 0
        //TODO CHECK IF THERE ARE STILL OTHER FIRE_CHANGERS PRESENT -> USE THAT COLOR INSTEAD IF THATS THE CASE
        oldState.with(Properties.FIRE_COLOR, 0);
        return oldState;
    }

    @Inject(method = "clientTick", at = @At("TAIL"))
    private static void injectClientTick(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {
        for (int i = 0; i < campfire.getItemsBeingCooked().size(); i++) {
            if (campfire.getItemsBeingCooked().get(i).isIn(Tags.Items.FIRE_CHANGER)) {
                IFireReaction item = (IFireReaction) campfire.getItemsBeingCooked().get(i).getItem();
                world.setBlockState(pos, state.with(Properties.FIRE_COLOR, item.getFireColor()), 1);
            }
        }
        //world.setBlockState(pos, state.with(IntProperty.of("fire_color", 0, 10), 4));
    }

    @Inject(method = "addItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/world/event/GameEvent;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V"))
    public void injectAddItems(Entity user, ItemStack stack, int cookTime, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isIn(Tags.Items.FIRE_CHANGER)) {
            IFireReaction item = (IFireReaction) stack.getItem();
            //user.sendMessage(Text.literal("fire color: " + item.getFireColor()));
            setCachedState(getCachedState().with(Properties.FIRE_COLOR, item.getFireColor()));
        }
    }

    public boolean hasFireChanger() {
        for (ItemStack stack : itemsBeingCooked) {
            if (stack.isIn(Tags.Items.FIRE_CHANGER)) {
                return true;
            }
        }
        return false;
    }

    public int getLatestFire() {
        int color = 0;
        if (hasFireChanger()) {
            for (ItemStack stack : itemsBeingCooked) {
                if (stack.isIn(Tags.Items.FIRE_CHANGER)) {
                    IFireReaction item = (IFireReaction) stack.getItem();
                    color = item.getFireColor();
                }
            }
        }
        return color;
    }
}
