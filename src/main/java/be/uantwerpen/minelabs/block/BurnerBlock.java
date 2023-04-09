package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.util.MinelabsProperties;
import be.uantwerpen.minelabs.util.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BurnerBlock extends CosmeticBlock {

    private static final BooleanProperty LIT = Properties.LIT;
    private static final BooleanProperty OXYGENATED = MinelabsProperties.OXYGENATED;
    private static final ParticleEffect RED_FIRE = ParticleTypes.FLAME;
    private static final ParticleEffect BLUE_FIRE = ParticleTypes.SOUL_FIRE_FLAME;

    public BurnerBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(LIT, false).with(OXYGENATED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(OXYGENATED);
        builder.add(LIT);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(LIT, false).with(OXYGENATED, false);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        return VoxelShapes.cuboid(0.250f, 0f-getYOffset(state), 0.250f, 0.75f, 0.625f-getYOffset(state), 0.75f);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).isEmpty()) {
            world.setBlockState(pos, state.cycle(OXYGENATED));
            return ActionResult.SUCCESS;
        } else {
            boolean lit = false;
            if (!state.get(LIT)){
                if (player.getStackInHand(hand).getItem() == Items.FLINT_AND_STEEL){
                    lit = true;
                    player.getStackInHand(hand).damage(1, player, p -> p.sendToolBreakStatus(hand));
                } else if (player.getStackInHand(hand).getItem() == Items.FIRE_CHARGE) {
                    lit = true;
                    player.getStackInHand(hand).decrement(1);
                }
                if (lit){
                    world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.4f + 0.8f);
                    world.setBlockState(pos, state.cycle(LIT));
                    return ActionResult.success(world.isClient);
                }
            } else {
                if (player.getStackInHand(hand).isIn(Tags.Items.FIRE_EXTINGUISH)){
                    world.setBlockState(pos, state.cycle(LIT));
                    world.playSound(player, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 2.0f, 1.0f);
                    return ActionResult.success(world.isClient);
                }
            }
            return ActionResult.FAIL;
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        //TODO CHANGE TO EVERY TICK ?
        double x = (double) pos.getX() + 0.5;
        double y = (double) pos.getY() + 0.7 - getYOffset(state);
        double z = (double) pos.getZ() + 0.5;

        if (state.get(LIT)) {
            spawnParticles(world, state.get(OXYGENATED) ? BLUE_FIRE : RED_FIRE, x, y, z);
            if (!state.get(OXYGENATED)) {
                world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.1, 0.0);
            }
        }
    }

    private void spawnParticles(World world, ParticleEffect effect, double x, double y, double z){
        world.addParticle(effect,true, x, y, z, 0.0, 0.015, 0.0);
    }
}
