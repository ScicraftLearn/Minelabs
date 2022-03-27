package be.uantwerpen.scicraft.potion;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import be.uantwerpen.scicraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class GasPotion extends LingeringPotionItem {
    public GasPotion(Settings settings) {
        super(settings);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        Potion potion = new Potion("Test");
        stacks.add(PotionUtil.setPotion(new ItemStack(this), potion));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if (context.getPlayer().isSneaking()) {
            //ActionResult actionResult = this.place(new ItemPlacementContext(context));
            // create BlockItem class object to use its functions? instead of copy-pasting
            BlockItem temp_stand = new BlockItem(Blocks.ERLENMEYER_STAND, new Item.Settings());
            ActionResult actionResult = temp_stand.place(new ItemPlacementContext(context));
            return actionResult;
        }
        else {
            TypedActionResult<ItemStack> actionResult = this.use(context.getWorld(), context.getPlayer(), context.getHand());
            return actionResult.getResult();
        }
    }
/*
    public ActionResult place(ItemPlacementContext context) {
        if (!context.canPlace()) {
            return ActionResult.FAIL;
        } else {
            //ItemPlacementContext itemPlacementContext = this.getPlacementContext(context); //?
            if (context == null) {
                return ActionResult.FAIL;
            } else {
                BlockState blockState = this.getPlacementState(context);
                if (blockState == null) {
                    return ActionResult.FAIL;
                } else if (!this.place_(context, blockState)) {
                    return ActionResult.FAIL;
                } else {
                    BlockPos blockPos = context.getBlockPos();
                    World world = context.getWorld();
                    PlayerEntity playerEntity = context.getPlayer();
                    ItemStack itemStack = context.getStack();
                    BlockState blockState2 = world.getBlockState(blockPos);
                    if (blockState2.isOf(blockState.getBlock())) {
                        blockState2 = this.placeFromTag(blockPos, world, itemStack, blockState2);
                        //this.postPlacement(blockPos, world, playerEntity, itemStack, blockState2);
                        blockState2.getBlock().onPlaced(world, blockPos, blockState2, playerEntity, itemStack);
                        if (playerEntity instanceof ServerPlayerEntity) {
                            Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
                        }
                    }

                    BlockSoundGroup blockSoundGroup = blockState2.getSoundGroup();
                    world.playSound(playerEntity, blockPos, this.getPlaceSound(blockState2), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
                    world.emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
                    if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }

                    return ActionResult.success(world.isClient);
                }
            }
        }

    }

    protected boolean place_(ItemPlacementContext context, BlockState state) {
        return context.getWorld().setBlockState(context.getBlockPos(), state, 11);
    }
    @Nullable
    protected BlockState getPlacementState(ItemPlacementContext context) {
        BlockState blockState = Blocks.ERLENMEYER_STAND.getPlacementState(context);
        return blockState != null ? blockState : null; //&& this.canPlace(context, blockState) ? blockState : null;
    }
    private BlockState placeFromTag(BlockPos pos, World world, ItemStack stack, BlockState state) {
        BlockState blockState = state;
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound != null) {
            NbtCompound nbtCompound2 = nbtCompound.getCompound("BlockStateTag");
            StateManager<Block, BlockState> stateManager = state.getBlock().getStateManager();
            Iterator var9 = nbtCompound2.getKeys().iterator();

            while(var9.hasNext()) {
                String string = (String)var9.next();
                Property<?> property = stateManager.getProperty(string);
                if (property != null) {
                    String string2 = nbtCompound2.get(string).asString();
                    blockState = with(blockState, property, string2);
                }
            }
        }

        if (blockState != state) {
            world.setBlockState(pos, blockState, 2);
        }

        return blockState;
    }
    private static <T extends Comparable<T>> BlockState with(BlockState state, Property<T> property, String name) {
        return (BlockState)property.parse(name).map((value) -> {
            return (BlockState)state.with(property, value);
        }).orElse(state);
    }
    protected SoundEvent getPlaceSound(BlockState state) {
        return state.getSoundGroup().getPlaceSound();
    }

 */
}