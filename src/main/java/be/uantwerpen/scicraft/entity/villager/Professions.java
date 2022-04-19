package be.uantwerpen.scicraft.entity.villager;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.item.Items;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class Professions {
	
	public static void registerProfesions() {
		fillTradeData();
	}
	
	public static final PointOfInterestType PHYSICIST_POI = PointOfInterestHelper.register(new Identifier(Scicraft.MOD_ID, "physicist"), 1, 1, Blocks.PION_NUL); //Workstation
	
	public static final VillagerProfession PHYSICIST_PROFESSION = Registry.register(Registry.VILLAGER_PROFESSION, new Identifier(Scicraft.MOD_ID, "physicist"), VillagerProfessionBuilder.create() //Villager profession
			.id(new Identifier(Scicraft.MOD_ID, "physicist"))
			.workstation(PHYSICIST_POI) //TODO change
			//.workSound(null) work sound
			.build());
	
	public static void fillTradeData() { //Villager trades
		TradeOfferHelper.registerVillagerOffers(PHYSICIST_PROFESSION, 1, (f)->{
            f.add((entity, random) -> new TradeOffer(new ItemStack(Items.PROTON,1), new ItemStack(Items.NEUTRON,1), 16, 20, 0.8f));
            }
		);
	}

}
