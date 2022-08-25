package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.block.entity.BlockEntities;
import be.uantwerpen.minelabs.block.entity.TimeFreezeBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TimeFreezeBlock extends BlockWithEntity {

    private final int freezeRadius = 4;

    public TimeFreezeBlock() {
        super(FabricBlockSettings.of(Material.GLASS).noCollision().strength(0.5f, 2.0f));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {

//        List<Entity> entitiesInRange_8_BLOCKS = getEntitiesInRange(pos, world, 8);
//
//        for(Entity entity : entitiesInRange_8_BLOCKS) {
//            entity.setFrozenTicks(0);
//        }

        super.onBreak(world, pos, state, player);
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TimeFreezeBlockEntity(BlockEntities.TIME_FREEZE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, BlockEntities.TIME_FREEZE_BLOCK_ENTITY, TimeFreezeBlockEntity::tick);
    }

    //    @Override
//    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
//        super.randomDisplayTick(state, world, pos, random); // does nothing
//
//        List<Entity> entitiesInRange_8_BLOCKS = getEntitiesInRange(pos, world, 8);
//
//        for(Entity entity : entitiesInRange_8_BLOCKS) {
//
//            // don't freeze players
//            if(!(entity instanceof PlayerEntity) && (entity instanceof LivingEntity living)) {
//                living.slowMovement(state, Vec3d.ZERO);
//                living.setInvulnerable(true);
//                //isPushable()
//                living.setBodyYaw(0);
//                living.setHeadYaw(0);
//                if((living instanceof MobEntity mob)) {
//                    mob.setAiDisabled(true);
//                    mob.getNavigation().stop();
//                }
//            }
//        }
//    }
//
//    private List<Entity> getEntitiesInRange(BlockPos pos, World world, int r) {
//
//        return world.getOtherEntities(
//                null, new Box(pos.getX()-r, pos.getY()-r, pos.getZ()-r, pos.getX()+r, pos.getY()+r, pos.getZ()+r), new Predicate<Entity>() {
//                    @Override
//                    public boolean test(Entity entity) {
//                        return true;
//                    }
//                });
//    }
}
