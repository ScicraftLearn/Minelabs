package be.uantwerpen.scicraft.event;

import be.uantwerpen.scicraft.dimension.ModDimensions;
import be.uantwerpen.scicraft.item.ItemGroups;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;

//Klasse werkt niet
public class ModEvents {
    public static void registerEvents() {
        UseItemCallback.EVENT.register((player, world, hand) ->
                {
                    if (world.getRegistryKey()==(ModDimensions.SUBATOM_KEY)) {
                        ItemStack stack=player.getStackInHand(hand);
                        if(stack.getItem().getGroup()== ItemGroups.ATOMS){
                            System.out.println("ja!");
                            return TypedActionResult.success(player.getStackInHand(hand));
                        }
                        else{
                            System.out.println("neen");
                            return TypedActionResult.fail(stack);
                        }
                    } else {
                        System.out.println("andere dimensie");
                        return TypedActionResult.success(player.getStackInHand(hand));
                    }
                }
        );
    }
}
