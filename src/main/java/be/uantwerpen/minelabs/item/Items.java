package be.uantwerpen.minelabs.item;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.crafting.molecules.Atom;
import be.uantwerpen.minelabs.entity.Entities;
import be.uantwerpen.minelabs.fluid.Fluids;
import be.uantwerpen.minelabs.potion.GasPotion;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Items {

    public static final Item ATOM_FLOOR = register(new BlockItem((Blocks.ATOM_FLOOR), new FabricItemSettings().group(ItemGroups.MINELABS)), "atomic_floor");

    // Items
    public static final Item ENTROPY_CREEPER_SPAWN_EGG = register(new SpawnEggItem(Entities.ENTROPY_CREEPER,
            0xbb64e1, 0x5d0486, new FabricItemSettings().group(ItemGroup.MISC)), "entropy_creeper_spawn_egg");

    public static final Item LASERTOOL_IRON = register(new LaserTool(2.5f, -2.4f, ToolMaterials.IRON, new Item.Settings().group(ItemGroup.TOOLS)), "lasertool_iron");
    public static final Item LASERTOOL_GOLD = register(new LaserTool(2.5f, -2.4f, ToolMaterials.GOLD, new Item.Settings().group(ItemGroup.TOOLS)), "lasertool_gold");
    public static final Item LASERTOOL_DIAMOND = register(new LaserTool(2.5f, -2.4f, ToolMaterials.DIAMOND, new Item.Settings().group(ItemGroup.TOOLS)), "lasertool_diamond");

    public static final Item SALT = register(new FireReactionBlockItem(Blocks.SALT_WIRE,
            new FabricItemSettings().maxCount(64).group(ItemGroups.CHEMICALS), 4, "NaCl") {
        @Override
        public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
            tooltip.add(Text.literal("NaCl"));
        }
    }, "salt");

    public static final Item SALT_SHARD = register(new Item(
            new FabricItemSettings().maxCount(64).group(ItemGroups.CHEMICALS)) {
        @Override
        public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
            tooltip.add(Text.literal("NaCl"));
        }
    }, "salt_shard");

    public static final Item SALT_ORE = register(new BlockItem(Blocks.SALT_ORE,
            new FabricItemSettings().maxCount(64).group(ItemGroups.CHEMICALS)), "salt_ore");

    public static final Item DEEPSLATE_SALT_ORE = register(new BlockItem(Blocks.DEEPSLATE_SALT_ORE,
            new FabricItemSettings().maxCount(64).group(ItemGroups.CHEMICALS)), "deepslate_salt_ore");

    public static final Item SALT_BLOCK = register(new BlockItem(Blocks.SALT_BLOCK,
            new FabricItemSettings().maxCount(64).group(ItemGroups.CHEMICALS)) {
        @Override
        public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
            tooltip.add(Text.literal("4x NaCl"));
        }
    }, "salt_block");

    public static final Item SALT_CRYSTAL = register(new BlockItem(Blocks.SALT_CRYSTAL,
            new FabricItemSettings().maxCount(64).group(ItemGroups.CHEMICALS)), "salt_crystal");

    public static final Item SMALL_SALT_CRYSTAL = register(new BlockItem(Blocks.SMALL_SALT_CRYSTAL,
            new FabricItemSettings().maxCount(64).group(ItemGroups.CHEMICALS)), "small_salt_crystal");

    public static final Item MEDIUM_SALT_CRYSTAL = register(new BlockItem(Blocks.MEDIUM_SALT_CRYSTAL,
            new FabricItemSettings().maxCount(64).group(ItemGroups.CHEMICALS)), "medium_salt_crystal");

    public static final Item LARGE_SALT_CRYSTAL = register(new BlockItem(Blocks.LARGE_SALT_CRYSTAL,
            new FabricItemSettings().maxCount(64).group(ItemGroups.CHEMICALS)), "large_salt_crystal");

    public static final Item BUDDING_SALT_BLOCK = register(new BlockItem(Blocks.BUDDING_SALT_BLOCK,
            new FabricItemSettings().maxCount(64).group(ItemGroups.CHEMICALS)), "budding_salt_block");

    public static final Item SAFETY_GLASSES = register(new ArmorItem(ArmorMaterials.CLOTH, EquipmentSlot.HEAD,
            new FabricItemSettings().group(ItemGroups.MINELABS)), "safety_glasses");

    public static final Item LAB_COAT = register(new ArmorItem(ArmorMaterials.CLOTH, EquipmentSlot.CHEST,
            new FabricItemSettings().group(ItemGroups.MINELABS)), "lab_coat");

    public static final Item LAB_COAT2 = register(new LabCoatArmorItem2(ArmorMaterials.CLOTH, EquipmentSlot.CHEST,
            new FabricItemSettings().group(ItemGroups.MINELABS)), "lab_coat2");

    //TODO ADD PANTS? AND BOOTS ?

    public static final Item LAB_COUNTER = register(new BlockItem(Blocks.LAB_CABIN,
            new FabricItemSettings().group(ItemGroups.MINELABS).maxCount(64)), "lab_cabin");

    public static final Item LAB_DRAWER = register(new BlockItem(Blocks.LAB_DRAWER,
            new FabricItemSettings().group(ItemGroups.MINELABS).maxCount(64)), "lab_drawer");

    public static final Item LAB_SINK = register(new BlockItem(Blocks.LAB_SINK,
            new FabricItemSettings().group(ItemGroups.MINELABS).maxCount(64)), "lab_sink");

    public static final Item LAB_CENTER = register(new BlockItem(Blocks.LAB_CENTER,
            new FabricItemSettings().group(ItemGroups.MINELABS).maxCount(64)), "lab_center");

    public static final Item LAB_CORNER = register(new BlockItem(Blocks.LAB_CORNER,
            new FabricItemSettings().group(ItemGroups.MINELABS).maxCount(64)), "lab_corner");

    public static final Item MICROSCOPE = register(new BlockItem(Blocks.MICROSCOPE,
            new FabricItemSettings().group(ItemGroups.MINELABS).maxCount(1)), "microscope");

    public static final Item TUBERACK = register(new BlockItem(Blocks.TUBERACK,
            new FabricItemSettings().group(ItemGroups.MINELABS).maxCount(1)), "tuberack");
    public static final Item LENS = register(new Item(
            new FabricItemSettings().group(ItemGroups.MINELABS).maxCount(1)), "lens");

    public static final Item BIG_LENS = register(new Item(
            new FabricItemSettings().group(ItemGroups.MINELABS).maxCount(1)), "big_lens");


    public static final Item LITHIUM_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), 1, "LiCl"), "lithium_chloride_dust");

    public static final Item STRONTIUM_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), 2, "SrCl2"), "strontium_chloride_dust");

    public static final Item STRONTIUM_NITRATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), 2, "SrN2O6"), "strontium_nitrate_dust"); // Sr(NO3)2

    public static final Item CALCIUM_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), 3, "CaCl2"), "calcium_chloride_dust");

    public static final Item SODIUM_CARBONATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), 4, "Na2CO3"), "sodium_carbonate_dust");

    public static final Item BORAX_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), 5, "borax"), "borax_dust");

    public static final Item COPPER_SULFATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), 6, "CuSO4"), "copper_sulfate_dust");

    public static final Item BORIC_ACID = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), 6, "BH3O3"), "boric_acid");

    public static final Item COPPER_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), 7, "CuCl2"), "copper_chloride_dust");

    public static final Item POTASSIUM_SULFATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), 8, "K2SO4"), "potassium_sulfate_dust");

    public static final Item POTASSIUM_NITRATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), 8, "KNO3"), "potassium_nitrate_dust");

    public static final Item POTASSIUM_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), 9, "KCl"), "potassium_chloride_dust");

    public static final Item MAGNESIUM_SULFATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), 10, "MgSO4"), "magnesium_sulfate_dust");

    public static final Item ASH_DUST = register(new Item(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS)), "ash_dust");

    public static final Item DUST_ALH3 = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), "AlH3"), "aluminium_hydride_dust");

    public static final Item DUST_SIO2 = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), "SiO2"), "silicon_dioxide_dust");

    public static final Item DUST_AlCl3 = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), "AlCl3"), "aluminium_trichloride_dust");

    public static final Item DUST_BN = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), "BN"), "boron_nitride_dust");

    public static final Item DUST_BEO = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), "BeO"), "beryllium_oxide_dust");

    public static final Item DUST_SIC = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), "SiC"), "silicon_carbide_dust");

    public static final Item DUST_ALN = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), "AlN"), "aluminium_nitride_dust");

    public static final Item DUST_MGO = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), "MgO"), "magnesium_oxide_dust");

    public static final Item DUST_SO3 = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64).group(ItemGroups.CHEMICALS), "SO3"), "sulfur_trioxide_dust");
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
    public static Item STRONTIUM_ATOM;

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
    public static Item VALENCEE;

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

    public static final Item ELECTRIC_FIELD_SENSOR = register(new BlockItem(Blocks.ELECTRIC_FIELD_SENSOR_BLOCK, new FabricItemSettings().group(ItemGroups.MINELABS)), "electric_field_sensor");

    // helium gas
    public static final Item HELIUM = register(new BlockItem(Blocks.HELIUM, new FabricItemSettings().group(ItemGroups.CHEMICALS)), "helium");
    public static final Item MOLOGRAM = register(new BlockItem(Blocks.MOLOGRAM_BLOCK, new FabricItemSettings().group(ItemGroups.MINELABS)), "mologram");

    public static final Item BALLOON = register(new BalloonItem(new Item.Settings().group(ItemGroups.MINELABS).maxCount(1)), "balloon");

    public static final Item BOHR_BLOCK = register(new BlockItem(Blocks.BOHR_BLOCK, new Item.Settings().group(ItemGroups.MINELABS)), "bohr_block");

    public static final Item LEWIS_BLOCK_ITEM = register(new BlockItem(Blocks.LEWIS_BLOCK, new Item.Settings().group(ItemGroups.MINELABS)), "lewis_block");
    public static final Item IONIC_BLOCK_ITEM = register(new BlockItem(Blocks.IONIC_BLOCK, new Item.Settings().group(ItemGroups.MINELABS)), "ionic_block");

    // Erlenmeyer
    public static final Item ERLENMEYER_STAND = register(new BlockItem(Blocks.ERLENMEYER_STAND,
            new FabricItemSettings().group(ItemGroups.CHEMICALS)), "erlenmeyer_stand");
    public static final Item ERLENMEYER = register(new ErlenmeyerItem(net.minecraft.fluid.Fluids.EMPTY,
            new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), ""), "erlenmeyer");

    public static final Item ERLENMEYER_O2 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "O2"), "erlenmeyer_o2");
    public static final Item ERLENMEYER_N2 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "N2"), "erlenmeyer_n2");
    public static final Item ERLENMEYER_CH4 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "CH4"), "erlenmeyer_ch4");
    public static final Item ERLENMEYER_H2 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "H2"), "erlenmeyer_h2");
    public static final Item ERLENMEYER_NO = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "NO"), "erlenmeyer_no");
    public static final Item ERLENMEYER_NO2 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "NO2"), "erlenmeyer_no2");
    public static final Item ERLENMEYER_Cl2 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "Cl2"), "erlenmeyer_cl2");
    public static final Item ERLENMEYER_CO2 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "CO2"), "erlenmeyer_co2");
    public static final Item ERLENMEYER_CO = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "CO"), "erlenmeyer_co");
    public static final Item ERLENMEYER_NH3 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "NH3"), "erlenmeyer_nh3");
    public static final Item ERLENMEYER_N2O = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "N2O"), "erlenmeyer_n2o");
    public static final Item ERLENMEYER_HCl = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "HCl"), "erlenmeyer_hcl");
    public static final Item ERLENMEYER_He = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "He"), "erlenmeyer_he");
    public static final Item ERLENMEYER_Ne = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "Ne"), "erlenmeyer_ne");
    public static final Item ERLENMEYER_Ar = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "Ar"), "erlenmeyer_ar");
    public static final Item ERLENMEYER_CL2O = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "Cl2O"), "erlenmeyer_cl2o");
    public static final Item ERLENMEYER_CH4S = register(new GasPotion(new FabricItemSettings().maxCount(64).group(ItemGroups.CHEMICALS), "CH4S"), "erlenmeyer_methanethiol");
    public static final Item ERLENMEYER_CH2O = register(new GasPotion(new FabricItemSettings().maxCount(64).group(ItemGroups.CHEMICALS), "CH2O"), "erlenmeyer_formaldehyde");
    public static final Item ERLENMEYER_H2CO3 = register(new GasPotion(new FabricItemSettings().maxCount(64).group(ItemGroups.CHEMICALS), "H2CO3"), "erlenmeyer_carbonic_acid");
    public static final Item ERLENMEYER_BH3 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "BH3"), "erlenmeyer_bh3");
    public static final Item ERLENMEYER_HF = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "HF"), "erlenmeyer_hf");
    public static final Item ERLENMEYER_SIH4 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "SiH4"), "erlenmeyer_sih4");
    public static final Item ERLENMEYER_PH3 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "PH3"), "erlenmeyer_ph3");
    public static final Item ERLENMEYER_H2S = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "H2S"), "erlenmeyer_h2s");
    public static final Item ERLENMEYER_CF4 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "CF4"), "erlenmeyer_cf4");
    public static final Item ERLENMEYER_BF3 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "BF3"), "erlenmeyer_bf3");
    public static final Item ERLENMEYER_BCL3 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "BCl3"), "erlenmeyer_bcl3");
    public static final Item ERLENMEYER_SO2 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "SO2"), "erlenmeyer_so2");
    public static final Item ERLENMEYER_CLF = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "ClF"), "erlenmeyer_clf");
    public static final Item ERLENMEYER_F2 = register(new GasPotion(new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "F2"), "erlenmeyer_f2");

    /*
        public static final Item ERLENMEYER_O2 = register(new GasPotion(
            new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), Gasses.O2), "erlenmeyer_o2");
    public static final Item ERLENMEYER_N2 = register(new GasPotion(
            new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), Gasses.N2), "erlenmeyer_n2");
    public static final Item ERLENMEYER_CH4 = register(new GasPotion(
            new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), Gasses.CH4), "erlenmeyer_ch4");
    public static final Item ERLENMEYER_H2 = register(new GasPotion(
            new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), Gasses.H2), "erlenmeyer_h2");
    public static final Item ERLENMEYER_NO = register(new GasPotion(
            new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), Gasses.NO), "erlenmeyer_n0");
    public static final Item ERLENMEYER_NO2 = register(new GasPotion(
            new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), Gasses.NO2), "erlenmeyer_no2");
    public static final Item ERLENMEYER_Cl2 = register(new GasPotion(
            new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), Gasses.Cl2), "erlenmeyer_cl2");
    public static final Item ERLENMEYER_CO2 = register(new GasPotion(
            new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), Gasses.CO2), "erlenmeyer_co2");
    public static final Item ERLENMEYER_CO = register(new GasPotion(
            new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), Gasses.CO), "erlenmeyer_co");
    public static final Item ERLENMEYER_NH3 = register(new GasPotion(
            new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), Gasses.NH3), "erlenmeyer_nh3");
    public static final Item ERLENMEYER_N2O = register(new GasPotion(
            new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), Gasses.N2O), "erlenmeyer_n2o");
    public static final Item ERLENMEYER_HCl = register(new GasPotion(
            new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), Gasses.HCl), "erlenmeyer_hcl");

    */
    public static final Item ERLENMEYER_HNO3 = register(new ErlenmeyerItem(
            Fluids.STILL_HNO3, new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "HNO3"), "erlenmeyer_hno3");
    public static final Item ERLENMEYER_H2O = register(new ErlenmeyerItem(
            Fluids.STILL_H2O, new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "H2O"), "erlenmeyer_h2o");
    public static final Item ERLENMEYER_NCL3 = register(new ErlenmeyerItem(
            Fluids.STILL_NCl3, new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "NCl3"), "erlenmeyer_ncl3");
    public static final Item ERLENMEYER_CS2 = register(new ErlenmeyerItem(
            Fluids.STILL_CS2, new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "CS2"), "erlenmeyer_cs2");
    public static final Item ERLENMEYER_CCL4 = register(new ErlenmeyerItem(
            Fluids.STILL_CCl4, new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "CCl4"), "erlenmeyer_ccl4");
    public static final Item ERLENMEYER_PCl3 = register(new ErlenmeyerItem(
            Fluids.STILL_PCl3, new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "PCl3"), "erlenmeyer_pcl3");
    public static final Item ERLENMEYER_SCl2 = register(new ErlenmeyerItem(
            Fluids.STILL_SCl2, new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "SCl2"), "erlenmeyer_scl2");
    public static final Item ERLENMEYER_HCN = register(new ErlenmeyerItem(
            Fluids.STILL_HCN, new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "HCN"), "erlenmeyer_hcn");
    public static final Item ERLENMEYER_CH4O = register(new ErlenmeyerItem(
            Fluids.STILL_CH4O, new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "CH4O"), "erlenmeyer_ch4o");
    public static final Item ERLENMEYER_SICL4 = register(new ErlenmeyerItem(
            Fluids.STILL_SiCl4, new Item.Settings().group(ItemGroups.CHEMICALS).maxCount(64), "SiCl4"), "erlenmeyer_sicl4");


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
        STRONTIUM_ATOM = register(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.Strontium), "strontium_atom");

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
        VALENCEE = register(new Item(new Item.Settings()), "valence_electrons");
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
