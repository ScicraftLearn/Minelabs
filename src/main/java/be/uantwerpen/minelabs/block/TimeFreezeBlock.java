package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.block.entity.BlockEntities;
import be.uantwerpen.minelabs.block.entity.ChargedBlockEntity;
import be.uantwerpen.minelabs.block.entity.TimeFreezeBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class TimeFreezeBlock extends BlockWithEntity {

    public TimeFreezeBlock() {
        super(FabricBlockSettings.of(Material.GLASS).noCollision().strength(0.5f, 2.0f));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        int e_radius = 16;
        Iterable<BlockPos> blocks_in_radius = BlockPos.iterate(pos.mutableCopy().add(-e_radius, -e_radius, -e_radius), pos.mutableCopy().add(e_radius, e_radius, e_radius));

        // go over all blocks in range of the time freeze block that you just broke
        for (BlockPos pos_block : blocks_in_radius) {
            // if charged blocks are found, check if there are more time freeze blocks around them
            if (world.getBlockEntity(pos_block) instanceof ChargedBlockEntity charged && !pos.equals(pos_block)) {
                Iterable<BlockPos> blocks_in_radius_of_charged = BlockPos.iterate(pos_block.mutableCopy().add(-e_radius, -e_radius, -e_radius), pos_block.mutableCopy().add(e_radius, e_radius, e_radius));
                boolean update = true;
                for (BlockPos new_pos : blocks_in_radius_of_charged) {
                    // make sure the TimeFreezeBlockEntity isn't the one you just broke (it is still in the world at this point in time)
                    if (world.getBlockEntity(new_pos) instanceof TimeFreezeBlockEntity && !pos_block.equals(new_pos) && !pos.equals(new_pos)) {
                        update = false;
                        break;
                    }
                }
                // if there are no other time freeze blocks around them, you can play their animation (if there is one)
                if(update) {
                    charged.needsUpdate(true);
                    //System.out.println("UPDATE NOW");
                }
            }
        }

//        List<Entity> entitiesInRange_8_BLOCKS = getEntitiesInRange(pos, world, 8);
//
//        for(Entity entity : entitiesInRange_8_BLOCKS) {
//            ALLOW MOVEMENT FOR ENTITIES AGAIN HERE
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

        @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random); // does nothing

        // freeze the entities
//        List<Entity> entitiesInRange_8_BLOCKS = getEntitiesInRange(pos, world, 8);
//        for(Entity entity : entitiesInRange_8_BLOCKS) {
//
//            // don't freeze players
//            if(!(entity instanceof PlayerEntity) && (entity instanceof LivingEntity living)) {
//                living.slowMovement(state, Vec3d.ZERO);
//                living.setInvulnerable(true);
//                //isPushable()  //perhaps make accessor mixin to make function setPushable(boolean)
//                living.setBodyYaw(0);
//                living.setHeadYaw(0);
//                if((living instanceof MobEntity mob)) {
//                    mob.setAiDisabled(true);
//                    mob.getNavigation().stop();
//                }
//            }
//        }
    }

    /**
     * This function gets all entities in range  and can be used to stop movement for all the
     * entities in the range of the Time Freeze Block
     * @param pos: BlockPos of the Time Freeze Block
     * @param world: current world
     * @param r: radius in which u want the entities (a cube, not a sphere)
     * @return list of entities
     */
    public static List<Entity> getEntitiesInRange(BlockPos pos, World world, int r) {
        return world.getOtherEntities(
                null, new Box(pos.getX()-r, pos.getY()-r, pos.getZ()-r, pos.getX()+r, pos.getY()+r, pos.getZ()+r), new Predicate<Entity>() {
                    @Override
                    public boolean test(Entity entity) {
                        return true;
                    }
                });
    }
}
