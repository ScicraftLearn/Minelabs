package be.minelabs.client.block;

import be.minelabs.block.Blocks;
import be.minelabs.block.blocks.QuantumfieldBlock;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class BlockColors {

    public static void onInitializeClient() {
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> 0x3495eb, Blocks.LAB_SINK);
//        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
//            float age = (float) QuantumfieldBlock.getAge(state) / QuantumfieldBlock.MAX_AGE;
//            // linear from 0 to 1 at 0.5 and then back down to 0
//            float intensity = 1f - (float) Math.abs(age - 0.5) * 2;
//            int color = MathHelper.lerp(intensity, 150, 255);
//            // alpha is not actually used, so just set it to max
//            return ColorHelper.Argb.getArgb(255, color, color, color);
//        }, Blocks.GLUON_QUANTUMFIELD);
    }
}
