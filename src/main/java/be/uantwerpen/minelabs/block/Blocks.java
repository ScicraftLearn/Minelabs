package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.entity.BlockEntities;
import be.uantwerpen.minelabs.fluid.Fluids;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;

public class Blocks {

    //Atom portal to subatom dimension
    public static final Block ATOM_PORTAL = register(new PortalBlock(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1.5f)),"atom_portal");
    //Atomic floor for atomic dimension
    public static final Block ATOM_FLOOR = register(new AtomicFloor(),"atomic_floor");

    public static final Block SALT_ORE = register(new OreBlock(FabricBlockSettings.of(Material.STONE)
            .mapColor(MapColor.WHITE_GRAY).strength(3.0f, 3.0f).requiresTool(), UniformIntProvider.create(0, 3)), "salt_ore");
    public static final Block DEEPSLATE_SALT_ORE = register(new OreBlock(FabricBlockSettings.of(Material.STONE)
            .mapColor(MapColor.WHITE_GRAY).strength(4.5f, 3.0f).requiresTool(), UniformIntProvider.create(1, 4)), "deepslate_salt_ore");
    public static final Block SALT_BLOCK = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE_GRAY).strength(2.0f)), "salt_block");

    public static final Block SALT_WIRE = register(new SaltWireBlock(FabricBlockSettings.of(Material.DECORATION)
            .mapColor(MapColor.WHITE_GRAY).noCollision().breakInstantly().dynamicBounds()), "salt_wire");

    public static final Block SALT_CRYSTAL = register(new AmethystClusterBlock(7, 3,
            FabricBlockSettings.of(Material.AMETHYST).nonOpaque().ticksRandomly()
                    .sounds(BlockSoundGroup.AMETHYST_CLUSTER).strength(1.5f).luminance((state) -> 5)), "salt_crystal");
    public static final Block LARGE_SALT_CRYSTAL = register(new AmethystClusterBlock(5, 3,
            FabricBlockSettings.copy(SALT_CRYSTAL).sounds(BlockSoundGroup.LARGE_AMETHYST_BUD).luminance((state) -> 4)), "large_salt_crystal");
    public static final Block MEDIUM_SALT_CRYSTAL = register(new AmethystClusterBlock(4, 3,
            FabricBlockSettings.copy(SALT_CRYSTAL).sounds(BlockSoundGroup.MEDIUM_AMETHYST_BUD).luminance((state) -> 2)), "medium_salt_crystal");
    public static final Block SMALL_SALT_CRYSTAL = register(new AmethystClusterBlock(3, 4,
            FabricBlockSettings.copy(SALT_CRYSTAL).sounds(BlockSoundGroup.SMALL_AMETHYST_BUD).luminance((state) -> 1)), "small_salt_crystal");

    public static final Block BUDDING_SALT_BLOCK = register(new BuddingSaltBlock(
            FabricBlockSettings.of(Material.AMETHYST).ticksRandomly().strength(1.5F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool()), "budding_salt_block");

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

    public static final ChargedBlock POSITRON = register(new ChargedBlock(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision(), () -> BlockEntities.POSTIRON_BLOCK_ENTITY), "positron");
    public static final ChargedBlock ELECTRON = register(new ChargedBlock(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision(),() -> BlockEntities.ELECTRON_BLOCK_ENTITY) , "electron");

    public static final ChargedBlock PROTON = register(new ChargedBlock(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision(), () -> BlockEntities.PROTON_BLOCK_ENTITY), "proton");
    public static final ChargedBlock ANTI_PROTON = register(new ChargedBlock(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision(), () -> BlockEntities.ANTI_PROTON_BLOCK_ENTITY), "anti_proton");

    public static final Block NEUTRON = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "neutron");
    public static final Block ANTI_NEUTRON = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "anti_neutron");

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
    public static final Block TIME_FREEZE_BLOCK = register(new TimeFreezeBlock(), "time_freeze_block");

    public static final ElectricFieldSensorBlock ELECTRIC_FIELD_SENSOR_BLOCK = register(new ElectricFieldSensorBlock(FabricBlockSettings.of(Material.METAL).noCollision().nonOpaque()), "electric_field_sensor");

    // Normal fire has luminance 15, soul fire 10 -> we choose 12 arbitrarily
    public static final Block GREEN_FIRE = register(new GreenFire(FabricBlockSettings.of(Material.FIRE, MapColor.EMERALD_GREEN).noCollision().breakInstantly().luminance(12).sounds(BlockSoundGroup.WOOL)), "green_fire");
    public static final Block HELIUM = register(new PionNulBlock(FabricBlockSettings.of(Material.AIR)
            .mapColor(MapColor.WHITE).strength(2f).noCollision().nonOpaque()), "helium");

    public static final Block LEWIS_BLOCK = register(new LewisBlock(FabricBlockSettings.copyOf(net.minecraft.block.Blocks.CHEST).nonOpaque()), "lewis_block");
    public static final Block IONIC_BLOCK = register(new IonicBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()), "ionic_block");

//    public static final Block ACID = Registry.register(new FluidBlock(Fluids.STILL_ACID, FabricBlockSettings.copy(net.minecraft.block.Blocks.WATER)), "acid");

    public static final Block ACID = Registry.register(Registry.BLOCK, new Identifier(Minelabs.MOD_ID, "acid"), new FluidBlock(Fluids.STILL_ACID, FabricBlockSettings.copy(net.minecraft.block.Blocks.WATER)) {
    });


    public static final Block BOHR_BLOCK = register(new BohrBlock(), "bohr_block");

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
        return Registry.register(Registry.BLOCK, new Identifier(Minelabs.MOD_ID, identifier), block);
    }

    /**
     * Main class method
     * Registers all Blocks
     */
    public static void registerBlocks() {
        Minelabs.LOGGER.info("registering blocks");
    }
}