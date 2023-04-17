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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagnetItem extends Item {

    private static final float SPEED = 0.35F;
    private boolean enabled;

    public MagnetItem(Settings settings) {
        super(settings);
        enabled = false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        // https://github.com/VazkiiMods/Botania/blob/1.19.x/Xplat/src/main/java/vazkii/botania/common/item/equipment/bauble/RingOfMagnetizationItem.java
        // https://github.com/VazkiiMods/Botania/blob/9c94927a7289b8a1212ba38d1c3901bb16cc7ece/Xplat/src/main/java/vazkii/botania/common/helper/MathHelper.java#L30
        // https://github.com/VazkiiMods/Botania/blob/9c94927a7289b8a1212ba38d1c3901bb16cc7ece/Xplat/src/main/java/vazkii/botania/common/helper/VecHelper.java
        int RANGE = 8;
        if (enabled) {
            if (entity instanceof PlayerEntity player) {
                if (world.isClient) return;
                List<Entity> toMove = player.getWorld().getOtherEntities(player,
                        new Box(player.getX() - RANGE, player.getY() - RANGE, player.getZ() - RANGE,
                                player.getX() + RANGE, player.getY() + RANGE, player.getZ() + RANGE),
                        this::isMagnetable);
                for (Entity movable_entity : toMove) {
                    double x = player.getX();
                    double y = player.getY() + 0.75; // Mid-body / eye height
                    double z = player.getZ();

                    Vec3d vec = new Vec3d(x, y, z);

                    Vec3d entityVector = new Vec3d(movable_entity.getX(), movable_entity.getY(), movable_entity.getZ());
                    Vec3d finalVector = vec.subtract(entityVector);

                    if (finalVector.length() > 1) {
                        finalVector = finalVector.normalize();
                    }

                    //entity.setDeltaMovement(finalVector.scale(modifier));
                    movable_entity.setVelocity(finalVector.multiply(SPEED));
                    /*

                    double distanceSq = Math.sqrt(x * x + y * y + z * z);
                    double adjustedSpeed = SPEED*3 / distanceSq;

                    if (distanceSq < 1.5625) {
                        movable_entity.onPlayerCollision(player);
                    } else {
                        double deltaX = movable_entity.getX() + x * adjustedSpeed;
                        double deltaZ = movable_entity.getZ()+ z * adjustedSpeed;
                        double deltaY = y>0 ? movable_entity.getY() + 0.12 : movable_entity.getY() + y * SPEED;

                        //movable_entity.setPos(deltaX, deltaY, deltaZ);
                        movable_entity.setVelocity(deltaX, deltaY, deltaZ);
                    }*/
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
                enabled = !enabled;
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
