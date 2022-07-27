package be.uantwerpen.scicraft.event;

import be.uantwerpen.scicraft.item.ItemGroups;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;

public class ModEvents {
    public static void registerEvents() {
        UseItemCallback.EVENT.register((player, item, hand) ->
                {
                    //Checks to prevent null errors
                    ItemStack stack = player.getStackInHand(hand);
                    if (stack.getItem().getGroup() == ItemGroups.ATOMS) {
                        System.out.println("Atom baby!");
                        return TypedActionResult.consume(stack);
                    } else {
                        System.out.println("No blocks");
                        return TypedActionResult.fail(ItemStack.EMPTY);
                    }
                }
        );
    }
}
