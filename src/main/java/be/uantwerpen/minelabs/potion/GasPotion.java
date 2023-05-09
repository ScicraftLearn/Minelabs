package be.uantwerpen.minelabs.potion;

import be.uantwerpen.minelabs.advancement.criterion.Criteria;
import be.uantwerpen.minelabs.advancement.criterion.ErlenmeyerCriterion;
import be.uantwerpen.minelabs.item.IMoleculeItem;
import be.uantwerpen.minelabs.item.Items;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GasPotion extends LingeringPotionItem implements IMoleculeItem {
    private final Molecule molecule;

    public GasPotion(Settings settings, Molecule molecule) {
        super(settings);
        this.molecule = molecule;
    }

   /*TODO : NO LONGER NEEDED ?
    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        Potion potion = new Potion("Test");
        if (this.isIn(group)) {
            ItemStack item = PotionUtil.setPotion(new ItemStack(this), potion);
            item.setNbt(null);
            stacks.add(item);
        }
    }*/

    @Override
    public ItemStack getDefaultStack() {
        return new ItemStack(this);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            GasEntity gasEntity = new GasEntity(world, user, this.molecule);
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
}
