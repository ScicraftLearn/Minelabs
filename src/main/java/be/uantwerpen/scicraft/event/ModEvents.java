package be.uantwerpen.scicraft.event;

import be.uantwerpen.scicraft.block.AtomicFloor;
import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.dimension.ModDimensions;
import be.uantwerpen.scicraft.item.ItemGroups;
import be.uantwerpen.scicraft.item.Items;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class ModEvents {

    public static void registerEvents() {
        //Check for dimension and block used
        UseBlockCallback.EVENT.register((player, world, hand, block) -> {
            ItemStack stack=player.getStackInHand(hand);
            //Allowed items should be added within this if statement, itemgroups or specific blocks
            if (world.getRegistryKey() == ModDimensions.SUBATOM_KEY) {
                if (stack.getItem().getGroup() == ItemGroups.ATOMS || stack.isOf(Items.ATOM_PORTAL)
                        || stack.isOf(Items.BOHR_BLOCK) || stack.isOf(Items.ATOM_FLOOR) || stack.isEmpty()
                        || stack.isOf(Items.NEUTRON) || stack.isOf(Items.PROTON) || stack.isOf(Items.ELECTRON)) {
                    return ActionResult.PASS;
                } else {
                    return ActionResult.FAIL;
                }
            } else if (world.getRegistryKey() != ModDimensions.SUBATOM_KEY && (player.getStackInHand(hand).isOf(Items.BOHR_BLOCK)
                    || player.getStackInHand(hand).isOf(Items.ATOM_FLOOR))) {
                return ActionResult.FAIL;
            } else {
                return ActionResult.PASS;
            }
        });

        //Removing fields when player leaves world
        ServerWorldEvents.UNLOAD.register((server, world) -> {
            if (world.getDimensionKey() == ModDimensions.DIMENSION_TYPE_KEY) {
                AtomicFloor.resetFields();
            }
        });
        //Removing fields from counter when the chunk is unloaded
        ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
            if (world.getDimensionKey() == ModDimensions.DIMENSION_TYPE_KEY) {
                AtomicFloor.resetFields();
            }
        });
        //Spawn portal on dimension load instead? done
        ServerWorldEvents.LOAD.register(((server, world) -> {
            if (world.getDimensionKey() == ModDimensions.DIMENSION_TYPE_KEY) ;
            {
                System.out.println("geladen?");
                world.setBlockState(new BlockPos(0, 1, 0), Blocks.ATOM_PORTAL.getDefaultState());
            }
        }));
    }
}
