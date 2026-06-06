package be.minelabs.item.items;

import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.advancement.criterion.ErlenmeyerCriterion;
import be.minelabs.entity.projectile.thrown.GasPotionEntity;
import be.minelabs.item.IMoleculeItem;
import be.minelabs.item.Items;
import be.minelabs.science.Molecule;
import be.minelabs.world.MinelabsGameRules;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GasPotion extends LingeringPotionItem implements IMoleculeItem {
    private final Molecule molecule;

    public GasPotion(Settings settings, Molecule molecule) {
        super(settings);
        this.molecule = molecule;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        molecule.makeTooltip(tooltip);
    }

    @Override
    public ItemStack getDefaultStack() {
        return new ItemStack(this);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.getGameRules().getBoolean(MinelabsGameRules.ALLOW_CHEMICAL_PROJECTILES)) {
            user.sendMessage(Text.translatable("text.minelabs.no_chemical_projectiles"));
            return TypedActionResult.fail(itemStack);
        }

        if (!world.isClient) {
            GasPotionEntity gasEntity = new GasPotionEntity(world, user, this.molecule);
            gasEntity.setItem(itemStack);
            gasEntity.setVelocity(user, user.getPitch(), user.getYaw(), -20.0F, 0.5F, 1.0F);
            world.spawnEntity(gasEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        // Advancement
        if (user instanceof ServerPlayerEntity serverPlayer) {
            Criteria.ERLENMEYER_CRITERION.trigger(serverPlayer, ErlenmeyerCriterion.Type.THROW);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer().isSneaking()) {
            return Items.ERLENMEYER_STAND.useOnBlock(context);
        } else {
            ActionResult result = super.useOnBlock(context);
            return result;
        }

    }

    @Override
    public String getMolecule() {
        return molecule.toString();
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return Util.createTranslationKey("item", Registries.ITEM.getId(this));
    }
}
