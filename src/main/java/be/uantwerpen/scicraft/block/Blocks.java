package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Blocks {

    public static final Block PION_NUL = register(new PionBlock(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "pion_nul");
    public static final Block PION_MINUS = register(new PionBlock(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "pion_minus");
    public static final Block PION_PLUS = register(new PionBlock(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "pion_plus");

    public static final Block WEAK_BOSON = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "weak_boson");
    public static final Block NEUTRINO = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "neutrino");
    public static final Block ANTINEUTRINO = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "antineutrino");
    public static final Block POSITRON = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "positron");

    public static final Block SALT_ORE = register(new Block(FabricBlockSettings.of(Material.STONE)
            .mapColor(MapColor.WHITE).requiresTool().strength(3.0f)),"salt_ore");

    // Quantumfields
    public static final Block GLUON_QUANTUMFIELD = register(new QuantumfieldBlock(), "gluon_quantumfield");
    public static final Block PHOTON_QUANTUMFIELD = register(new QuantumfieldBlock(), "photon_quantumfield");
    public static final Block WEAK_BOSON_QUANTUMFIELD = register(new QuantumfieldBlock(), "weak_boson_quantumfield");
    public static final Block ELECTRON_QUANTUMFIELD = register(new QuantumfieldBlock(), "electron_quantumfield");
    public static final Block NEUTRINO_QUANTUMFIELD = register(new QuantumfieldBlock(), "neutrino_quantumfield");
    public static final Block UPQUARK_QUANTUMFIELD = register(new QuantumfieldBlock(), "upquark_quantumfield");
    public static final Block DOWNQUARK_QUANTUMFIELD = register(new QuantumfieldBlock(), "downquark_quantumfield");


    public static final Block GREEN_FIRE = register(new GreenFire(FabricBlockSettings.of(Material.FIRE).noCollision().breakInstantly().luminance(10),
                    5.0f), "green_fire");

    /**
     * Register a Block
     * <p>
     *
     * @param block      : Block Object to register
     * @param identifier : String name of the Item
     * @return {@link Block}
     */
    private static Block register(Block block, String identifier) {
        return Registry.register(Registry.BLOCK, new Identifier(Scicraft.MOD_ID, identifier), block);
    }

    /**
     * Main class method
     * Registers all Blocks
     */
    public static void registerBlocks() {
        Scicraft.LOGGER.info("registering blocks");
    }


}