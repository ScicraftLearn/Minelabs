package be.uantwerpen.scicraft.item;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.fluid.Fluids;
import be.uantwerpen.scicraft.lewisrecipes.Atom;
import be.uantwerpen.scicraft.potion.GasPotion;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Items {
    // Items
    public static final Item ENTROPY_CREEPER_SPAWN_EGG = register(new SpawnEggItem(Entities.ENTROPY_CREEPER,
            0xbb64e1, 0x5d0486, new FabricItemSettings().group(ItemGroup.MISC)), "entropy_creeper_spawn_egg");

    // Items > Atoms
    public static Item HYDROGEN_ATOM;
    public static Item HELIUM_ATOM;

    public static Item LITHIUM_ATOM;
    public static Item BERYLLIUM_ATOM;
    public static Item BORON_ATOM;
    public static Item CARBON_ATOM;
    public static Item NITROGEN_ATOM;
    public static Item OXYGEN_ATOM;
    public static Item FLUORINE_ATOM;
    public static Item NEON_ATOM;

    public static Item SODIUM_ATOM;
    public static Item MAGNESIUM_ATOM;
    public static Item ALUMINIUM_ATOM;
    public static Item SILICON_ATOM;
    public static Item PHOSPHORUS_ATOM;
    public static Item SULFUR_ATOM;
    public static Item CHLORINE_ATOM;
    public static Item ARGON_ATOM;


    public static Item POTASSIUM_ATOM;
    public static Item CALCIUM_ATOM;
    public static Item IRON_ATOM;
    public static Item COPPER_ATOM;
    public static Item ZINC_ATOM;
    public static Item BROMINE_ATOM;

    public static Item SILVER_ATOM;
    public static Item CADMIUM_ATOM;
    public static Item TIN_ATOM;
    public static Item IODINE_ATOM;

    public static Item GOLD_ATOM;
    public static Item MERCURY_ATOM;
    public static Item LEAD_ATOM;
    public static Item URANIUM_ATOM;


    // Items > Atoms (internal)
    public static Item HYDROGEN_ATOM_INTERNAL;
    public static Item HELIUM_ATOM_INTERNAL;

    public static Item LITHIUM_ATOM_INTERNAL;
    public static Item BERYLLIUM_ATOM_INTERNAL;
    public static Item BORON_ATOM_INTERNAL;
    public static Item CARBON_ATOM_INTERNAL;
    public static Item NITROGEN_ATOM_INTERNAL;
    public static Item OXYGEN_ATOM_INTERNAL;
    public static Item FLUORINE_ATOM_INTERNAL;
    public static Item NEON_ATOM_INTERNAL;

    public static Item SODIUM_ATOM_INTERNAL;
    public static Item MAGNESIUM_ATOM_INTERNAL;
    public static Item ALUMINIUM_ATOM_INTERNAL;
    public static Item SILICON_ATOM_INTERNAL;
    public static Item PHOSPHORUS_ATOM_INTERNAL;
    public static Item SULFUR_ATOM_INTERNAL;
    public static Item CHLORINE_ATOM_INTERNAL;
    public static Item ARGON_ATOM_INTERNAL;

    public static Item POTASSIUM_ATOM_INTERNAL;
    public static Item CALCIUM_ATOM_INTERNAL;
    public static Item IRON_ATOM_INTERNAL;
    public static Item COPPER_ATOM_INTERNAL;
    public static Item ZINC_ATOM_INTERNAL;
    public static Item BROMINE_ATOM_INTERNAL;

    public static Item SILVER_ATOM_INTERNAL;
    public static Item CADMIUM_ATOM_INTERNAL;
    public static Item TIN_ATOM_INTERNAL;
    public static Item IODINE_ATOM_INTERNAL;

    public static Item GOLD_ATOM_INTERNAL;
    public static Item MERCURY_ATOM_INTERNAL;
    public static Item LEAD_ATOM_INTERNAL;
    public static Item URANIUM_ATOM_INTERNAL;


    // Items > Bindings (internal)
    public static Item BINDING_HORIZONTAL;
    public static Item BINDING_VERTICAL;
    public static Item BINDING_DOUBLE_HORIZONTAL;
    public static Item BINDING_DOUBLE_VERTICAL;
    public static Item BINDING_TRIPLE_HORIZONTAL;
    public static Item BINDING_TRIPLE_VERTICAL;

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

    public static final Item LEWIS_BLOCK_ITEM = register(new BlockItem(Blocks.LEWIS_BLOCK, new Item.Settings().group(ItemGroup.MISC)) ,"lewis_block");

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

    static {
        // Items > Atoms
        HYDROGEN_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.HYDROGEN), "hydrogen_atom");
        HELIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.HELIUM), "helium_atom");

        LITHIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.LITHIUM), "lithium_atom");
        BERYLLIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.BERYLLIUM), "beryllium_atom");
        BORON_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.BORON), "boron_atom");
        CARBON_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.CARBON), "carbon_atom");
        NITROGEN_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.NITROGEN), "nitrogen_atom");
        OXYGEN_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.OXYGEN), "oxygen_atom");
        FLUORINE_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.FLUORINE), "fluorine_atom");
        NEON_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.NEON), "neon_atom");

        SODIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.SODIUM), "sodium_atom");
        MAGNESIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.MAGNESIUM), "magnesium_atom");
        ALUMINIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.ALUMINIUM), "aluminium_atom");
        SILICON_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.SILICON), "silicon_atom");
        PHOSPHORUS_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.PHOSPHORUS), "phosphorus_atom");
        SULFUR_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.SULFUR), "sulfur_atom");
        CHLORINE_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.CHLORINE), "chlorine_atom");
        ARGON_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.ARGON), "argon_atom");

        POTASSIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.POTASSIUM), "potassium_atom");
        CALCIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.CALCIUM), "calcium_atom");
        IRON_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.IRON), "iron_atom");
        COPPER_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.COPPER), "copper_atom");
        ZINC_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.ZINC), "zinc_atom");
        BROMINE_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.BROMINE), "bromine_atom");

        SILVER_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.SILVER), "silver_atom");
        CADMIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT),Atom.CADMIUM), "cadmium_atom");
        TIN_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.TIN), "tim_atom");
        IODINE_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.IODINE), "iodine_atom");

        GOLD_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.GOLD), "gold_atom");
        MERCURY_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.MERCURY), "mercury_atom");
        LEAD_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.LEAD), "lead_atom");
        URANIUM_ATOM = register(new AtomItem(new Item.Settings().group(ItemGroups.SCICRAFT), Atom.URANIUM), "uranium_atom");

        // Items > Atoms (internal)
        HYDROGEN_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.HYDROGEN), "internal_atoms/hydrogen_atom_internal");
        HELIUM_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.HELIUM), "internal_atoms/helium_atom_internal");

        LITHIUM_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.LITHIUM), "internal_atoms/lithium_atom_internal");
        BERYLLIUM_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.BERYLLIUM), "internal_atoms/beryllium_atom_internal");
        BORON_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.BORON), "internal_atoms/boron_atom_internal");
        CARBON_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.CARBON), "internal_atoms/carbon_atom_internal");
        NITROGEN_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.NITROGEN), "internal_atoms/nitrogen_atom_internal");
        OXYGEN_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.OXYGEN), "internal_atoms/oxygen_atom_internal");
        FLUORINE_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.FLUORINE), "internal_atoms/fluorine_atom_internal");
        NEON_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.NEON), "internal_atoms/neon_atom_internal");

        SODIUM_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.SODIUM), "internal_atoms/sodium_atom_internal");
        MAGNESIUM_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.MAGNESIUM), "internal_atoms/magnesium_atom_internal");
        ALUMINIUM_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.ALUMINIUM), "internal_atoms/aluminium_atom_internal");
        SILICON_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.SILICON), "internal_atoms/silicon_atom_internal");
        PHOSPHORUS_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.PHOSPHORUS), "internal_atoms/phosphorus_atom_internal");
        SULFUR_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.SULFUR), "internal_atoms/sulfur_atom_internal");
        CHLORINE_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.CHLORINE), "internal_atoms/chlorine_atom_internal");
        ARGON_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.ARGON), "internal_atoms/argon_atom_internal");

        POTASSIUM_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.POTASSIUM), "internal_atoms/potassium_atom_internal");
        CALCIUM_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.CALCIUM), "internal_atoms/calcium_atom_internal");
        IRON_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.IRON), "internal_atoms/iron_atom_internal");
        COPPER_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.COPPER), "internal_atoms/copper_atom_internal");
        ZINC_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.ZINC), "internal_atoms/zinc_atom_internal");
        BROMINE_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.BROMINE), "internal_atoms/bromine_atom_internal");

        SILVER_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.SILVER), "internal_atoms/silver_atom_internal");
        CADMIUM_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.CADMIUM), "internal_atoms/cadmium_atom_internal");
        TIN_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.TIN), "internal_atoms/tim_atom_internal");
        IODINE_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.IODINE), "internal_atoms/iodine_atom_internal");

        GOLD_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.GOLD), "internal_atoms/gold_atom_internal");
        MERCURY_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.MERCURY), "internal_atoms/mercury_atom_internal");
        LEAD_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.LEAD), "internal_atoms/lead_atom_internal");
        URANIUM_ATOM_INTERNAL = register(new LewisCraftingItem(new Item.Settings(), Atom.URANIUM), "internal_atoms/uranium_atom_internal");

        // Items > Bindings (internal)
        BINDING_HORIZONTAL = register(new Item(new Item.Settings()), "internal_bindings/binding_horizontal");
        BINDING_VERTICAL = register(new Item(new Item.Settings()), "internal_bindings/binding_vertical");
        BINDING_DOUBLE_HORIZONTAL = register(new Item(new Item.Settings()), "internal_bindings/binding_double_horizontal");
        BINDING_DOUBLE_VERTICAL = register(new Item(new Item.Settings()), "internal_bindings/binding_double_vertical");
        BINDING_TRIPLE_HORIZONTAL = register(new Item(new Item.Settings()), "internal_bindings/binding_triple_horizontal");
        BINDING_TRIPLE_VERTICAL = register(new Item(new Item.Settings()), "internal_bindings/binding_triple_vertical");
    }

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
