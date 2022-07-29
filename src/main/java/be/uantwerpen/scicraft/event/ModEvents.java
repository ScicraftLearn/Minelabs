package be.uantwerpen.scicraft.event;

import be.uantwerpen.scicraft.block.AtomicFloor;
import be.uantwerpen.scicraft.dimension.ModDimensions;
import be.uantwerpen.scicraft.item.ItemGroups;
import be.uantwerpen.scicraft.item.Items;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.world.WorldEvents;

public class ModEvents {
    public static void registerEvents() {
        //Check for dimension and block used
        UseBlockCallback.EVENT.register((player,world,hand,block)->{
            if(world.getRegistryKey()==ModDimensions.SUBATOM_KEY){
                if(player.getStackInHand(hand).getItem().getGroup()==ItemGroups.ATOMS || player.getStackInHand(hand).isOf(Items.ATOM_PORTAL)
                        || player.getStackInHand(hand).isOf(Items.BOHR_BLOCK) || player.getStackInHand(hand).isOf(Items.ATOM_FLOOR)){
                    return ActionResult.PASS;
                }else {
                    return ActionResult.FAIL;
                }
            }else{
                return ActionResult.PASS;
            }
        });
        ServerWorldEvents.UNLOAD.register((server,world)-> {
            if(world.getDimensionKey().equals(ModDimensions.SUBATOM_KEY)){
                System.out.println("clearing fields");
                AtomicFloor.resetFields();
            }
        });
    }
}
