package be.minelabs.item.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
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

public abstract class AbstractMagnet extends Item {

    protected static float SPEED = 0.03F;
    protected static int RANGE = 8;
    private static final String ENABLED_KEY = "minelabs.enabled";

    public AbstractMagnet(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        // https://github.com/VazkiiMods/Botania/blob/1.19.x/Xplat/src/main/java/vazkii/botania/common/item/equipment/bauble/RingOfMagnetizationItem.java
        // https://github.com/VazkiiMods/Botania/blob/9c94927a7289b8a1212ba38d1c3901bb16cc7ece/Xplat/src/main/java/vazkii/botania/common/helper/MathHelper.java#L30
        // https://github.com/VazkiiMods/Botania/blob/9c94927a7289b8a1212ba38d1c3901bb16cc7ece/Xplat/src/main/java/vazkii/botania/common/helper/VecHelper.java
        if (getState(stack)) {
            if (entity instanceof PlayerEntity player) {
                if (world.isClient) return;
                List<Entity> toMove = player.getWorld().getOtherEntities(player,
                        new Box(player.getX() - RANGE, player.getY() - RANGE, player.getZ() - RANGE,
                                player.getX() + RANGE, player.getY() + RANGE, player.getZ() + RANGE),
                        this::isAttractable);
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
                    finalVector.multiply(SPEED);

                    movable_entity.addVelocity(finalVector.x, finalVector.y, finalVector.z);
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (getState(stack)) {
            tooltip.add(Text.translatable("text.minelabs.active"));
        } else {
            tooltip.add(Text.translatable("text.minelabs.inactive"));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
            if (!world.isClient) {
                this.toggleMagnet(user.getStackInHand(hand));
            }
            user.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }

        return super.use(world, user, hand);
    }

    /**
     * Toggle the Magnet.
     * Will change the nbt of the stack
     *
     * @param stack: ItemStack of this type
     */
    protected void toggleMagnet(ItemStack stack){
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putBoolean(ENABLED_KEY, !nbt.getBoolean(ENABLED_KEY));
    }

    /**
     * Set the Magnet's state (active/inactive)
     *
     * @param stack : ItemStack of this type
     * @param state : true/false, should magnet be enabled
     */
    protected void setState(ItemStack stack, boolean state){
        stack.getOrCreateNbt().putBoolean(ENABLED_KEY, state);
    }

    /**
     * Get the Magnet's state (active/inactive)
     *
     * @param stack : ItemStack of this type
     * @return boolean: true/false
     */
    protected boolean getState(ItemStack stack){
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(ENABLED_KEY)){
            return nbt.getBoolean(ENABLED_KEY);
        }
        return false;
    }

    /**
     * Only attracts Experience Orbs by default
     *
     * @param entity   : Entity to attract
     * @return boolean : true/false
     */
    protected boolean isAttractable(Entity entity) {
        return entity instanceof ExperienceOrbEntity;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return getState(stack);
    }
}
