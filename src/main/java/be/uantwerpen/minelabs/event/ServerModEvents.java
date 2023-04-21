package be.uantwerpen.minelabs.event;

import be.uantwerpen.minelabs.dimension.ModDimensions;
import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.item.ItemGroups;
import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.mixins.FishingBobberEntityAccessor;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;


public class ServerModEvents {
    public static void registerEvents() {
        //Check for dimension and block used
        UseBlockCallback.EVENT.register((player, world, hand, block) -> {
            if (!player.getAbilities().creativeMode) {
                ItemStack stack = player.getStackInHand(hand);
                //Allowed items should be added within this if statement, itemgroups or specific blocks
                if (world.getRegistryKey() == ModDimensions.SUBATOM_KEY) {
                    if (stack.getItem().getGroup() == ItemGroups.ATOMS || stack.isOf(Items.BOHR_BLUEPRINT)
                            || stack.isOf(Items.ATOM_FLOOR) || stack.isEmpty()
                            || stack.getItem().getGroup() == ItemGroups.ELEMENTARY_PARTICLES || stack.getItem().getGroup() == ItemGroups.QUANTUM_FIELDS) {

                        return ActionResult.PASS;
                    } else {
                        return ActionResult.FAIL;
                    }
                } else if (world.getRegistryKey() != ModDimensions.SUBATOM_KEY && player.getStackInHand(hand).isOf(Items.ATOM_FLOOR)) {
                    return ActionResult.FAIL;
                } else {
                    return ActionResult.PASS;
                }
            }
            return ActionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!player.getStackInHand(hand).isOf(net.minecraft.item.Items.FISHING_ROD))
                return TypedActionResult.pass(player.getStackInHand(hand));

            FishingBobberEntity fishHook = player.fishHook;
            if (fishHook != null && fishHook.getHookedEntity() instanceof BohrBlueprintEntity entity && !world.isClient){
                entity.extractByRod((ServerPlayerEntity) player, fishHook);
            }
            return TypedActionResult.pass(player.getStackInHand(hand));
        });
    }
}
