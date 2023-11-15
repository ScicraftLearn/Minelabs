package be.minelabs.item.items;

import be.minelabs.entity.projectile.thrown.PointChargedEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChargedPointItem extends ChargedItem {
    public ChargedPointItem(Settings settings) {
        super(settings);
    }

    @Override
    protected ProjectileEntity createThrownEntity(PlayerEntity user, World world, ItemStack stack) {
        return new PointChargedEntity(user, world, stack);
    }

    @Override
    protected ProjectileEntity createPlacedEntity(World world, BlockPos pos, ItemStack stack) {
        return new PointChargedEntity(world, pos, stack);
    }
}
