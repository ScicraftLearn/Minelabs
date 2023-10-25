package be.minelabs.client.integration.rei.categories;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.client.integration.rei.displays.LewisDisplay;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class LewisCategory implements DisplayCategory<BasicDisplay> {

    private static final Identifier TEXTURE = new Identifier(Minelabs.MOD_ID,
            "textures/gui/lewis_block_inventory.png");

    public static final CategoryIdentifier<LewisDisplay> MOLECULE_CRAFTING =
            CategoryIdentifier.of(Minelabs.MOD_ID, "molecule_crafting");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return MOLECULE_CRAFTING;
    }

    @Override
    public Text getTitle() {
        // TODO TRANSLATE
        return Text.literal("Molecule Crafting");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.LEWIS_BLOCK.asItem().getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        // TODO https://www.youtube.com/watch?v=HbZ6ocABo-M minute 7...
        return DisplayCategory.super.setupDisplay(display, bounds);
    }

    @Override
    public int getDisplayHeight() {
        // TODO check
        return 160;
    }
}
