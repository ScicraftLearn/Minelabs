package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.block.entity.BlockEntities;
import be.uantwerpen.minelabs.block.entity.ElectricFieldSensorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ElectricFieldSensorBlock extends BlockWithEntity {

    public ElectricFieldSensorBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntities.ELECTRIC_FIELD_SENSOR.instantiate(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof ElectricFieldSensorBlockEntity sensor) {
            sensor.calculateField(world, pos, null);
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof ElectricFieldSensorBlockEntity sensor) {
                player.sendMessage(Text.of(sensor.getField().toString()), false);
            }

        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
