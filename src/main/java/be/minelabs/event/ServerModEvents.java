package be.minelabs.event;

import be.minelabs.advancement.criterion.BohrCriterion;
import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.world.dimension.ModDimensions;
import be.minelabs.entity.BohrBlueprintEntity;
import be.minelabs.item.items.AtomItem;
import be.minelabs.item.ItemGroups;
import be.minelabs.item.Items;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
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
                    if (ItemGroups.ATOMS.contains(stack) || stack.isOf(Items.BOHR_BLUEPRINT)
                            || stack.isOf(Items.ATOM_FLOOR) || stack.isEmpty()
                            || ItemGroups.ELEMENTARY_PARTICLES.contains(stack)
                            || ItemGroups.QUANTUM_FIELDS.contains(stack)){
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
            if (world.isClient || !player.getStackInHand(hand).isOf(net.minecraft.item.Items.FISHING_ROD))
                return TypedActionResult.pass(player.getStackInHand(hand));

            FishingBobberEntity fishHook = player.fishHook;
            if (fishHook != null && fishHook.getHookedEntity() instanceof BohrBlueprintEntity entity){
                ItemStack stack = entity.extractByRod((ServerPlayerEntity) player, fishHook);

                if(!stack.isEmpty()){
                    // advancement
                    if (stack.getItem() instanceof AtomItem)
                        Criteria.BOHR_CRITERION.trigger((ServerPlayerEntity) player, BohrCriterion.Type.REMOVE_ATOM, true);
                    else
                        Criteria.BOHR_CRITERION.trigger((ServerPlayerEntity) player, BohrCriterion.Type.REMOVE_PARTICLE, true);
                }
            }
            return TypedActionResult.pass(player.getStackInHand(hand));
        });
    }
}
