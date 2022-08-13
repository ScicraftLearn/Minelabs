package be.uantwerpen.scicraft.mixins;

import be.uantwerpen.scicraft.item.IFireReaction;
import be.uantwerpen.scicraft.util.Tags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {

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

    @Inject(method = "addItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/CampfireBlockEntity;updateListeners()V"))
    public void injectAddItem(Entity user, ItemStack stack, int cookTime, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isIn(Tags.Items.FIRE_CHANGER)) {
            IFireReaction item = (IFireReaction) stack.getItem();
            //user.sendMessage(Text.literal("fire color: " + item.getFireColor()));
            //Scicraft.LOGGER.info("fire color: " + item.getFireColor());
            //System.out.println("fire color: " + item.getFireColor());
            //TODO finish ??
            //Blocks.CAMPFIRE.getDefaultState().getProperties();
            //user.world.setBlockState(user.getBlockPos(), Blocks.CAMPFIRE.getDefaultState().with(Properties.AGE_1, item.getFireColor()), 3);
            //user.world.emitGameEvent(GameEvent.BLOCK_CHANGE, user.getPos(), GameEvent.Emitter.of(user));
        }
    }

}
