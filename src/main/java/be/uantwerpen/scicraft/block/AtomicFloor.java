package be.uantwerpen.scicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;

public class AtomicFloor extends Block {
    public AtomicFloor(Settings settings) {
        super(Settings.of(Material.BARRIER).nonOpaque());
    }
}