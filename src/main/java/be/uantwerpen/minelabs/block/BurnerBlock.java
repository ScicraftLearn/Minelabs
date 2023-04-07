package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.util.MinelabsProperties;
import be.uantwerpen.minelabs.util.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BurnerBlock extends Block {

    private static final BooleanProperty LIT = Properties.LIT;
    private static final BooleanProperty OXYGENATED = MinelabsProperties.OXYGENATED;
    private static final IntProperty COUNTER = MinelabsProperties.COUNTER;
    private static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    private final ParticleEffect RED_FIRE = ParticleTypes.FLAME;
    private final ParticleEffect BLUE_FIRE = ParticleTypes.SOUL_FIRE_FLAME;

    public BurnerBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(COUNTER);
        builder.add(OXYGENATED);
        builder.add(LIT);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        int base = 0;
        if (ctx.getWorld().getBlockState(ctx.getBlockPos().down()).getBlock() instanceof LabBlock) {
            base = 2;
        } else if (ctx.getWorld().getBlockState(ctx.getBlockPos().down()).getBlock() instanceof LabCenterBlock) {
            base = 1;
        }
        return this.getDefaultState()
                .with(FACING, ctx.getPlayerFacing().getOpposite())
                .with(COUNTER, base)
                .with(LIT, false)
                .with(OXYGENATED, false);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).isEmpty()) {
            world.setBlockState(pos, state.cycle(OXYGENATED));
            return ActionResult.SUCCESS;
        } else {
            if (player.getStackInHand(hand).getItem() == Items.FLINT_AND_STEEL && !state.get(LIT)) {
                world.setBlockState(pos, state.cycle(LIT));
                return ActionResult.SUCCESS;
            }
            if (player.getStackInHand(hand).isIn(Tags.Items.FIRE_EXTINGUISH) && state.get(LIT)) {
                //TODO BETTER SOLUTION ?
                world.setBlockState(pos, state.cycle(LIT));
                world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        }
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double x = (double) pos.getX() + 0.5;
        double y = (double) pos.getY() + 0.7;
        double z = (double) pos.getZ() + 0.5;

        if (state.get(LIT)) {
            if (state.get(OXYGENATED)) {
                world.addParticle(this.BLUE_FIRE, x, y, z, 0.0, 0.0, 0.0);
            } else {
                world.addParticle(this.RED_FIRE, x, y, z, 0.0, 0.0, 0.0);
                world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
            }
        }
    }
}
