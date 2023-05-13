package be.minelabs.item;

import net.minecraft.item.Item;

public class FireReactionItem extends Item implements IFireReaction, IMoleculeItem {

    private final int FIRE_COLOR;
    private final String molecule;

    /**
     * @param settings   : Item settings
     * @param fire_color : 0-10, 0: default color,
     *                   1 : Carmine,
     *                   2 : Red,
     *                   3 : Orange,
     *                   4 : Yellow,
     *                   5 : Lime ( yellow-green ),
     *                   6 : Green,
     *                   7 : Blue (Soul fire),
     *                   8 : Violet,
     *                   9 : Purple,
     *                   10: White,
     */
    public FireReactionItem(Settings settings, int fire_color, String molecule) {
        super(settings);
        this.FIRE_COLOR = fire_color;
        this.molecule = molecule;
    }

    public int getFireColor() {
        return FIRE_COLOR;
    }

    @Override
    public String getMolecule() {
        return this.molecule;
    }
}
