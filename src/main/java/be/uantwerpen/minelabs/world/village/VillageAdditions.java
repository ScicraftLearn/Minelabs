package be.uantwerpen.minelabs.world.village;

import be.uantwerpen.minelabs.Minelabs;
import fzzyhmstrs.structurized_reborn.impl.FabricStructurePoolRegistry;
import net.minecraft.util.Identifier;

public class VillageAdditions {

    public static void registerVillageStructures(){
        FabricStructurePoolRegistry.registerSimple(
                new Identifier("minecraft:village/plains/houses"),
                new Identifier(Minelabs.MOD_ID, "plains_scientist_1"),
                2); // SAME AS WEAPON SMITH
    }
}
