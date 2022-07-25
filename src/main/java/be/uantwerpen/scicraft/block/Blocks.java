package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.entity.BlockEntities;
//import be.uantwerpen.scicraft.block.entity.PionMinusBlockEntity;
//import be.uantwerpen.scicraft.block.entity.PionPlusBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Blocks {

    // Value of charge here will be used temporarily when the block is still 'fresh' at the server, before a reload
    public static final Block PION_NUL = register(new PionNulBlock(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "pion_nul");
    public static final Block PION_MINUS = register(new ChargedPionBlock(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision(), () -> BlockEntities.PION_MINUS_BLOCK_ENTITY), "pion_minus");
    public static final Block PION_PLUS = register(new ChargedPionBlock(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision(), () -> BlockEntities.PION_PLUS_BLOCK_ENTITY), "pion_plus");

    public static final Block WEAK_BOSON = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "weak_boson");
    public static final Block NEUTRINO = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "neutrino");
    public static final Block ANTINEUTRINO = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "antineutrino");

    public static final ChargedBlockNEW POSITRON = register(new ChargedBlockNEW(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision(), () -> BlockEntities.POSTIRON_BLOCK_ENTITY), "positron");
    public static final ChargedBlockNEW ELECTRON = register(new ChargedBlockNEW(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision(),() -> BlockEntities.ELECTRON_BLOCK_ENTITY) , "electron");

    public static final ChargedBlock PROTON = register(new ChargedBlock(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision(), () -> BlockEntities.PROTON_BLOCK_ENTITY), "proton");

    public static final Block NEUTRON = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "neutron");

    public static final Block CHARGED_PLACEHOLDER = register(new ChargedPlaceholderBlock(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision().nonOpaque(), () -> BlockEntities.CHARGED_PLACEHOLDER_BLOCK_ENTITY), "charged_placeholder");
    public static final Block ANIMATED_CHARGED = register(new AnimatedChargedBlock(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision(), () -> BlockEntities.ANIMATED_CHARGED_BLOCK_ENTITY), "animated_charged");

    // Quantumfields
    public static final Block GLUON_QUANTUMFIELD = register(new QuantumfieldBlock(), "gluon_quantumfield");
    public static final Block PHOTON_QUANTUMFIELD = register(new QuantumfieldBlock(), "photon_quantumfield");
    public static final Block WEAK_BOSON_QUANTUMFIELD = register(new QuantumfieldBlock(), "weak_boson_quantumfield");
    public static final Block ELECTRON_QUANTUMFIELD = register(new QuantumfieldBlock(), "electron_quantumfield");
    public static final Block NEUTRINO_QUANTUMFIELD = register(new QuantumfieldBlock(), "neutrino_quantumfield");
    public static final Block UPQUARK_QUANTUMFIELD = register(new QuantumfieldBlock(), "upquark_quantumfield");
    public static final Block DOWNQUARK_QUANTUMFIELD = register(new QuantumfieldBlock(), "downquark_quantumfield");

    // Normal fire has luminance 15, soul fire 10 -> we choose 12 arbitrarily
    public static final Block GREEN_FIRE = register(new GreenFire(FabricBlockSettings.of(Material.FIRE, MapColor.EMERALD_GREEN).noCollision().breakInstantly().luminance(12).sounds(BlockSoundGroup.WOOL)), "green_fire");
    public static final Block HELIUM = register(new PionNulBlock(FabricBlockSettings.of(Material.AIR)
            .mapColor(MapColor.WHITE).strength(2f).noCollision().nonOpaque()), "helium");

    /**
     * Register a Block
     * <p>
     *
     * @param block      : Block Object to register
     * @param identifier : String name of the Item
     * @return 
     * @return {@link Block}
     */
    private static <T extends Block> T register(T block, String identifier) {
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