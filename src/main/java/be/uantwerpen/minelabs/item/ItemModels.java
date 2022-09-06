package be.uantwerpen.minelabs.item;

import be.uantwerpen.minelabs.Minelabs;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ItemModels {

    static {
        registerAtom(Items.HYDROGEN_ATOM);
        registerAtom(Items.HELIUM_ATOM);

        registerAtom(Items.LITHIUM_ATOM);
        registerAtom(Items.BERYLLIUM_ATOM);
        registerAtom(Items.BORON_ATOM);
        registerAtom(Items.CARBON_ATOM);
        registerAtom(Items.NITROGEN_ATOM);
        registerAtom(Items.OXYGEN_ATOM);
        registerAtom(Items.FLUORINE_ATOM);
        registerAtom(Items.NEON_ATOM);

        registerAtom(Items.SODIUM_ATOM);
        registerAtom(Items.MAGNESIUM_ATOM);
        registerAtom(Items.ALUMINIUM_ATOM);
        registerAtom(Items.SILICON_ATOM);
        registerAtom(Items.PHOSPHORUS_ATOM);
        registerAtom(Items.SULFUR_ATOM);
        registerAtom(Items.CHLORINE_ATOM);
        registerAtom(Items.ARGON_ATOM);

        registerAtom(Items.POTASSIUM_ATOM);
        registerAtom(Items.CALCIUM_ATOM);
        registerAtom(Items.IRON_ATOM);
        registerAtom(Items.COPPER_ATOM);
        registerAtom(Items.ZINC_ATOM);
        registerAtom(Items.BROMINE_ATOM);

        registerAtom(Items.SILVER_ATOM);
        registerAtom(Items.CADMIUM_ATOM);
        registerAtom(Items.TIN_ATOM);
        registerAtom(Items.IODINE_ATOM);

        registerAtom(Items.GOLD_ATOM);
        registerAtom(Items.MERCURY_ATOM);
        registerAtom(Items.LEAD_ATOM);
        registerAtom(Items.URANIUM_ATOM);

        registerBond(Items.BOND);
    }

    /**
     * Register Atoms to Model Provider Registry ({@link ModelPredicateProviderRegistry})<br>
     * Returns the {@link Item} provided by {@code register(Item, String)}
     *
     * @param item:       Item Object to register
     */
    private static void registerAtom(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("lct"),
                (stack, world, entity, seed) -> stack.getOrCreateNbt().getBoolean("MinelabsItemInLCT") ? 1.0F : 0.0F);
    }

    /**
     * Register Atoms to Model Provider Registry ({@link ModelPredicateProviderRegistry})<br>
     * Returns the {@link Item} provided by {@code register(Item, String)}
     *
     * @param item :       Item Object to register
     */
    private static void registerBond(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("bonds"),
                (stack, world, entity, seed) -> ((float) stack.getOrCreateNbt().getInt("MinelabsBondAmount")) / 10F);
        ModelPredicateProviderRegistry.register(item, new Identifier("direction"),
                (stack, world, entity, seed) -> stack.getOrCreateNbt().getBoolean("MinelabsBondDirection") ? 1.0F : 0.0F);
    }

    /**
     * Main class method<br>
     * Registers all ItemModels
     */
    public static void registerModels() {
        Minelabs.LOGGER.info("registering itemmodels");
    }
}
