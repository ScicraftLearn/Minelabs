package be.minelabs.client.item;

import be.minelabs.block.Blocks;
import be.minelabs.item.Items;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ItemModels {
    public static void onInitializeClient() {
        Erlenmeyers.onInitializeClient();

        Items.ATOMS.forEach(ItemModels::registerAtom);

        registerBond(Items.BOND);
        registerValence(Items.VALENCEE);

        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> 0x3495eb, Blocks.LAB_SINK);
    }

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
                (stack, world, entity, seed) -> {
                    NbtCompound nbt = stack.getNbt();
                    if (nbt != null && nbt.contains("MinelabsBondAmount")){
                        return ((float) nbt.getInt("MinelabsBondAmount")) / 10F;
                    }
                    return 0.0f;
                });
        ModelPredicateProviderRegistry.register(item, new Identifier("direction"),
                (stack, world, entity, seed) -> {
                    NbtCompound nbt = stack.getNbt();
                    if (nbt != null && nbt.contains("MinelabsBondDirection")){
                        return nbt.getBoolean("MinelabsBondDirection") ? 1.0F : 0.0F;
                    }
                    return 0.0f;
                });
    }
    private static void registerValence(Item item) {
        String[] directions = {"n", "e", "s", "w"};
        for (String direction : directions) {
        ModelPredicateProviderRegistry.register(item, new Identifier(direction),
                (stack, world, entity, seed) -> {
                    NbtCompound nbt = stack.getNbt();
                    if (nbt != null && nbt.contains(direction)){
                        return ((float) nbt.getInt(direction))/2F;
                    }
                    return 0.0f;
                });
        }
    }
}
