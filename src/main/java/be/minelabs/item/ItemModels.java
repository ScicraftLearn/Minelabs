package be.minelabs.item;

import be.minelabs.Minelabs;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ItemModels {

    /**
     * Register Atoms to Model Provider Registry ({@link ModelPredicateProviderRegistry})<br>
     * Returns the {@link Item} provided by {@code register(Item, String)}
     *
     * @param item:       Item Object to register
     */
    private static void registerAtom(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("lct"),
                (stack, world, entity, seed) -> {
                    NbtCompound nbt = stack.getNbt();
                    if (nbt != null && nbt.contains("MinelabsItemInLCT")){
                        return nbt.getBoolean("MinelabsItemInLCT") ? 1.0F : 0.0F;
                    }
                    return 0.0f;
                });
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
    private static void registerValence(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("n"),
                (stack, world, entity, seed) -> ((float) stack.getOrCreateNbt().getInt("n"))/2f );
        ModelPredicateProviderRegistry.register(item, new Identifier("e"),
                (stack, world, entity, seed) -> ((float) stack.getOrCreateNbt().getInt("e"))/2f );
        ModelPredicateProviderRegistry.register(item, new Identifier("s"),
                (stack, world, entity, seed) -> ((float) stack.getOrCreateNbt().getInt("s"))/2f );
        ModelPredicateProviderRegistry.register(item, new Identifier("w"),
                (stack, world, entity, seed) -> ((float) stack.getOrCreateNbt().getInt("w"))/2f );
    }
    /**
     * Main class method<br>
     * Registers all ItemModels
     */
    public static void registerModels() {
        Minelabs.LOGGER.info("registering itemmodels");

        Items.ATOMS.forEach(ItemModels::registerAtom);

        registerBond(Items.BOND);
        registerValence(Items.VALENCEE);
    }
}
