package be.uantwerpen.scicraft.util;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class Tags {
    public static class Blocks {

        public static final Tag<Block> COPPER_BLOCKS = createTag("copper_blocks");

        /**
         * Create a Block tag (tag is only used inside this mod)
         * Don't forget the json file (data/scicraft/blocks)
         *
         * @param name : name of the tag
         * @return {@link Tag}
         */
        private static Tag<Block> createTag(String name) {
            return TagFactory.BLOCK.create(new Identifier(Scicraft.MOD_ID, name));
        }

        /**
         * Create a Block tag (tag for usage outside this mod)
         * Don't forget the json file (data/c/blocks)
         *
         * @param name : name of the tag
         * @return {@link Tag}
         */
        private static Tag<Block> createCommonTag(String name) {
            return TagFactory.BLOCK.create(new Identifier("c", name));
        }
    }

    public static class Items {

        public static final Tag<Item> COPPER_BLOCKS = createTag("copper_blocks");

        /**
         * Create an Item tag (tag is only used inside this mod)
         * Don't forget the json file (data/scicraft/items)
         *
         * @param name : name of the tag
         * @return {@link Tag}
         */
        private static Tag<Item> createTag(String name) {
            return TagFactory.ITEM.create(new Identifier(Scicraft.MOD_ID, name));
        }

        /**
         * Create an Item tag (tag for usage outside this mod)
         * Don't forget the json file (data/c/items)
         *
         * @param name : name of the tag
         * @return {@link Tag}
         */
        private static Tag<Item> createCommonTag(String name) {
            return TagFactory.ITEM.create(new Identifier("c", name));
        }
    }

    public static class Fluids {

        /**
         * Create a Fluid tag (tag is only used inside this mod)
         * Don't forget the json file (data/scicraft/items)
         *
         * @param name : name of the tag
         * @return {@link Tag}
         */
        private static Tag<Fluid> createTag(String name) {
            return TagFactory.FLUID.create(new Identifier(Scicraft.MOD_ID, name));
        }

        /**
         * Create a Fluid tag (tag for usage outside this mod)
         * Don't forget the json file (data/c/items)
         *
         * @param name : name of the tag
         * @return {@link Tag}
         */
        private static Tag<Fluid> createCommonTag(String name) {
            return TagFactory.FLUID.create(new Identifier("c", name));
        }
    }
}
