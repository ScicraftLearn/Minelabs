package be.uantwerpen.scicraft.potion;

import net.minecraft.item.ItemStack;
import net.minecraft.item.LingeringPotionItem;

public class GasPotion extends LingeringPotionItem {
    public GasPotion(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().putString("Potion", "minecraft:empty");
        return stack;
    }
}
