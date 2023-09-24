package be.minelabs.block.blocks;

import be.minelabs.block.entity.BlockEntities;
import be.minelabs.block.entity.ElectricFieldSensorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
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
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
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


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        //Only tick server, the result will be synced in this case
        return checkType(type, BlockEntities.ELECTRIC_FIELD_SENSOR, (world1, pos, state1, blockEntity) -> {
            if (!world1.isClient()) {
                blockEntity.tick(world1, pos, state1);
            }
        });
    }

}
