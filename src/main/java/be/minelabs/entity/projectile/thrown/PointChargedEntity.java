package be.minelabs.entity.projectile.thrown;

import be.minelabs.entity.Entities;
import be.minelabs.screen.ChargedPointScreenHandler;
import be.minelabs.util.Tags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PointChargedEntity extends ChargedEntity {

    private final SimpleInventory inventory = new SimpleInventory(1) {
        @Override
        public void onClose(PlayerEntity player) {
            super.onClose(player);
            ItemStack stack = this.getStack(0);
            if (!stack.isEmpty()) {
                int count = stack.getCount();
                if (stack.isIn(Tags.Items.POSITIVE_CHARGE)) {
                    setCharge(count);
                } else if (stack.isIn(Tags.Items.NEGATIVE_CHARGE)) {
                    setCharge(-count);
                } else {
                    setCharge(0);
                }
            } else {
                setCharge(0);
            }
        }
    };

    public PointChargedEntity(EntityType<? extends PointChargedEntity> entityType, World world) {
        super(entityType, world);
    }

    public PointChargedEntity(LivingEntity owner, World world, ItemStack stack) {
        super(Entities.POINT_CHARGED_ENTITY, owner, world, stack);
    }

    public PointChargedEntity(World world, BlockPos pos, ItemStack stack) {
        super(Entities.POINT_CHARGED_ENTITY, world, pos, stack);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        player.openHandledScreen(new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return Text.translatable("entity.minelabs.point_charge");
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new ChargedPointScreenHandler(syncId, playerInventory, inventory);
            }
        });
        return ActionResult.success(world.isClient);
    }
}