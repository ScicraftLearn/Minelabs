package be.minelabs.client.integration.rei.displays;

import be.minelabs.Minelabs;
import be.minelabs.client.integration.rei.categories.CoulombInfoCategory;
import be.minelabs.science.coulomb.CoulombData;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CoulombInfoDisplay extends BasicDisplay {

    public CoulombInfoDisplay(CoulombData data, Identifier id) {
        super(new ArrayList<>(), null);
        String[] split = id.getPath().split("/");
        String itemName = split[split.length - 1].split("\\.")[0];

        Item item = Registries.ITEM.get(new Identifier(Minelabs.MOD_ID, "subatomic/" + itemName));
        inputs.add(EntryIngredients.of(item));
    }

    public CoulombInfoDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CoulombInfoCategory.COULOMB_INFO;
    }
}
