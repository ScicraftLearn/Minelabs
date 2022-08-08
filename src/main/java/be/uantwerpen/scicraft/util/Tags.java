package be.uantwerpen.scicraft.util;

import be.uantwerpen.scicraft.Scicraft;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Use these tags for NEW tags.
 * Using old tags just add a json in our mod:
 * "data/minecraft/blocks/", "data/minecraft/items/", "data/minecraft/fluids/"
 *
 * @author pixar02
 */
public class Tags {
    public static class Blocks {

        public static final TagKey<Block> COPPER_BLOCKS = createTag("copper_blocks");
        public static final TagKey<Block> CHARGED_BLOCKS = createTag("charged_blocks");

        /**
         * Create a Block tag (tag is only used inside this mod)
         * Don't forget the json file (data/scicraft/tags/blocks)
         *
         * @param name : name of the tag
         * @return {@link TagKey}
         */
        private static TagKey<Block> createTag(String name) {
            return TagKey.of(Registry.BLOCK_KEY, new Identifier(Scicraft.MOD_ID, name));
        }

        /**
         * Create a Block tag (tag for usage outside this mod)
         * Don't forget the json file (data/c/tags/blocks)
         *
         * @param name : name of the tag
         * @return {@link TagKey}
         */
        private static TagKey<Block> createCommonTag(String name) {
            return TagKey.of(Registry.BLOCK_KEY, new Identifier("c", name));
        }
    }

    public static class Items {

        /**
         * Create an Item tag (tag is only used inside this mod)
         * Don't forget the json file (data/scicraft/tags/items)
         *
         * @param name : name of the tag
         * @return {@link TagKey}
         */
        private static TagKey<Item> createTag(String name) {
            return TagKey.of(Registry.ITEM_KEY, new Identifier("c", name));
        }

        /**
         * Create an Item tag (tag for usage outside this mod)
         * Don't forget the json file (data/c/tags/items)
         *
         * @param name : name of the tag
         * @return {@link TagKey}
         */
        private static TagKey<Item> createCommonTag(String name) {
            return TagKey.of(Registry.ITEM_KEY, new Identifier("c", name));
        }
    }

    public static class Fluids {

        /**
         * Create a Fluid tag (tag is only used inside this mod)
         * Don't forget the json file (data/scicraft/tags/fluids)
         *
         * @param name : name of the tag
         * @return {@link TagKey}
         */
        private static TagKey<Fluid> createTag(String name) {
            return TagKey.of(Registry.FLUID_KEY, new Identifier("c", name));
        }

        /**
         * Create a Fluid tag (tag for usage outside this mod)
         * Don't forget the json file (data/c/tags/fluids)
         *
         * @param name : name of the tag
         * @return {@link TagKey}
         */
        private static TagKey<Fluid> createCommonTag(String name) {
            return TagKey.of(Registry.FLUID_KEY, new Identifier("c", name));
        }
    }
}
