package be.uantwerpen.scicraft.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    //Mixin for rendering picture of the molecule structure
    @Inject(method = "renderSpyglassOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;getInstance()Lnet/minecraft/client/render/Tessellator;"))
    private void renderSpyglassOverlay(float scale, CallbackInfo ci) {
        final Identifier TESTING = new Identifier("scicraft:textures/item/electron.png");
        PlayerEntity user = MinecraftClient.getInstance().player;
        World world = MinecraftClient.getInstance().world;
        HitResult lookingAt = MinecraftClient.getInstance().crosshairTarget;
        BlockPos blockPos = null;
        //This looks if what you are pointing at is a block.
        if (lookingAt != null && lookingAt.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) lookingAt;
            blockPos = blockHit.getBlockPos();
        }
        //If lookingAt is not a block within range, do normal SpyGlass.
        if (blockPos != null && blockPos.isWithinDistance(user.getPos(), 2.4)) {
            //Show picture of molecule model.
            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            RenderSystem.setShaderTexture(0, TESTING);

        }
    }
}
