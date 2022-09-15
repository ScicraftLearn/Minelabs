package be.uantwerpen.minelabs.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class LabSinkBlock extends LabBlock {

    public static BooleanProperty FILLED = BooleanProperty.of("filled");

    public LabSinkBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(FILLED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FILLED);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient && hand == Hand.MAIN_HAND) {
            ItemStack stack = player.getStackInHand(hand);
            if (state.get(FILLED)) {
                //FILLED
                if (stack.isOf(Items.BUCKET)) {
                    // remove a bucket and give Water bucket
                    // TODO WHY PLACE WATER??
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.WATER_BUCKET)));
                    world.setBlockState(pos, state.cycle(FILLED));
                    world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
                    return ActionResult.FAIL;
                } else if (PotionUtil.getPotion(stack) == Potions.EMPTY) {
                    // fill the bottle
                    // SINK DOES NOT NEED REFILL
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
                    return ActionResult.SUCCESS;
                }
            } else {
                //NOT FILLED
                if (stack.isOf(Items.WATER_BUCKET)) {
                    //set water bucket to empty bucket
                    // TODO WHY PLACE WATER??
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
                    world.setBlockState(pos, state.cycle(FILLED));
                    world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                    return ActionResult.FAIL;
                } else if (PotionUtil.getPotion(stack) == Potions.WATER) {
                    //EMPTY THE BOTTLE
                    //set stack to bottle

                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        }
        return ActionResult.PASS;
    }
}
