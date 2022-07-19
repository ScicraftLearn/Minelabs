package be.uantwerpen.scicraft.item;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.fluid.Fluids;
import be.uantwerpen.scicraft.lewisrecipes.Atom;
import be.uantwerpen.scicraft.potion.GasPotion;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
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
    public static final Item NEUTRON = register(new NeutronItem(new Item.Settings().group(ItemGroups.ELEMENTARY_PARTICLES).maxCount(64)), "neutron");
    public static final Item PION_NUL = register(new BlockItem(Blocks.PION_NUL, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "pion_nul");
    public static final Item PION_MINUS = register(new BlockItem(Blocks.PION_MINUS, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "pion_minus");
    public static final Item PION_PLUS = register(new BlockItem(Blocks.PION_PLUS, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "pion_plus");

    //public static final Item CHARGED_BLOCK = register(new BlockItem(Blocks.CHARGED_BLOCK, new FabricItemSettings().group(ItemGroups.ELEMENTARY_PARTICLES)), "charged_block");

    // helium gas
    public static final Item HELIUM = register(new BlockItem(Blocks.HELIUM, new FabricItemSettings().group(ItemGroups.SCICRAFT)), "helium");

    public static final Item LEWIS_BLOCK_ITEM = register(new BlockItem(Blocks.LEWIS_BLOCK, new Item.Settings().group(ItemGroups.SCICRAFT)), "lewis_block");

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
        HYDROGEN_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.HYDROGEN), "hydrogen_atom");
        HELIUM_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.HELIUM), "helium_atom");

        LITHIUM_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.LITHIUM), "lithium_atom");
        BERYLLIUM_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.BERYLLIUM), "beryllium_atom");
        BORON_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.BORON), "boron_atom");
        CARBON_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.CARBON), "carbon_atom");
        NITROGEN_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.NITROGEN), "nitrogen_atom");
        OXYGEN_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.OXYGEN), "oxygen_atom");
        FLUORINE_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.FLUORINE), "fluorine_atom");
        NEON_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.NEON), "neon_atom");

        SODIUM_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.SODIUM), "sodium_atom");
        MAGNESIUM_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.MAGNESIUM), "magnesium_atom");
        ALUMINIUM_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.ALUMINIUM), "aluminium_atom");
        SILICON_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.SILICON), "silicon_atom");
        PHOSPHORUS_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.PHOSPHORUS), "phosphorus_atom");
        SULFUR_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.SULFUR), "sulfur_atom");
        CHLORINE_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.CHLORINE), "chlorine_atom");
        ARGON_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.ARGON), "argon_atom");

        POTASSIUM_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.POTASSIUM), "potassium_atom");
        CALCIUM_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.CALCIUM), "calcium_atom");
        IRON_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.IRON), "iron_atom");
        COPPER_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.COPPER), "copper_atom");
        ZINC_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.ZINC), "zinc_atom");
        BROMINE_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.BROMINE), "bromine_atom");

        SILVER_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.SILVER), "silver_atom");
        CADMIUM_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.CADMIUM), "cadmium_atom");
        TIN_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.TIN), "tin_atom");
        IODINE_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.IODINE), "iodine_atom");

        GOLD_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.GOLD), "gold_atom");
        MERCURY_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.MERCURY), "mercury_atom");
        LEAD_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.LEAD), "lead_atom");
        URANIUM_ATOM = registerAtom(new LewisCraftingItem(new Item.Settings().group(ItemGroups.ATOMS), Atom.URANIUM), "uranium_atom");

        // Items > Bindings (internal)
        BOND = registerBond(new Item(new Item.Settings()));
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
     * Register Atoms to Model Provider Registry ({@link FabricModelPredicateProviderRegistry})<br>
     * Returns the {@link Item} provided by {@code register(Item, String)}
     *
     * @param item:       Item Object to register
     * @param identifier: String name of the Item
     * @return {@link Item}
     */
    private static Item registerAtom(Item item, String identifier) {
        FabricModelPredicateProviderRegistry.register(item, new Identifier("lct"),
                (stack, world, entity, seed) -> stack.getOrCreateNbt().getBoolean("ScicraftItemInLCT") ? 1.0F : 0.0F);
        return register(item, identifier);
    }

    /**
     * Register Atoms to Model Provider Registry ({@link ModelPredicateProviderRegistry})<br>
     * Returns the {@link Item} provided by {@code register(Item, String)}
     *
     * @param item :       Item Object to register
     * @return {@link Item}
     */
    private static Item registerBond(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("bonds"),
                (stack, world, entity, seed) -> ((float) stack.getOrCreateNbt().getInt("ScicraftBondAmount")) / 10F);
        ModelPredicateProviderRegistry.register(item, new Identifier("direction"),
                (stack, world, entity, seed) -> stack.getOrCreateNbt().getBoolean("ScicraftBondDirection") ? 1.0F : 0.0F);
        return register(item, "bond");
    }

    /**
     * Main class method<br>
     * Registers all (Block)Items
     */
    public static void registerItems() {
        Scicraft.LOGGER.info("registering items");
    }
}
