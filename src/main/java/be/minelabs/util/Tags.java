package be.minelabs.util;

import be.minelabs.Minelabs;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

/**
 * Use these tags for NEW tags.
 * Using old tags just add a json in our mod:
 * "data/minecraft/blocks/", "data/minecraft/items/", "data/minecraft/fluids/"
 *
 * @author pixar02
 */
public class Tags {
    public static class Blocks {
        public static final TagKey<Block> QUANTUMFIELDS = createTag("quantumfields");
        public static final TagKey<Block> LASERTOOL_MINEABLE = createTag("lasertool_mineable");
        public static final TagKey<Block> COPPER_BLOCKS = createTag("copper_blocks");
        public static final TagKey<Block> CHARGED_BLOCKS = createTag("charged_blocks");
        public static final TagKey<Block> ENTROPY_IMMUNE = createTag("entropy_immune");
        public static final TagKey<Block> REACTION_DEFAULT_BLACKLIST = createTag("reaction_default_blacklist");
        public static final TagKey<Block> HCL_BLACKLIST = createTag("hcl_blacklist");
        public static final TagKey<Block> GLAZED_TERRACOTTA = createTag("glazed_terracotta");

        /**
         * Create a Block tag (tag is only used inside this mod)
         * Don't forget the json file (data/minelabs/tags/blocks)
         *
         * @param name : name of the tag
         * @return {@link TagKey}
         */
        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(Minelabs.MOD_ID, name));
        }

        /**
         * Create a Block tag (tag for usage outside this mod)
         * Don't forget the json file (data/c/tags/blocks)
         *
         * @param name : name of the tag
         * @return {@link TagKey}
         */
        private static TagKey<Block> createCommonTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier("c", name));
        }
    }

    public static class Items {
        public static final TagKey<Item> LASERTOOLS = createTag("lasertools");
        public static final TagKey<Item> FIRE_CHANGER = createCommonTag("fire_changer");
        public static final TagKey<Item> FIRE_EXTINGUISH = createTag("fire_extinguish");

        public static final TagKey<Item> CHARGE = createTag("charge");
        public static final TagKey<Item> NEGATIVE_CHARGE = createTag("negative_charge");
        public static final TagKey<Item> POSITIVE_CHARGE = createTag("positive_charge");

        public static final TagKey<Item> BLACK_HOLE_BLACKLIST = createTag("black_hole_blacklist");
        public static final TagKey<Item> MAGNET_WHITELIST = createTag("magnet_whitelist");

        /**
         * Create an Item tag (tag is only used inside this mod)
         * Don't forget the json file (data/minelabs/tags/items)
         *
         * @param name : name of the tag
         * @return {@link TagKey}
         */
        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(Minelabs.MOD_ID, name));
        }

        /**
         * Create an Item tag (tag for usage outside this mod)
         * Don't forget the json file (data/c/tags/items)
         *
         * @param name : name of the tag
         * @return {@link TagKey}
         */
        private static TagKey<Item> createCommonTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier("c", name));
        }
    }

    public static class Fluids {

        public static final TagKey<Fluid> NEWTON_FLUID = createTag("newton_fluid");

        /**
         * Create a Fluid tag (tag is only used inside this mod)
         * Don't forget the json file (data/minelabs/tags/fluids)
         *
         * @param name : name of the tag
         * @return {@link TagKey}
         */
        private static TagKey<Fluid> createTag(String name) {
            return TagKey.of(RegistryKeys.FLUID, new Identifier(Minelabs.MOD_ID, name));
        }

        /**
         * Create a Fluid tag (tag for usage outside this mod)
         * Don't forget the json file (data/c/tags/fluids)
         *
         * @param name : name of the tag
         * @return {@link TagKey}
         */
        private static TagKey<Fluid> createCommonTag(String name) {
            return TagKey.of(RegistryKeys.FLUID, new Identifier("c", name));
        }
    }
}
