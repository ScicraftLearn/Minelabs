package be.uantwerpen.minelabs.world.village;

import be.uantwerpen.minelabs.Minelabs;
import fzzyhmstrs.structurized_reborn.impl.FabricStructurePoolRegistry;
import net.minecraft.util.Identifier;

public class VillageAdditions {

    public static void registerVillageStructures(){
        //TODO OTHER (BIOME: TAIGA) HOUSES
        // CHECK HOUSE/PROFESSION POOL
        FabricStructurePoolRegistry.registerSimple(
                new Identifier("minecraft:village/plains/houses"),
                new Identifier(Minelabs.MOD_ID, "village/plains_scientist_1"),
                2); // SAME AS WEAPON SMITH
        FabricStructurePoolRegistry.registerSimple(
                new Identifier("minecraft:village/savanna/houses"),
                new Identifier(Minelabs.MOD_ID, "village/savanna_scientist_1"),
                2);
        FabricStructurePoolRegistry.registerSimple(
                new Identifier("minecraft:village/snowy/houses"),
                new Identifier(Minelabs.MOD_ID, "village/snowy_scientist_1"),
                2);
        FabricStructurePoolRegistry.registerSimple(
                new Identifier("minecraft:village/snowy/houses"),
                new Identifier(Minelabs.MOD_ID, "village/snowy_scientist_2"),
                2);
        FabricStructurePoolRegistry.registerSimple(
                new Identifier("minecraft:village/desert/houses"),
                new Identifier(Minelabs.MOD_ID, "village/desert_scientist_1"),
                2);
    }
}
