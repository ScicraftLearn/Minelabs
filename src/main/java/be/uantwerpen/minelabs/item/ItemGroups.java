package be.uantwerpen.minelabs.item;

import be.uantwerpen.minelabs.Minelabs;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ItemGroups {
    // Hard-coded ItemGroup, update from 19.3 allows to use ItemGroupEvents to extend ItemGroups. Currently, only
    //  addition by had is allowed or automatic addition (in Items.Settings). You cant combine both because of the
    //  implementation within the Fabric-API.
    public static final ItemGroup MINELABS = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "minelabs"))
            .icon(() -> new ItemStack(Items.MOLOGRAM))
            .appendItems(stack -> {
                stack.add(new ItemStack(Items.ATOM_FLOOR));
                stack.add(new ItemStack(Items.SAFETY_GLASSES));
                stack.add(new ItemStack(Items.LAB_COAT));
                stack.add(new ItemStack(Items.LAB_COUNTER));
                stack.add(new ItemStack(Items.LAB_DRAWER));
                stack.add(new ItemStack(Items.LAB_SINK));
                stack.add(new ItemStack(Items.LAB_CENTER));
                stack.add(new ItemStack(Items.LAB_CORNER));
                stack.add(new ItemStack(Items.MICROSCOPE));
                stack.add(new ItemStack(Items.BURNER));
                stack.add(new ItemStack(Items.TUBERACK));
                stack.add(new ItemStack(Items.LENS));
                stack.add(new ItemStack(Items.BIG_LENS));
                stack.add(new ItemStack(Items.TIME_FREEZE_BLOCK));
                stack.add(new ItemStack(Items.ELECTRIC_FIELD_SENSOR));
                stack.add(new ItemStack(Items.MOLOGRAM));
                stack.add(new ItemStack(Items.BALLOON));
                stack.add(new ItemStack(Items.BOHR_BLUEPRINT));
                stack.add(new ItemStack(Items.LEWIS_BLOCK_ITEM));
                stack.add(new ItemStack(Items.IONIC_BLOCK_ITEM));
                stack.add(new ItemStack(Items.LASERTOOL_IRON));
                stack.add(new ItemStack(Items.LASERTOOL_GOLD));
                stack.add(new ItemStack(Items.LASERTOOL_DIAMOND));
                stack.add(new ItemStack(Items.ENTROPY_CREEPER_SPAWN_EGG));
                stack.add(new ItemStack(Items.MAGNET));
                stack.add(new ItemStack(Items.STAR));
            })
            .build();

    public static final ItemGroup CHEMICALS = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "chemicals"))
            .icon(() -> new ItemStack(Items.ERLENMEYER))
            .appendItems(stack -> {
                stack.add(new ItemStack(Items.SALT));
                stack.add(new ItemStack(Items.SALT_SHARD));
                stack.add(new ItemStack(Items.SALT_ORE));
                stack.add(new ItemStack(Items.DEEPSLATE_SALT_ORE));
                stack.add(new ItemStack(Items.SALT_BLOCK));
                stack.add(new ItemStack(Items.SALT_CRYSTAL));
                stack.add(new ItemStack(Items.SMALL_SALT_CRYSTAL));
                stack.add(new ItemStack(Items.MEDIUM_SALT_CRYSTAL));
                stack.add(new ItemStack(Items.LARGE_SALT_CRYSTAL));
                stack.add(new ItemStack(Items.BUDDING_SALT_BLOCK));
                stack.add(new ItemStack(Items.LITHIUM_CHLORIDE_DUST));
                stack.add(new ItemStack(Items.STRONTIUM_CHLORIDE_DUST));
                stack.add(new ItemStack(Items.STRONTIUM_NITRATE_DUST));
                stack.add(new ItemStack(Items.CALCIUM_CHLORIDE_DUST));
                stack.add(new ItemStack(Items.SODIUM_CARBONATE_DUST));
                stack.add(new ItemStack(Items.BORAX_DUST));
                stack.add(new ItemStack(Items.COPPER_SULFATE_DUST));
                stack.add(new ItemStack(Items.BORIC_ACID));
                stack.add(new ItemStack(Items.COPPER_CHLORIDE_DUST));
                stack.add(new ItemStack(Items.POTASSIUM_NITRATE_DUST));
                stack.add(new ItemStack(Items.POTASSIUM_CHLORIDE_DUST));
                stack.add(new ItemStack(Items.POTASSIUM_SULFATE_DUST));
                stack.add(new ItemStack(Items.MAGNESIUM_SULFATE_DUST));
                stack.add(new ItemStack(Items.ASH_DUST));
                stack.add(new ItemStack(Items.DUST_ALH3));
                stack.add(new ItemStack(Items.DUST_SIO2));
                stack.add(new ItemStack(Items.DUST_AlCl3));
                stack.add(new ItemStack(Items.DUST_BN));
                stack.add(new ItemStack(Items.DUST_BEO));
                stack.add(new ItemStack(Items.DUST_SIC));
                stack.add(new ItemStack(Items.DUST_ALN));
                stack.add(new ItemStack(Items.DUST_MGO));
                stack.add(new ItemStack(Items.DUST_SO3));
                stack.add(new ItemStack(Items.ERLENMEYER_STAND));
                stack.add(new ItemStack(Items.ERLENMEYER));
                stack.add(new ItemStack(Items.ERLENMEYER_O2));
                stack.add(new ItemStack(Items.ERLENMEYER_N2));
                stack.add(new ItemStack(Items.ERLENMEYER_CH4));
                stack.add(new ItemStack(Items.ERLENMEYER_H2));
                stack.add(new ItemStack(Items.ERLENMEYER_NO));
                stack.add(new ItemStack(Items.ERLENMEYER_NO2));
                stack.add(new ItemStack(Items.ERLENMEYER_Cl2));
                stack.add(new ItemStack(Items.ERLENMEYER_CO2));
                stack.add(new ItemStack(Items.ERLENMEYER_CO));
                stack.add(new ItemStack(Items.ERLENMEYER_NH3));
                stack.add(new ItemStack(Items.ERLENMEYER_N2O));
                stack.add(new ItemStack(Items.ERLENMEYER_HCl));
                stack.add(new ItemStack(Items.ERLENMEYER_He));
                stack.add(new ItemStack(Items.ERLENMEYER_Ne));
                stack.add(new ItemStack(Items.ERLENMEYER_Ar));
                stack.add(new ItemStack(Items.ERLENMEYER_CL2O));
                stack.add(new ItemStack(Items.ERLENMEYER_CH4S));
                stack.add(new ItemStack(Items.ERLENMEYER_CH2O));
                stack.add(new ItemStack(Items.ERLENMEYER_H2CO3));
                stack.add(new ItemStack(Items.ERLENMEYER_BH3));
                stack.add(new ItemStack(Items.ERLENMEYER_HF));
                stack.add(new ItemStack(Items.ERLENMEYER_SIH4));
                stack.add(new ItemStack(Items.ERLENMEYER_PH3));
                stack.add(new ItemStack(Items.ERLENMEYER_H2S));
                stack.add(new ItemStack(Items.ERLENMEYER_CF4));
                stack.add(new ItemStack(Items.ERLENMEYER_BF3));
                stack.add(new ItemStack(Items.ERLENMEYER_BCL3));
                stack.add(new ItemStack(Items.ERLENMEYER_SO2));
                stack.add(new ItemStack(Items.ERLENMEYER_CLF));
                stack.add(new ItemStack(Items.ERLENMEYER_F2));
                stack.add(new ItemStack(Items.ERLENMEYER_HNO3));
                stack.add(new ItemStack(Items.ERLENMEYER_H2O));
                stack.add(new ItemStack(Items.ERLENMEYER_NCL3));
                stack.add(new ItemStack(Items.ERLENMEYER_CS2));
                stack.add(new ItemStack(Items.ERLENMEYER_CCL4));
                stack.add(new ItemStack(Items.ERLENMEYER_PCl3));
                stack.add(new ItemStack(Items.ERLENMEYER_SCl2));
                stack.add(new ItemStack(Items.ERLENMEYER_HCN));
                stack.add(new ItemStack(Items.ERLENMEYER_CH4O));
                stack.add(new ItemStack(Items.ERLENMEYER_SICL4));
            })
            .build();

    public static final ItemGroup ATOMS = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "atoms"))
            .icon(() -> new ItemStack(Items.HYDROGEN_ATOM))
            .appendItems(stack ->{
                stack.add(new ItemStack(Items.HYDROGEN_ATOM));
                stack.add(new ItemStack(Items.HELIUM_ATOM));
                stack.add(new ItemStack(Items.LITHIUM_ATOM));
                stack.add(new ItemStack(Items.BERYLLIUM_ATOM));
                stack.add(new ItemStack(Items.BORON_ATOM));
                stack.add(new ItemStack(Items.CARBON_ATOM));
                stack.add(new ItemStack(Items.NITROGEN_ATOM));
                stack.add(new ItemStack(Items.OXYGEN_ATOM));
                stack.add(new ItemStack(Items.FLUORINE_ATOM));
                stack.add(new ItemStack(Items.NEON_ATOM));
                stack.add(new ItemStack(Items.SODIUM_ATOM));
                stack.add(new ItemStack(Items.MAGNESIUM_ATOM));
                stack.add(new ItemStack(Items.ALUMINIUM_ATOM));
                stack.add(new ItemStack(Items.SILICON_ATOM));
                stack.add(new ItemStack(Items.PHOSPHORUS_ATOM));
                stack.add(new ItemStack(Items.SULFUR_ATOM));
                stack.add(new ItemStack(Items.CHLORINE_ATOM));
                stack.add(new ItemStack(Items.ARGON_ATOM));
                stack.add(new ItemStack(Items.POTASSIUM_ATOM));
                stack.add(new ItemStack(Items.CALCIUM_ATOM));
                stack.add(new ItemStack(Items.TITANIUM_ATOM));
                stack.add(new ItemStack(Items.MANGANESE_ATOM));
                stack.add(new ItemStack(Items.IRON_ATOM));
                stack.add(new ItemStack(Items.COPPER_ATOM));
                stack.add(new ItemStack(Items.ZINC_ATOM));
                stack.add(new ItemStack(Items.BROMINE_ATOM));
                stack.add(new ItemStack(Items.STRONTIUM_ATOM));
                stack.add(new ItemStack(Items.SILVER_ATOM));
                stack.add(new ItemStack(Items.CADMIUM_ATOM));
                stack.add(new ItemStack(Items.TIN_ATOM));
                stack.add(new ItemStack(Items.IODINE_ATOM));
                stack.add(new ItemStack(Items.TUNGSTEN_ATOM));
                stack.add(new ItemStack(Items.GOLD_ATOM));
                stack.add(new ItemStack(Items.MERCURY_ATOM));
                stack.add(new ItemStack(Items.LEAD_ATOM));
                stack.add(new ItemStack(Items.URANIUM_ATOM));
            })
            .build();

    public static final ItemGroup ELEMENTARY_PARTICLES = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "elementary_particles"))
            .icon(() -> new ItemStack(Items.PION_NUL))
            .appendItems(stack ->{

                stack.add(new ItemStack(Items.UPQUARK_RED));
                stack.add(new ItemStack(Items.UPQUARK_GREEN));
                stack.add(new ItemStack(Items.UPQUARK_BLUE));
                stack.add(new ItemStack(Items.ANTI_UPQUARK_RED));
                stack.add(new ItemStack(Items.ANTI_UPQUARK_GREEN));
                stack.add(new ItemStack(Items.ANTI_UPQUARK_BLUE));
                stack.add(new ItemStack(Items.DOWNQUARK_RED));
                stack.add(new ItemStack(Items.DOWNQUARK_GREEN));
                stack.add(new ItemStack(Items.DOWNQUARK_BLUE));
                stack.add(new ItemStack(Items.ANTI_DOWNQUARK_RED));
                stack.add(new ItemStack(Items.ANTI_DOWNQUARK_GREEN));
                stack.add(new ItemStack(Items.ANTI_DOWNQUARK_BLUE));
                stack.add(new ItemStack(Items.GLUON));
                stack.add(new ItemStack(Items.ELECTRON));
                stack.add(new ItemStack(Items.POSITRON));
                stack.add(new ItemStack(Items.PHOTON));
                stack.add(new ItemStack(Items.NEUTRINO));
                stack.add(new ItemStack(Items.ANTINEUTRINO));
                stack.add(new ItemStack(Items.WEAK_BOSON));
                stack.add(new ItemStack(Items.PROTON));
                stack.add(new ItemStack(Items.ANTI_PROTON));
                stack.add(new ItemStack(Items.NEUTRON));
                stack.add(new ItemStack(Items.ANTI_NEUTRON));
                stack.add(new ItemStack(Items.PION_NUL));
                stack.add(new ItemStack(Items.PION_MINUS));
                stack.add(new ItemStack(Items.PION_PLUS));
                stack.add(new ItemStack(Items.CHARGED_POINT));
            })
            .build();

    public static final ItemGroup QUANTUM_FIELDS = FabricItemGroupBuilder.create(
                    new Identifier(Minelabs.MOD_ID, "quantum_fields"))
            .icon(() -> new ItemStack(Items.GLUON_QUANTUMFIELD))
            .appendItems(stack ->{
                stack.add(new ItemStack(Items.UPQUARK_QUANTUMFIELD));
                stack.add(new ItemStack(Items.DOWNQUARK_QUANTUMFIELD));
                stack.add(new ItemStack(Items.GLUON_QUANTUMFIELD));
                stack.add(new ItemStack(Items.ELECTRON_QUANTUMFIELD));
                stack.add(new ItemStack(Items.PHOTON_QUANTUMFIELD));
                stack.add(new ItemStack(Items.NEUTRINO_QUANTUMFIELD));
                stack.add(new ItemStack(Items.WEAK_BOSON_QUANTUMFIELD));
            })
            .build();

}
