package be.uantwerpen.scicraft.event;

import be.uantwerpen.scicraft.dimension.ModDimensions;
import be.uantwerpen.scicraft.item.ItemGroups;
import be.uantwerpen.scicraft.item.Items;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.ActionResult;

public class ModEvents {
    public static void registerEvents() {
        //Check for dimension and block used
        UseBlockCallback.EVENT.register((player,world,hand,block)->{
            if(world.getRegistryKey()==ModDimensions.SUBATOM_KEY){
                if(player.getStackInHand(hand).getItem().getGroup()==ItemGroups.ATOMS || player.getStackInHand(hand).isOf(Items.ATOM_PORTAL)){
                    return ActionResult.PASS;
                }else {
                    return ActionResult.FAIL;
                }
            }else{
                if(player.getStackInHand(hand).getItem().getGroup()!=ItemGroups.ATOMS){
                    return ActionResult.FAIL;
                }else{
                    return ActionResult.PASS;
                }
            }
        });
    }
}
