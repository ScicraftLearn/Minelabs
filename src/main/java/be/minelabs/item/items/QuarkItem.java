package be.minelabs.item.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QuarkItem extends Item {

    public QuarkItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        // color RED/GREEN/BLUE OR ANTI-RED/ANTI-GREEN/ANTI-BLUE
        // electric: up +2/3 , down -1/3 , anti-up: -2/3 , anti-down: +1/3
        String[] key = getTranslationKey().split("_");
        boolean anti_particle = key[0].contains("anti");


        tooltip.add(Text.translatable(anti_particle ? "text.minelabs.anti_quark.color." + key[key.length - 1]
                : "text.minelabs.quark.color." + key[key.length - 1]));

        if (anti_particle) {
            // ANTI PARTICLE
            if (key[1].contains("up")) {
                tooltip.add(Text.translatable("text.minelabs.anti_quark.electric_up"));
            } else {
                tooltip.add(Text.translatable("text.minelabs.anti_quark.electric_down"));
            }
        } else {
            // NOT ANTI PARTICLE
            if (key[0].contains("up")) {
                tooltip.add(Text.translatable("text.minelabs.quark.electric_up"));
            } else {
                tooltip.add(Text.translatable("text.minelabs.quark.electric_down"));
            }
        }
    }
}
