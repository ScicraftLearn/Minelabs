package be.uantwerpen.scicraft.item;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.fluid.Fluids;
import be.uantwerpen.scicraft.potion.GasPotion;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Items {
    // Items
    public static final Item ENTROPY_CREEPER_SPAWN_EGG = register(new SpawnEggItem(Entities.ENTROPY_CREEPER,
            0xbb64e1, 0x5d0486, new FabricItemSettings().group(ItemGroup.MISC)), "entropy_creeper_spawn_egg");

    // Items > Atoms
    public static final Item HYDROGEN_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 1, "H"), "hydrogen_atom");
    public static final Item HELIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 2, "He"), "helium_atom");
    public static final Item LITHIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 3, "Li"), "lithium_atom");
    public static final Item BERYLLIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 4, "Be"), "beryllium_atom");
    public static final Item BORON_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 5, "B"), "boron_atom");
    public static final Item CARBON_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 6, "C"), "carbon_atom");
    public static final Item NITROGEN_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 7, "N"), "nitrogen_atom");
    public static final Item OXYGEN_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 8, "O"), "oxygen_atom");
    public static final Item FLUORINE_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 9, "F"), "fluorine_atom");
    public static final Item NEON_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 10, "Ne"), "neon_atom");

    public static final Item SODIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 11, "Na"), "sodium_atom");
    public static final Item MAGNESIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 12, "Mg"), "magnesium_atom");
    public static final Item ALUMINIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 13, "Al"), "aluminium_atom");
    public static final Item SILICON_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 14, "Si"), "silicon_atom");
    public static final Item PHOSPHORUS_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 15, "P"), "phosphorus_atom");
    public static final Item SULFUR_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 16, "S"), "sulfur_atom");
    public static final Item CHLORINE_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 17, "Cl"), "chlorine_atom");
    public static final Item ARGON_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), 18, "Ar"), "argon_atom");

    // Items > Quantum fields
    public static final Item GLUON_QUANTUMFIELD = register(new BlockItem(Blocks.GLUON_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "gluon_quantumfield");
    public static final Item PHOTON_QUANTUMFIELD = register(new BlockItem(Blocks.PHOTON_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "photon_quantumfield");
    public static final Item WEAK_BOSON_QUANTUMFIELD = register(new BlockItem(Blocks.WEAK_BOSON_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "weak_boson_quantumfield");
    public static final Item ELECTRON_QUANTUMFIELD = register(new BlockItem(Blocks.ELECTRON_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "electron_quantumfield");
    public static final Item NEUTRINO_QUANTUMFIELD = register(new BlockItem(Blocks.NEUTRINO_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "neutrino_quantumfield");
    public static final Item UPQUARK_QUANTUMFIELD = register(new BlockItem(Blocks.UPQUARK_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "upquark_quantumfield");
    public static final Item DOWNQUARK_QUANTUMFIELD = register(new BlockItem(Blocks.DOWNQUARK_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "downquark_quantumfield");

    // Items > Elementary particles
    public static final Item PION_NUL = register(new BlockItem(Blocks.PION_NUL, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "pion_nul");
    public static final Item PION_MINUS = register(new BlockItem(Blocks.PION_MINUS, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "pion_minus");
    public static final Item PION_PLUS = register(new BlockItem(Blocks.PION_PLUS, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "pion_plus");

    public static final Item WEAK_BOSON = register(new BlockItem(Blocks.WEAK_BOSON, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "weak_boson");
    public static final Item NEUTRINO = register(new BlockItem(Blocks.NEUTRINO, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "neutrino");
    public static final Item ANTINEUTRINO = register(new BlockItem(Blocks.ANTINEUTRINO, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "antineutrino");
    public static final Item POSITRON = register(new BlockItem(Blocks.POSITRON, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "positron");

    public static final Item ELECTRON = register(new ElectronItem(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "electron");
    public static final Item PROTON = register(new ProtonItem(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "proton");
    public static final Item NEUTRON = register(new NeutronItem(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "neutron");

    public static final Item GLUON = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "gluon");
    public static final Item PHOTON = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "photon");

    public static final Item UPQUARK_RED = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "upquark_red");
    public static final Item UPQUARK_GREEN = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "upquark_green");
    public static final Item UPQUARK_BLUE = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "upquark_blue");
    public static final Item ANTI_UPQUARK_RED = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "anti_upquark_red");
    public static final Item ANTI_UPQUARK_GREEN = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "anti_upquark_green");
    public static final Item ANTI_UPQUARK_BLUE = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "anti_upquark_blue");
    public static final Item DOWNQUARK_RED = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "downquark_red");
    public static final Item DOWNQUARK_GREEN = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "downquark_green");
    public static final Item DOWNQUARK_BLUE = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "downquark_blue");
    public static final Item ANTI_DOWNQUARK_RED = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "anti_downquark_red");
    public static final Item ANTI_DOWNQUARK_GREEN = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "anti_downquark_green");
    public static final Item ANTI_DOWNQUARK_BLUE = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "anti_downquark_blue");

    public static final Item ERLENMEYER_STAND = register(new BlockItem(Blocks.ERLENMEYER_STAND, new Item.Settings().group(ItemGroups.SCICRAFT)), "erlenmeyer_stand");
    // Erlenmeyer
    public static final Item ERLENMEYER = register(new Item(new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer");

//    public static final Item ACID_BUCKET = register(new BucketItem(Fluids.STILL_ACID, new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_fluid");

    public static final Item ERLENMEYER_02 = register(new GasPotion(new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_o2");
    public static final Item ERLENMEYER_N2 = register(new GasPotion(new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_n2");
    public static final Item ERLENMEYER_CH4 = register(new GasPotion(new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_ch4");
    public static final Item ERLENMEYER_H2 = register(new GasPotion(new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_h2");
    public static final Item ERLENMEYER_N0 = register(new GasPotion(new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_n0");
    public static final Item ERLENMEYER_N02 = register(new GasPotion(new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_no2");
    public static final Item ERLENMEYER_Cl2 = register(new GasPotion(new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_cl2");
    public static final Item ERLENMEYER_CO2 = register(new GasPotion(new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_co2");
    public static final Item ERLENMEYER_CO = register(new GasPotion(new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_co");
    public static final Item ERLENMEYER_NH3 = register(new GasPotion(new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_nh3");
    public static final Item ERLENMEYER_N2O = register(new GasPotion(new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_n2o");
    public static final Item ERLENMEYER_HCl = register(new GasPotion(new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_hcl");

    public static final Item ERLENMEYER_HNO3 = register(new BucketItem(Fluids.STILL_ACID, new Item.Settings().group(ItemGroups.SCICRAFT).maxCount(64)), "erlenmeyer_hno3");

    /**
     * Register an Item
     *
     * @param item:       Item Object to register
     * @param identifier: String name of the Item
     * @return {@link Item}
     */
    private static Item register(Item item, String identifier) {
        return Registry.register(Registry.ITEM, new Identifier(Scicraft.MOD_ID, identifier), item);
    }

    /**
     * Main class method
     * Registers all (Block)Items
     */
    public static void registerItems() {
        Scicraft.LOGGER.info("registering items");
    }
}
