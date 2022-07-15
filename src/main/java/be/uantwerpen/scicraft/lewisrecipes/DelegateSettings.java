package be.uantwerpen.scicraft.lewisrecipes;

import be.uantwerpen.scicraft.item.Items;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.item.Item;

import java.util.Map;

public class DelegateSettings {
    public static final int LCT_TEXTURE_ID = 0;
    public static final int LCT_CRAFTING_PROGRESS = 1;
    public static final int LCT_SLOT_ITEMS = 2;
    public static final int LCT_SLOT_READY = 3;

    public static final BiMap<Item, Integer> ATOM_MAPPINGS = HashBiMap.create(Map.of(
            Items.HYDROGEN_ATOM, 2,
            Items.OXYGEN_ATOM, 3,
            Items.CARBON_ATOM, 5,
            Items.NITROGEN_ATOM, 7,
            Items.SODIUM_ATOM, 11,
            Items.CHLORINE_ATOM, 13
    ));

    public static final BiMap<Integer, Integer> SLOT_MAPPINGS = HashBiMap.create(Map.of(
            0, 2,
            1, 3,
            2, 5,
            3, 7,
            4, 11,
            5, 13,
            6, 17,
            7, 19,
            8, 23
    ));
}
