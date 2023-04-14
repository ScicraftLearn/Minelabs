package be.uantwerpen.minelabs.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagnetItem extends Item {

    private static final double SPEED = 0.035;
    private static final double SPEED_4 = SPEED * 4;
    private final int RANGE = 5;
    private boolean enabled;

    public MagnetItem(Settings settings) {
        super(settings);
        enabled = false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (enabled) {
            if (entity instanceof PlayerEntity player) {
                if (world.isClient) return;
                List<Entity> toMove = player.getWorld().getOtherEntities(player,
                        new Box(player.getX() - RANGE, player.getY() - RANGE, player.getZ() - RANGE,
                                player.getX() + RANGE, player.getY() + RANGE, player.getZ() + RANGE),
                        this::isMagnetable);
                for (Entity movable_entity : toMove) {
                    double x = player.getX() - movable_entity.getX();
                    //for y value, make attraction point a little bellow eye level for best visual effect
                    double y = player.getY() + player.getEyeY() * .75f - movable_entity.getY();
                    double z = player.getZ() - movable_entity.getZ();
                    double distanceSq = x * x + y * y + z * z;
                    double adjustedSpeed = SPEED / distanceSq;

                    if (distanceSq < 1.5625) {
                        movable_entity.onPlayerCollision(player);
                    } else {
                        Direction mov = player.getHorizontalFacing().getOpposite();

                        double deltaX = mov.getOffsetX() + x * adjustedSpeed;
                        double deltaZ = mov.getOffsetZ() + z * adjustedSpeed;
                        double deltaY = y>0 ? 0.12 : mov.getOffsetY() + y * SPEED;

                        movable_entity.setVelocity(deltaX, deltaY, deltaZ);
                    }
                }
            }
        }

    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (enabled) {
            tooltip.add(Text.translatable("text.minelabs.active"));
        } else {
            tooltip.add(Text.translatable("text.minelabs.inactive"));
        }
        tooltip.add(Text.translatable("text.minelabs.magnet_instruction"));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
            if (!world.isClient) {
                ItemStack stack = user.getStackInHand(hand);
                enabled = !isEnabled();
                user.sendMessage(Text.of("TOGGLE MAGNET: " + enabled));
            }
            user.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }

        return super.use(world, user, hand);
    }

    private boolean isMagnetable(Entity entity) {
        // COULD ADD TAGS IF NEEDED
        return entity instanceof ExperienceOrbEntity || entity instanceof ItemEntity;
    }

    private boolean isEnabled() {
        return enabled;
    }

    private NbtCompound serialize() {
        NbtCompound tag = new NbtCompound();
        tag.putBoolean("minelabs.enabled", enabled);
        return tag;
    }

    private void deserialize(NbtCompound nbt) {
        this.enabled = nbt.getBoolean("minelabs.enabled");
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return enabled;
    }
}
