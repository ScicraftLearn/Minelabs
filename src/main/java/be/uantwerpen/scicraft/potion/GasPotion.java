package be.uantwerpen.scicraft.potion;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.effects.SplashColor;
import be.uantwerpen.scicraft.item.ItemGroups;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Iterator;

public class GasPotion extends LingeringPotionItem {
    public GasPotion(Settings settings) {
        super(settings);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if(group == ItemGroups.CHEMICALS) {
            stacks.add(PotionUtil.setPotion(new ItemStack(this), be.uantwerpen.scicraft.potion.Potions.N2));
        }
    }
    static final BlockItem erlenmeyer_block = new BlockItem(Blocks.ERLENMEYER_STAND, new Item.Settings());
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer().isSneaking()) {
            return erlenmeyer_block.place(new ItemPlacementContext(context));
        }
        else {
            TypedActionResult<ItemStack> actionResult = this.use(context.getWorld(), context.getPlayer(), context.getHand());
            return actionResult.getResult();
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return PotionUtil.getPotion(stack).finishTranslationKey(this.getTranslationKey() + ".chemical.");
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        PotionUtil.setCustomPotionEffects(itemStack, Collections.singleton(new StatusEffectInstance(new SplashColor(2993342))));
        if (!world.isClient) {
            PotionEntity potionEntity = new PotionEntity(world, user);
            potionEntity.setItem(itemStack);
            potionEntity.setVelocity(user, user.getPitch(), user.getYaw(), -20.0F, 1F, 0.6F);
            world.spawnEntity(potionEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}