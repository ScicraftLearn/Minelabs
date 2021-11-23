package be.uantwerpen.scicraft.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;

public class Blocks {
    public static final Block PROTON_BLOCK = new Block(FabricBlockSettings.of(Material.WOOL).mapColor(MapColor.WHITE).strength(2f).nonOpaque().collidable(false));
    public static final Block PION_NUL_BLOCK = new PionBlock(FabricBlockSettings.of(Material.WOOL).mapColor(MapColor.WHITE).strength(2f).nonOpaque().collidable(false));
}