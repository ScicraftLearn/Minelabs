package be.uantwerpen.scicraft.event;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.block.entity.BohrBlockEntity;
import be.uantwerpen.scicraft.dimension.ModDimensions;
import be.uantwerpen.scicraft.item.ItemGroups;
import be.uantwerpen.scicraft.item.Items;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;


public class ModEvents {

    public static void registerEvents() {

        //Check for dimension and block used
        UseBlockCallback.EVENT.register((player, world, hand, block) -> {
            ItemStack stack = player.getStackInHand(hand);
            //Allowed items should be added within this if statement, itemgroups or specific blocks
            if (world.getRegistryKey() == ModDimensions.SUBATOM_KEY) {
                if (stack.getItem().getGroup() == ItemGroups.ATOMS || stack.isOf(Items.ATOM_PORTAL)
                        || stack.isOf(Items.BOHR_BLOCK) || stack.isOf(Items.ATOM_FLOOR) || stack.isEmpty()
                        || stack.getItem().getGroup() == ItemGroups.ELEMENTARY_PARTICLES || stack.getItem().getGroup() == ItemGroups.QUANTUM_FIELDS) {

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

        //Spawn portal on dimension load instead? done
        ServerWorldEvents.LOAD.register(((server, world) -> {
            if (world.getDimensionKey() == ModDimensions.DIMENSION_TYPE_KEY) ;
            {
                System.out.println("geladen?");
                world.setBlockState(new BlockPos(0, 6, 0), Blocks.ATOM_PORTAL.getDefaultState());
                for (PlayerEntity p : world.getPlayers()
                ) {
                    //IMPLEMENT PLAYER CHANGES
                }
            }
        }));

//        render bohr block text on hud rendering when a bohr block is being looked at
        HudRenderCallback.EVENT.register(
                (matrixStack, delta) -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    HitResult hitResult = client.crosshairTarget;

                    assert client.world != null;
                    if (hitResult instanceof BlockHitResult blockHitResult) {
                        BlockEntity blockEntity = client.world.getBlockEntity(blockHitResult.getBlockPos());
                        if (blockEntity instanceof BohrBlockEntity) {
                            ((BohrBlockEntity) blockEntity).renderText();
                        }
                    }


                }
        );

    }
}
