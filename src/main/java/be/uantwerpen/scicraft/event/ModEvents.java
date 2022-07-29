package be.uantwerpen.scicraft.event;

import be.uantwerpen.scicraft.block.AtomicFloor;
import be.uantwerpen.scicraft.dimension.ModDimensions;
import be.uantwerpen.scicraft.item.ItemGroups;
import be.uantwerpen.scicraft.item.Items;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public class ModEvents {
    public static boolean isOut=false;

    public static void registerEvents() {
        //Check for dimension and block used
        UseBlockCallback.EVENT.register((player,world,hand,block)->{
            //Allowed items should be added within this if statement, itemgroups or specific blocks
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
        //Removing fields when player leaves world
        ServerWorldEvents.UNLOAD.register((server,world)-> {
            if(world.getDimensionKey().equals(ModDimensions.SUBATOM_KEY)){
                AtomicFloor.resetFields();
            }
        });
        //Removing fields from counter when the chunk is unloaded
        ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
            if(world.getDimensionKey().equals(ModDimensions.SUBATOM_KEY)){
                AtomicFloor.resetFields();
                isOut=true;
            }
        });
    }
}
