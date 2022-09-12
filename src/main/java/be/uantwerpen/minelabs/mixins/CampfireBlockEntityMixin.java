package be.uantwerpen.minelabs.mixins;

import be.uantwerpen.minelabs.block.entity.ICampfireBlockEntity;
import be.uantwerpen.minelabs.item.IFireReaction;
import be.uantwerpen.minelabs.util.Properties;
import be.uantwerpen.minelabs.util.Tags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin implements ICampfireBlockEntity {

    @Shadow
    @Final
    private DefaultedList<ItemStack> itemsBeingCooked;

    @Inject(method = "litServerTick", at = @At(value = "HEAD"))
    private static void injectServerTick(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {
        int color = state.get(Properties.FIRE_COLOR);
        for (int i = 0; i < campfire.getItemsBeingCooked().size(); i++) {
            if (campfire.getItemsBeingCooked().get(i).isIn(Tags.Items.FIRE_CHANGER)) {
                IFireReaction item = (IFireReaction) campfire.getItemsBeingCooked().get(i).getItem();
                color = item.getFireColor();
            }
        }
        if (color != state.get(Properties.FIRE_COLOR)) {
            //Change 3 -> 19 if you don't want observer updates
            world.setBlockState(pos, state.with(Properties.FIRE_COLOR, color), 3);
            //campfire.setCachedState(state.with(Properties.FIRE_COLOR, color));
        }
    }

    @Inject(method = "litServerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateListeners(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;I)V"))
    private static void craftServerTick(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {
        ICampfireBlockEntity castedE = (ICampfireBlockEntity) campfire;
        //Change 3 -> 19 if you don't want observer updates
        world.setBlockState(pos, state.with(Properties.FIRE_COLOR, castedE.getLatestFire()), 3);
    }

    /**
     * Does the campfire have a fire_changer item
     *
     * @return boolean
     */
    public boolean hasFireChanger() {
        for (ItemStack stack : itemsBeingCooked) {
            if (stack.isIn(Tags.Items.FIRE_CHANGER)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the latest fire color of the items in the campfire
     *
     * @return Integer
     */
    public int getLatestFire() {
        int color = 0;
        for (ItemStack stack : itemsBeingCooked) {
            if (stack.isIn(Tags.Items.FIRE_CHANGER)) {
                IFireReaction item = (IFireReaction) stack.getItem();
                color = item.getFireColor();
            }
        }
        return color;
    }
}
