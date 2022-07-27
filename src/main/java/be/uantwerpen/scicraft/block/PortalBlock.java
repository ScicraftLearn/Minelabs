package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.BlockEntities;
import be.uantwerpen.scicraft.dimension.ModDimensions;
import be.uantwerpen.scicraft.item.ItemGroups;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.*;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

public class PortalBlock extends Block{
    public PortalBlock(Settings settings) {
        super(settings);
    }

    private Position playerpos = null;
    private Inventory inv = null;

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (!player.isSneaking()) {
                //Get world instance
                MinecraftServer server = world.getServer();
                if (server != null) {
                    if (player instanceof ServerPlayerEntity) {
                        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                        //Only teleport if an atom is used to right click
                        if (player.getStackInHand(hand).getItem().getGroup() == ItemGroups.ATOMS) {
                            //Consume 1 atom and teleport the player
                            useAtom(player);
                            teleportPlayer(world, server, serverPlayer, player);
                        } else {
                            System.out.println("geen atoom");
                        }
                        return ActionResult.SUCCESS;
                    }
                }
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    public static BlockPos getDest(BlockPos pos, World destWorld, boolean isInDimension) {
        double y = 61;

        if (!isInDimension) {
            y = pos.getY();
        }

        BlockPos destPos = new BlockPos(pos.getX(), y, pos.getZ());
        int tries = 0;
        while ((!destWorld.getBlockState(destPos).isAir() && !destWorld.getBlockState(destPos)
                .canBucketPlace(Fluids.WATER)) &&
                (!destWorld.getBlockState(destPos.up()).isAir() && !destWorld.getBlockState(destPos.up())
                        .canBucketPlace(Fluids.WATER)) && tries < 25) {
            destPos = destPos.up(2);
            tries++;
        }

        return destPos;
    }

    private void teleportPlayer(World world, MinecraftServer server, ServerPlayerEntity serverPlayer, PlayerEntity player) {
        ServerWorld overWorld = server.getWorld(World.OVERWORLD);
        //If player is in subatomic dimension
        if (world.getRegistryKey() == ModDimensions.SUBATOM_KEY && overWorld != null) {
            if (playerpos != null) {
                serverPlayer.teleport(overWorld, playerpos.getX(), playerpos.getY(), playerpos.getZ(),
                        serverPlayer.bodyYaw, serverPlayer.prevPitch);
            } else {
                playerpos = player.getPos();
                //Loop to see if the block is empty so the player can teleport
                for (double y = 0; y <= overWorld.getHeight(); y++) {
                    if (overWorld.getBlockEntity(new BlockPos(playerpos.getX(), playerpos.getY(), playerpos.getZ())) == null && overWorld.getBlockEntity(new BlockPos(playerpos.getX(), playerpos.getY() + 1, playerpos.getZ())) == null) {
                        serverPlayer.teleport(overWorld, playerpos.getX(), y, playerpos.getZ(),
                                serverPlayer.bodyYaw, serverPlayer.prevPitch);
                        break;
                    }
                }
            }
            //Consume 1 atom for teleport
            playerpos = null;
        }
        //If player is in overworld
        else {
            //Save player position so it can be returned
            playerpos = player.getPos();
            ServerWorld atomdim = server.getWorld(ModDimensions.SUBATOM_KEY);
            serverPlayer.teleport(atomdim, 0, -63, 0,
                    serverPlayer.bodyYaw, serverPlayer.prevPitch);
            //Inventory gets saved and then cleared
            inv = serverPlayer.getInventory();
            //serverPlayer.getInventory().clear();
        }
    }
    private void useAtom(PlayerEntity player){
        if(player.getMainHandStack().getCount()>1){
            player.getMainHandStack().setCount(player.getMainHandStack().getCount()-1);
        }else {
            player.getMainHandStack().setCount(0);
        }
    }
}
