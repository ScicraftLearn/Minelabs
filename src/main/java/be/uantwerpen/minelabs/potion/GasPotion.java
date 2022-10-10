package be.uantwerpen.minelabs.potion;

import be.uantwerpen.minelabs.item.Items;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.collection.DefaultedList;

public class GasPotion extends LingeringPotionItem {
    public GasPotion(Settings settings) {
        super(settings);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        Potion potion = new Potion("Test");
        if (this.isIn(group))
            stacks.add(PotionUtil.setPotion(new ItemStack(this), potion));
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().putString("Potion", "minecraft:empty");
        return stack;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer().isSneaking()) {
            return Items.ERLENMEYER_STAND.useOnBlock(context);
        } else {
            return super.useOnBlock(context);
        }

    }
}
