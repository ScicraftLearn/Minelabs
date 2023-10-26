package be.minelabs.client.integration.rei.categories;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.client.integration.rei.displays.IonicDisplay;
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

public class IonicCategory implements DisplayCategory<BasicDisplay> {
    private static final Identifier TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/gui/ionic_gui.png");

    public static final CategoryIdentifier<IonicDisplay> IONIC_CRAFTING =
            CategoryIdentifier.of(Minelabs.MOD_ID, "ionic_crafting");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return IONIC_CRAFTING;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("Ion Crafting");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.IONIC_BLOCK.asItem().getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        // TODO
        return DisplayCategory.super.setupDisplay(display, bounds);
    }

    @Override
    public int getDisplayHeight() {
        return 90;
    }
}
