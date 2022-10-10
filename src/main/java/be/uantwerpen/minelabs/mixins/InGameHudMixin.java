package be.uantwerpen.minelabs.mixins;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.item.Items;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    private static final Identifier SAFETY_GLASS = new Identifier(Minelabs.MOD_ID, "textures/misc/safety_glass.png");
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void renderOverlay(Identifier texture, float opacity);

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;asItem()Lnet/minecraft/item/Item;"))
    public void renderInject(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ItemStack itemStack = client.player.getInventory().getArmorStack(3);
        if (itemStack.isOf(Items.SAFETY_GLASSES)) {
            renderOverlay(SAFETY_GLASS, 1.0F);
        }
    }
}
