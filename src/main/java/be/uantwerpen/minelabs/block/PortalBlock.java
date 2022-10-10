package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.dimension.ModDimensions;
import be.uantwerpen.minelabs.item.ItemGroups;
import be.uantwerpen.minelabs.item.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Objects;

public class PortalBlock extends Block{
    public PortalBlock(Settings settings) {
        super(settings);
    }

    private Position playerpos = null;

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                //Only teleport if an atom is used to right-click
                if (player.getStackInHand(hand).getItem().getGroup() == ItemGroups.ATOMS) {
                    RegistryKey<World> playerRegistryKey = player.getWorld().getRegistryKey();
                    if (!(ModDimensions.SUBATOM_KEY.equals(playerRegistryKey) || World.OVERWORLD.equals(playerRegistryKey))) {
                        world.removeBlock(pos, false);
                        world.createExplosion(null, DamageSource.badRespawnPoint(), null, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 5.0f, true, Explosion.DestructionType.DESTROY);

                        return ActionResult.PASS;
                    }

                    //Consume 1 atom and teleport the player
                    //useAtom(player);
                    //serverPlayer.moveToWorld(serv);
                    teleportPlayer(world, Objects.requireNonNull(world.getServer()), serverPlayer, player);
                } else {
                    System.out.println("geen atoom");
                }
                return ActionResult.SUCCESS;
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        ItemStack stack = new ItemStack(Items.ATOM_PORTAL);
        Inventory inv = new SimpleInventory(stack);
        ItemScatterer.spawn(world, pos, inv);
        super.onBreak(world, pos, state, player);
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
        }
        //If player is in overworld
        else {
            //Save player position so it can be returned
            playerpos = player.getPos();
            ServerWorld atomdim = server.getWorld(ModDimensions.SUBATOM_KEY);
            assert atomdim != null;
            serverPlayer.teleport(atomdim, 0, 67, 0,
                    serverPlayer.bodyYaw, serverPlayer.prevPitch);

        }
    }

    public static boolean isPortalWorking(World world) {
        return world.getDimension().bedWorks();
    }

}
