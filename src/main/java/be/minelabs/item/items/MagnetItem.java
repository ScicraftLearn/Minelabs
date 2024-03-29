package be.minelabs.item.items;

import be.minelabs.util.Tags;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagnetItem extends AbstractMagnet {
    // COULD HAVE A DIFFERENT SPEED / RANGE

    public MagnetItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("text.minelabs.toggle_instruction"));
    }

    @Override
    protected boolean isAttractable(Entity entity) {
        Iterable<ItemStack> armor = entity.getArmorItems();
        for (ItemStack stack: armor) {
            if (stack.isIn(Tags.Items.MAGNET_WHITELIST)){
                return true;
            }
        }
        if (entity instanceof ItemEntity itemEntity){
            return itemEntity.getStack().isIn(Tags.Items.MAGNET_WHITELIST);
        }
        return super.isAttractable(entity);
    }
}
