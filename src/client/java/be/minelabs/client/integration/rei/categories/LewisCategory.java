package be.minelabs.client.integration.rei.categories;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.client.integration.rei.displays.LewisDisplay;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class LewisCategory implements DisplayCategory<BasicDisplay> {

    private static final Identifier TEXTURE = new Identifier(Minelabs.MOD_ID,
            "textures/gui/lewis_block/lewis_block_rei.png");

    public static final CategoryIdentifier<LewisDisplay> MOLECULE_CRAFTING =
            CategoryIdentifier.of(Minelabs.MOD_ID, "molecule_crafting");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return MOLECULE_CRAFTING;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("integration.rei.molecule_crafting");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.LEWIS_BLOCK.asItem().getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        // TODO https://www.youtube.com/watch?v=HbZ6ocABo-M minute 7...

        final Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 60);
        List<Widget> widgets = new LinkedList<>();

        widgets.add(Widgets.createTexturedWidget(TEXTURE,
                new Rectangle(startPoint.x, startPoint.y, 176, 108)));

        for (int m = 0; m < 5; ++m) {
            for (int l = 0; l < 5; ++l) {
                int index = l + m * 5;
                if (index < display.getInputEntries().size() - 1) {
                    widgets.add(Widgets.createSlot(
                                    new Point(startPoint.x + 8 + l * 18, startPoint.y + m * 18 + 9))
                            .entries(display.getInputEntries().get(index)));
                } else {
                    widgets.add(Widgets.createSlot(
                            new Point(startPoint.x + 8 + l * 18, startPoint.y + m * 18 + 9)));
                }
            }
        }

        widgets.add(Widgets.createSlot(
                        new Point(startPoint.x + 134, startPoint.y + 46)).disableBackground()
                .entries(display.getOutputEntries().get(0)));

        LewisDisplay disp = (LewisDisplay) display;
        if (!disp.getContainer().isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 134, startPoint.y + 82)).disableBackground()
                    .entries(disp.getContainer()));
        }

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        // TODO check
        return 130;
    }
}
