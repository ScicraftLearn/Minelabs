package be.uantwerpen.scicraft.potion;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.item.MoleculeItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.collection.DefaultedList;

public class GasPotion extends LingeringPotionItem implements MoleculeItem {
    private final String molecule;
    public GasPotion(Settings settings, String molecule) {
        super(settings);
        this.molecule = molecule;
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
    public String getMolecule() {
        return molecule;
    }
}
