package be.minelabs.item.items;

import be.minelabs.entity.projectile.thrown.ChargedEntity;
import be.minelabs.world.MinelabsGameRules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ForceCompassItem extends Item {

    public ForceCompassItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        // Stops the bobbing of the item when the field gets updated
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient || !selected) {
            // Stop calculation if client and if not selected
            return;
        }

        Vec3d field = Vec3d.ZERO;
        int e_radius = world.getGameRules().getInt(MinelabsGameRules.E_RADIUS);
        List<Entity> entities = world.getOtherEntities(entity,
                Box.of(entity.getPos(), e_radius, e_radius, e_radius), e -> e instanceof ChargedEntity);

        for (Entity entiti : entities) {
            if (entiti instanceof ChargedEntity charged) {
                double force = 8.987f * charged.getCharge() / charged.distanceTo(entity);
                Vec3d vector = entity.getPos().subtract(charged.getPos()).normalize(); // Vector between entities
                vector = vector.multiply(force); //scale vector with Force
                vector = vector.multiply(0.1);

                field = field.add(vector);
            }
        }
        if (field.length() >= ChargedEntity.MAX_FIELD) {
            field = field.multiply(ChargedEntity.MAX_FIELD / field.length()); // SCALE TO MAX_FIELD
        }
        setField(stack, field);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        // print/send message to player about field
        user.sendMessage(Text.of(getField(user.getStackInHand(hand)).toString()), false);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    public static void setField(ItemStack stack, Vec3d field) {
        NbtCompound nbt = stack.getOrCreateNbt(); // Don't want to "remove/destroy" other nbt data
        nbt.putDouble("ex", field.x);
        nbt.putDouble("ey", field.y);
        nbt.putDouble("ez", field.z);
        stack.setNbt(nbt);
    }

    public static Vec3d getField(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("ex") && nbt.contains("ey") && nbt.contains("ez")) {
            return new Vec3d(nbt.getDouble("ex"), nbt.getDouble("ey"), nbt.getDouble("ez"));
        }
        return Vec3d.ZERO;
    }
}
