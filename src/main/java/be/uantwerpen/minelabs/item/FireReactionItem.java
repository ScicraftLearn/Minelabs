package be.uantwerpen.minelabs.item;

import net.minecraft.item.Item;

public class FireReactionItem extends Item implements IFireReaction {

    private final int FIRE_COLOR;

    /**
     * @param settings   : Item settings
     * @param fire_color : 0-10, 0: default color,
     *                   1 : Carmine,
     *                   2 : Red,
     *                   3 : Orange,
     *                   4 : Yellow,
     *                   5 : Yellow-Green (? lime ?),
     *                   6 : Green,
     *                   7 : Blue (Soul fire),
     *                   8 : Violet,
     *                   9 : Purple,
     *                   10: White,
     */
    public FireReactionItem(Settings settings, int fire_color) {
        super(settings);
        FIRE_COLOR = fire_color;
    }

    public int getFireColor() {
        return FIRE_COLOR;
    }
}
