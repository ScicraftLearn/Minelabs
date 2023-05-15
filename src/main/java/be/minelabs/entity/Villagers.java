package be.minelabs.entity;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.crafting.molecules.Atom;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class Villagers {

    //selects the block that the villager should be interested in and registers the profession
    public static final PointOfInterestType LEWIS_POI = registerPOI("lewis_poi", Blocks.LEWIS_BLOCK);
    public static final VillagerProfession SCIENCE_VILLAGER = registerProfession("sciencevillager",
            RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(Minelabs.MOD_ID, "lewis_poi")));

    /**
     * Create a new profession for villagers
     *
     * @param name : professions name
     * @param type : POI type, profession's workstation
     * @return VillagerProfession
     */
    public static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> type){
        return Registry.register(Registries.VILLAGER_PROFESSION, new Identifier(Minelabs.MOD_ID, name),
                makeProfession("minelabs:" + name, type, SoundEvents.ENTITY_VILLAGER_WORK_ARMORER));
    }

    private static VillagerProfession makeProfession(String id, RegistryKey<PointOfInterestType> workstation, SoundEvent workSound){
        return new VillagerProfession(id,
                entry -> entry.matches(poiTRegKey -> poiTRegKey.equals(workstation)),
                entry -> entry.matches(poiTRegKey -> poiTRegKey.equals(workstation)),
                ImmutableSet.of(),
                ImmutableSet.of(),
                workSound);
    }

    /**
     * Register new POI
     *
     * @param name  : POI name
     * @param block : Block of interest
     * @return PointOfInterestType
     */
    public static PointOfInterestType registerPOI(String name, Block block){
        return PointOfInterestHelper.register(new Identifier(Minelabs.MOD_ID, name),
                1,1, ImmutableSet.copyOf(block.getStateManager().getStates()));
    }

    //logger so that all the static fields get initialised correctly
    public static void registerVillagers() {
        Minelabs.LOGGER.debug("registering villagers");
        registerTrades();
    }

    /*list with all the items you can buy from this villager
     * The list are for a specific level and the moment the villager passes this level they stop being available
     * */
    public static void registerTrades(){
        TradeOfferHelper.registerVillagerOffers(SCIENCE_VILLAGER, 1,
                factories -> {
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 3),
                            new ItemStack(Atom.CARBON.getItem(), 5),
                            6,2,0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(be.minelabs.item.Items.ERLENMEYER, 16),
                            6,2,0.02f
                    )));
                });
        TradeOfferHelper.registerVillagerOffers(SCIENCE_VILLAGER, 2,
                factories -> {
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 1),
                            new ItemStack(be.minelabs.item.Items.LAB_CENTER, 2),
                            4,4,0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 3),
                            new ItemStack(be.minelabs.item.Items.LAB_SINK, 1),
                            2,4,0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(be.minelabs.item.Items.LAB_DRAWER, 1),
                            2,4,0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(be.minelabs.item.Items.LAB_CABIN, 1),
                            2,4,0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 1),
                            new ItemStack(be.minelabs.item.Items.LAB_CORNER, 2),
                            4,4,0.02f
                    )));
                });
        TradeOfferHelper.registerVillagerOffers(SCIENCE_VILLAGER, 3,
                factories -> {
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(be.minelabs.item.Items.MICROSCOPE, 1),
                            1,5,0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(be.minelabs.item.Items.TUBERACK, 1),
                            1,5,0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(be.minelabs.item.Items.BURNER, 1),
                            1,5,0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(be.minelabs.item.Items.LENS, 1),
                            8,5,0.02f
                    )));
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 2),
                            new ItemStack(be.minelabs.item.Items.BIG_LENS, 1),
                            8,5,0.02f
                    )));
                });
    }
}
