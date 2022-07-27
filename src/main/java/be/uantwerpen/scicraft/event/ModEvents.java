package be.uantwerpen.scicraft.event;

import be.uantwerpen.scicraft.dimension.ModDimensions;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;

//Klasse werkt niet
public class ModEvents {
    public static void registerEvents() {
        UseItemCallback.EVENT.register((player, world, hand) ->
                {
                    if (world.getDimensionKey().equals(ModDimensions.SUBATOM_KEY)) {
                        System.out.println("ja!");
                        return TypedActionResult.success(player.getStackInHand(hand));
                    } else {
                        System.out.println("nee");
                        return TypedActionResult.fail(ItemStack.EMPTY);
                    }
                }
        );
    }
}
