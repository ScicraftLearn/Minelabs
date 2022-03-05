package be.uantwerpen.scicraft.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpyglassItem;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(SpyglassItem.class)
public class SpyGlassItemMixin {
    @Inject(method="use", at= @At(value = "RETURN"))
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        HitResult lookingAt = MinecraftClient.getInstance().crosshairTarget;
        BlockPos blockPos = null;
        //This looks if what you are pointing at is a block.
        if (lookingAt != null && lookingAt.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) lookingAt;
            blockPos = blockHit.getBlockPos();
        }
        //If lookingAt is not a block within range, do normal SpyGlass.
        if(blockPos != null && blockPos.isWithinDistance(user.getPos(), 1.0)){
            //Show picture of molecule model.
            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (!world.isClient) {
                user.sendMessage(new LiteralText("Hello, atoms"), false);
            }
        }
    }
}
