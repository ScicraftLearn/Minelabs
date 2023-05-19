package be.minelabs;

import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.block.Blocks;
import be.minelabs.block.ExtraDispenserBehavior;
import be.minelabs.block.entity.BlockEntities;
import be.minelabs.recipe.CraftingRecipes;
import be.minelabs.world.dimension.ModDimensions;
import be.minelabs.entity.effect.Effects;
import be.minelabs.entity.Entities;
import be.minelabs.village.Villagers;
import be.minelabs.event.ServerModEvents;
import be.minelabs.fluid.Fluids;
import be.minelabs.item.ItemGroups;
import be.minelabs.item.Items;
import be.minelabs.entity.decoration.painting.Paintings;
import be.minelabs.particle.Particles;
import be.minelabs.screen.ScreenHandlers;
import be.minelabs.sound.SoundEvents;
import be.minelabs.util.NucleusStabilityTable;
import be.minelabs.world.gen.WorldGeneration;
import be.minelabs.world.gen.VillageAdditions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Minelabs implements ModInitializer {

    public static final String MOD_ID = "minelabs";

    // This logger is used to write text to the console and the log file.
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(NucleusStabilityTable.INSTANCE);

        Items.onInitialize();
        Blocks.onInitialize();
        Fluids.onInitialize();
        Entities.onInitialize();

        SoundEvents.onInitialize();
        Particles.onInitialize();
        ScreenHandlers.onInitialize();

        WorldGeneration.onInitialize();
        ModDimensions.onInitialize();

        CraftingRecipes.onInitialize();
        Villagers.onInitialize();

        ServerModEvents.onInitialize();
        Criteria.onInitialize();
    }
}