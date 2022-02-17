package be.uantwerpen.scicraft.block;

import net.minecraft.block.Block;

public class QuantumfieldBlock extends Block {
    public QuantumfieldBlock(Settings settings) {
        // Properties of all quantumfield blocks
        super(settings.noCollision().breakInstantly().requiresTool());
    }
}
