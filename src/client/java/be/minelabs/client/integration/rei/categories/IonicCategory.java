package be.minelabs.client.integration.rei.categories;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.client.integration.rei.displays.IonicDisplay;
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

public class IonicCategory implements DisplayCategory<BasicDisplay> {
    private static final Identifier TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/gui/ionic/ionic_gui_rei.png");

    public static final CategoryIdentifier<IonicDisplay> IONIC_CRAFTING =
            CategoryIdentifier.of(Minelabs.MOD_ID, "ionic_crafting");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return IONIC_CRAFTING;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("rei.category.minelabs.ionic_crafting");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.IONIC_BLOCK.asItem().getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getCenterX() - 105, bounds.getCenterY() - 45);
        List<Widget> widgets = new LinkedList<>();
        widgets.add(Widgets.createTexturedWidget(TEXTURE,
                new Rectangle(startPoint.x, startPoint.y, 206, 111)));
        // TODO ingredients

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 176, startPoint.y + 40)).disableBackground()
                .entries(display.getOutputEntries().get(0)));
        IonicDisplay disp = (IonicDisplay) display;
        if (!disp.getContainer().isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 178, startPoint.y + 86)).disableBackground()
                    .entries(disp.getContainer()));
        }

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 120;
    }

    @Override
    public int getDisplayWidth(BasicDisplay display) {
        return 200;
    }
}
