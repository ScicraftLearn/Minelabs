package be.uantwerpen.scicraft.item;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SaltItem extends BlockItem implements IFireReaction {

    private final int FIRE_COLOR;

    public SaltItem(Block block, Settings settings, int fire_color) {
        super(block, settings);
        FIRE_COLOR = fire_color;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("NaCl"));
        super.appendTooltip(stack, world, tooltip, context);
    }

    public int getFireColor() {
        return FIRE_COLOR;
    }
}

