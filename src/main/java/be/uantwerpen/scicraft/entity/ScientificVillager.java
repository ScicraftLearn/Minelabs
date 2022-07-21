package be.uantwerpen.scicraft.entity;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.Blocks;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class ScientificVillager {

    //selects the block that the villigat should be interested in and registers the profession
    public static final PointOfInterestType LEWIS_POI = registerPOI("lewis_poi", Blocks.LEWIS_BLOCK);
    public static final VillagerProfession SCIENCE_VILLAGER = registerProfession("sciencevillager",
            RegistryKey.of(Registry.POINT_OF_INTEREST_TYPE_KEY, new Identifier(Scicraft.MOD_ID, "lewis_poi")));

    //Creates a new profession based on the POI
    //This also sets the sound and can be used to set other stuff
    public static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> type){
        return Registry.register(Registry.VILLAGER_PROFESSION, new Identifier(Scicraft.MOD_ID,name),
                VillagerProfessionBuilder.create().id(new Identifier(Scicraft.MOD_ID,name)).workstation(type)
                        .workSound(SoundEvents.ENTITY_VILLAGER_WORK_ARMORER).build());
    }

    //registers the block that the villager needs to become a science villager
    public static PointOfInterestType registerPOI(String name, Block block){
        return PointOfInterestHelper.register(new Identifier(Scicraft.MOD_ID, name),
                1,1, ImmutableSet.copyOf(block.getStateManager().getStates()));
    }

    //logger so that all the static fields get initialised correctly
    public static void registerVillagers() {
        Scicraft.LOGGER.debug("Registering Villagers for " + Scicraft.MOD_ID);
    }

    //list with all the items you can buy from this villager
    public static void registerTrades(){
        TradeOfferHelper.registerVillagerOffers(SCIENCE_VILLAGER, 1,
                factories -> {
                    factories.add(((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 3),
                            new ItemStack(be.uantwerpen.scicraft.item.Items.CARBON_ATOM, 5),
                            6,2,0.02f
                    )));
                });
    }
}
