package be.minelabs.client.integration.rei.categories;

import be.minelabs.Minelabs;
import be.minelabs.client.integration.rei.displays.CoulombInfoDisplay;
import be.minelabs.item.Items;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class CoulombInfoCategory implements DisplayCategory<BasicDisplay> {

    public static final CategoryIdentifier<CoulombInfoDisplay> COULOMB_INFO =
            CategoryIdentifier.of(Minelabs.MOD_ID, "coulomb_info");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return COULOMB_INFO;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("rei.category.minelabs.coulomb_info");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Items.ELECTRON.getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        return widgets;
    }

    @Override
    public int getMaximumDisplaysPerPage() {
        return 1;
    }
}
