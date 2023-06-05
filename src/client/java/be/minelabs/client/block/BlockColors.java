package be.minelabs.client.block;

import be.minelabs.block.Blocks;
import be.minelabs.block.blocks.QuantumfieldBlock;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class BlockColors {

    public static void onInitializeClient() {
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> 0x3495eb, Blocks.LAB_SINK);
    }
}
