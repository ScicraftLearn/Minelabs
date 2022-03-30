package be.uantwerpen.scicraft.potion;

import be.uantwerpen.scicraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;

public class GasPotion extends LingeringPotionItem {
    public GasPotion(Settings settings) {
        super(settings);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        Potion potion = new Potion("Test");
        stacks.add(PotionUtil.setPotion(new ItemStack(this), potion));
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
}