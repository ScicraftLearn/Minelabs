package be.uantwerpen.scicraft.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class ConcreteHudRenderCallback implements HudRenderCallback {
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta){
//        MinecraftClient.getInstance().textRenderer.draw(matrixStack, "help", 10, 10, 111455);
    }

    public void listener(){
        this.onHudRender(new MatrixStack(),10);
    }

}
