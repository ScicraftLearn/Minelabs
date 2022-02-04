package be.uantwerpen.scicraft.item;

import be.uantwerpen.scicraft.dimension.CustomDimension;
import be.uantwerpen.scicraft.entity.ElectronEntity;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import java.util.Objects;

public class DimensionItem extends Item {
    public DimensionItem(Settings settings) {
        super(settings);
    }

    /**
     * When ElectronItem is right-clicked, use up the item if necessary and spawn the entity
     * @param world minecraft world
     * @param user player invoking the right click action
     * @param hand the hand of the user
     * @return TypedActionResult, indicates if the use of the item succeeded or not
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand); // creates a new ItemStack instance of the user's itemStack in-hand

        // 20 gameticks cooldown, 1 sec same as enderPearl
        user.getItemCooldownManager().set(this, 20);

        if (!world.isClient && user instanceof ServerPlayerEntity player) {
            MinecraftServer server = world.getServer();
            if (server == null) {
                return TypedActionResult.fail(itemStack);
            }
            ServerWorld serverWorld = player.getWorld();
            ServerWorld modWorld = CustomDimension.getModWorld(server);

            if (serverWorld != modWorld) {
                TeleportTarget target = new TeleportTarget(new Vec3d(0.5, 101, 0.5), Vec3d.ZERO, 0, 0);
                FabricDimensions.teleport(player, modWorld, target);

                if (player.world != modWorld) {
                    return TypedActionResult.fail(itemStack);
                }

                modWorld.setBlockState(new BlockPos(0, 100, 0), Blocks.DIAMOND_BLOCK.getDefaultState());
                modWorld.setBlockState(new BlockPos(0, 101, 0), Blocks.TORCH.getDefaultState());
            } else {
                TeleportTarget target = new TeleportTarget(new Vec3d(0, 100, 0), Vec3d.ZERO,
                        (float) Math.random() * 360 - 180, (float) Math.random() * 360 - 180);
                FabricDimensions.teleport(player, CustomDimension.getWorld(world.getServer(), World.OVERWORLD), target);
            }
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1); // decrements itemStack if user is not in creative mode
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
