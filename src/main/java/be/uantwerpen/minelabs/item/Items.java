package be.uantwerpen.minelabs.item;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.crafting.molecules.Atom;
import be.uantwerpen.minelabs.entity.Entities;
import be.uantwerpen.minelabs.fluid.Fluids;
import be.uantwerpen.minelabs.potion.GasPotion;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Items {
    //Atomic portal
    public static final Item ATOM_PORTAL = register(new BlockItem((Blocks.ATOM_PORTAL), new FabricItemSettings().group(ItemGroups.MINELABS)), "atom_portal");
    public static final Item ATOM_FLOOR = register(new BlockItem((Blocks.ATOM_FLOOR), new FabricItemSettings().group(ItemGroups.MINELABS)), "atomic_floor");

    // Items
    public static final Item ENTROPY_CREEPER_SPAWN_EGG = register(new SpawnEggItem(Entities.ENTROPY_CREEPER,
            0xbb64e1, 0x5d0486, new FabricItemSettings().group(ItemGroup.MISC)), "entropy_creeper_spawn_egg");

    public static final Item LASERTOOL_IRON = register(new LaserTool( 2.5f, -2.4f, ToolMaterials.IRON, new Item.Settings().group(ItemGroup.TOOLS)), "lasertool_iron");
    public static final Item LASERTOOL_GOLD = register(new LaserTool( 2.5f, -2.4f, ToolMaterials.GOLD, new Item.Settings().group(ItemGroup.TOOLS)), "lasertool_gold");
    public static final Item LASERTOOL_DIAMOND = register(new LaserTool( 2.5f, -2.4f, ToolMaterials.DIAMOND, new Item.Settings().group(ItemGroup.TOOLS)), "lasertool_diamond");

    public static final Item SALT = register(new SaltItem(Blocks.SALT_WIRE,
            new FabricItemSettings().maxCount(64).group(ItemGroups.MINELABS), 4), "salt");

    public static final Item SALT_SHARD = register(new SaltShardItem(
            new FabricItemSettings().maxCount(64).group(ItemGroups.MINELABS)), "salt_shard");

    public static final Item SALT_ORE = register(new BlockItem(Blocks.SALT_ORE,
            new FabricItemSettings().maxCount(64).group(ItemGroups.MINELABS)), "salt_ore");

    public static final Item DEEPSLATE_SALT_ORE = register(new BlockItem(Blocks.DEEPSLATE_SALT_ORE,
            new FabricItemSettings().maxCount(64).group(ItemGroups.MINELABS)), "deepslate_salt_ore");

    public static final Item SALT_BLOCK = register(new SaltBlockItem(Blocks.SALT_BLOCK,
            new FabricItemSettings().maxCount(64).group(ItemGroups.MINELABS)), "salt_block");

    public static final Item SALT_CRYSTAL = register(new BlockItem(Blocks.SALT_CRYSTAL,
            new FabricItemSettings().maxCount(64).group(ItemGroups.MINELABS)), "salt_crystal");

    public static final Item SMALL_SALT_CRYSTAL = register(new BlockItem(Blocks.SMALL_SALT_CRYSTAL,
            new FabricItemSettings().maxCount(64).group(ItemGroups.MINELABS)), "small_salt_crystal");

    public static final Item MEDIUM_SALT_CRYSTAL = register(new BlockItem(Blocks.MEDIUM_SALT_CRYSTAL,
            new FabricItemSettings().maxCount(64).group(ItemGroups.MINELABS)), "medium_salt_crystal");

    public static final Item LARGE_SALT_CRYSTAL = register(new BlockItem(Blocks.LARGE_SALT_CRYSTAL,
            new FabricItemSettings().maxCount(64).group(ItemGroups.MINELABS)), "large_salt_crystal");

    public static final Item BUDDING_SALT_BLOCK = register(new BlockItem(Blocks.BUDDING_SALT_BLOCK,
            new FabricItemSettings().maxCount(64).group(ItemGroups.MINELABS)), "budding_salt_block");

    public static final Item LITHIUM_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS), 1), "lithium_chloride_dust");

    public static final Item STRONTIUM_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS), 2), "strontium_chloride_dust");

    public static final Item STRONTIUM_NITRATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS), 2), "strontium_nitrate_dust");

    public static final Item CALCIUM_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS), 3), "calcium_chloride_dust");

    public static final Item SODIUM_CARBONATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS), 4), "sodium_carbonate_dust");

    public static final Item BORAX_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS), 5), "borax_dust");

    public static final Item COPPER_SULFATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS), 6), "copper_sulfate_dust");

    public static final Item BORIC_ACID_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS), 6), "boric_acid_dust");

    public static final Item COPPER_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS), 7), "copper_chloride_dust");

    public static final Item POTASSIUM_SULFATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS), 8), "potassium_sulfate_dust");

    public static final Item POTASSIUM_NITRATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS), 8), "potassium_nitrate_dust");

    public static final Item POTASSIUM_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS), 9), "potassium_chloride_dust");

    public static final Item MAGNESIUM_SULFATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS), 10), "magnesium_sulfate_dust");

    public static final Item ASH_DUST = register(new Item(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.MINELABS)), "ash_dust");

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
    public static Item TITANIUM_ATOM;
    public static Item MANGANESE_ATOM;
    public static Item IRON_ATOM;
    public static Item COPPER_ATOM;
    public static Item ZINC_ATOM;
    public static Item BROMINE_ATOM;

    public static Item SILVER_ATOM;
    public static Item CADMIUM_ATOM;
    public static Item TIN_ATOM;
    public static Item IODINE_ATOM;

    public static Item TUNGSTEN_ATOM;
    public static Item GOLD_ATOM;
    public static Item MERCURY_ATOM;
    public static Item LEAD_ATOM;
    public static Item URANIUM_ATOM;

    // Items > Bond to display in LCT (internal)
    public static Item BOND;

    // Items > Quantum fields
    public static final Item UPQUARK_QUANTUMFIELD = register(new BlockItem(Blocks.UPQUARK_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "upquark_quantumfield");
    public static final Item DOWNQUARK_QUANTUMFIELD = register(new BlockItem(Blocks.DOWNQUARK_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "downquark_quantumfield");
    public static final Item GLUON_QUANTUMFIELD = register(new BlockItem(Blocks.GLUON_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "gluon_quantumfield");
    public static final Item ELECTRON_QUANTUMFIELD = register(new BlockItem(Blocks.ELECTRON_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "electron_quantumfield");
    public static final Item PHOTON_QUANTUMFIELD = register(new BlockItem(Blocks.PHOTON_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "photon_quantumfield");
    public static final Item NEUTRINO_QUANTUMFIELD = register(new BlockItem(Blocks.NEUTRINO_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "neutrino_quantumfield");
    public static final Item WEAK_BOSON_QUANTUMFIELD = register(new BlockItem(Blocks.WEAK_BOSON_QUANTUMFIELD, new FabricItemSettings().group(ItemGroups.QUANTUM_FIELDS)), "weak_boson_quantumfield");

    // Items > Electric field
    public static final Item TIME_FREEZE_BLOCK = register(new BlockItem(Blocks.TIME_FREEZE_BLOCK, new FabricItemSettings().group(ItemGroups.MINELABS)), "time_freeze_block");

    // Items > Elementary particles

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

    public static final Item GLUON = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "gluon");

    public static final Item ELECTRON = register(new ElectronItem(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "electron");
    public static final Item POSITRON = register(new BlockItem(Blocks.POSITRON, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "positron");
    public static final Item PHOTON = register(new Item(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "photon");

    public static final Item NEUTRINO = register(new BlockItem(Blocks.NEUTRINO, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "neutrino");
    public static final Item ANTINEUTRINO = register(new BlockItem(Blocks.ANTINEUTRINO, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "antineutrino");
    public static final Item WEAK_BOSON = register(new BlockItem(Blocks.WEAK_BOSON, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "weak_boson");

    public static final Item PROTON = register(new ProtonItem(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "proton");
    public static final Item ANTI_PROTON = register(new AntiProtonItem(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "anti_proton");
    public static final Item NEUTRON = register(new NeutronItem(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "neutron");
    public static final Item ANTI_NEUTRON = register(new AntiNeutronItem(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "anti_neutron");
    public static final Item PION_NUL = register(new BlockItem(Blocks.PION_NUL, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "pion_nul");
    public static final Item PION_MINUS = register(new BlockItem(Blocks.PION_MINUS, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "pion_minus");
    public static final Item PION_PLUS = register(new BlockItem(Blocks.PION_PLUS, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "pion_plus");

    //public static final Item CHARGED_BLOCK = register(new BlockItem(Blocks.CHARGED_BLOCK, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "charged_block");

    public static final Item ELECTRIC_FIELD_SENSOR = register(new BlockItem(Blocks.ELECTRIC_FIELD_SENSOR_BLOCK, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "electric_field_sensor");

    // helium gas
    public static final Item HELIUM = register(new BlockItem(Blocks.HELIUM, new FabricItemSettings().group(ItemGroups.MINELABS)), "helium");

    public static final Item BOHR_BLOCK = register(new BlockItem(Blocks.BOHR_BLOCK, new Item.Settings().group(ItemGroups.MINELABS)), "bohr_block");

    public static final Item LEWIS_BLOCK_ITEM = register(new BlockItem(Blocks.LEWIS_BLOCK, new Item.Settings().group(ItemGroups.MINELABS)), "lewis_block");
    public static final Item IONIC_BLOCK_ITEM = register(new BlockItem(Blocks.IONIC_BLOCK, new Item.Settings().group(ItemGroups.MINELABS)), "ionic_block");

    // Erlenmeyer
    public static final Item ERLENMEYER = register(new Item(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer");

//    public static final Item ACID_BUCKET = register(new BucketItem(Fluids.STILL_ACID, new Item.Settings().group(ItemGroups.MINELABS).maxCount(64)), "erlenmeyer_fluid");

    public static final Item ERLENMEYER_02 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer_o2");
    public static final Item ERLENMEYER_N2 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer_n2");
    public static final Item ERLENMEYER_CH4 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer_ch4");
    public static final Item ERLENMEYER_H2 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer_h2");
    public static final Item ERLENMEYER_N0 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer_n0");
    public static final Item ERLENMEYER_N02 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer_no2");
    public static final Item ERLENMEYER_Cl2 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer_cl2");
    public static final Item ERLENMEYER_CO2 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer_co2");
    public static final Item ERLENMEYER_CO = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer_co");
    public static final Item ERLENMEYER_NH3 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer_nh3");
    public static final Item ERLENMEYER_N2O = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer_n2o");
    public static final Item ERLENMEYER_HCl = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer_hcl");

    public static final Item ERLENMEYER_HNO3 = register(new BucketItem(Fluids.STILL_ACID, new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64)), "erlenmeyer_hno3");

    static {
        // Items > Atoms
        HYDROGEN_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.HYDROGEN), "hydrogen_atom");
        HELIUM_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.HELIUM), "helium_atom");

        LITHIUM_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.LITHIUM), "lithium_atom");
        BERYLLIUM_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.BERYLLIUM), "beryllium_atom");
        BORON_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.BORON), "boron_atom");
        CARBON_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.CARBON), "carbon_atom");
        NITROGEN_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.NITROGEN), "nitrogen_atom");
        OXYGEN_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.OXYGEN), "oxygen_atom");
        FLUORINE_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.FLUORINE), "fluorine_atom");
        NEON_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.NEON), "neon_atom");

        SODIUM_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.SODIUM), "sodium_atom");
        MAGNESIUM_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.MAGNESIUM), "magnesium_atom");
        ALUMINIUM_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.ALUMINIUM), "aluminium_atom");
        SILICON_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.SILICON), "silicon_atom");
        PHOSPHORUS_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.PHOSPHORUS), "phosphorus_atom");
        SULFUR_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.SULFUR), "sulfur_atom");
        CHLORINE_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.CHLORINE), "chlorine_atom");
        ARGON_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.ARGON), "argon_atom");

        POTASSIUM_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.POTASSIUM), "potassium_atom");
        CALCIUM_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.CALCIUM), "calcium_atom");
        TITANIUM_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.TITANIUM), "titanium_atom");
        MANGANESE_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.MANGANESE), "manganese_atom");
        IRON_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.IRON), "iron_atom");
        COPPER_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.COPPER), "copper_atom");
        ZINC_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.ZINC), "zinc_atom");
        BROMINE_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.BROMINE), "bromine_atom");

        SILVER_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.SILVER), "silver_atom");
        CADMIUM_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.CADMIUM), "cadmium_atom");
        TIN_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.TIN), "tin_atom");
        IODINE_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.IODINE), "iodine_atom");

        TUNGSTEN_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.TUNGSTEN), "tungsten_atom");
        GOLD_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.GOLD), "gold_atom");
        MERCURY_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.MERCURY), "mercury_atom");
        LEAD_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.LEAD), "lead_atom");
        URANIUM_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.URANIUM), "uranium_atom");

        // Items > Bindings (internal)
        BOND = register(new Item(new Item.Settings()), "bond");
    }

    /**
     * Register an Item
     *
     * @param item:       Item Object to register
     * @param identifier: String name of the Item
     * @return {@link Item}
     */
    private static Item register(Item item, String identifier) {
        return Registry.register(Registry.ITEM, new Identifier(Minelabs.MOD_ID, identifier), item);
    }

    /**
     * Main class method<br>
     * Registers all (Block)Items
     */
    public static void registerItems() {
        Minelabs.LOGGER.info("registering items");
    }
}
