package be.minelabs;

import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.dimension.ModDimensions;
import be.minelabs.screen.ScreenHandlers;
import be.minelabs.item.Items;
import be.minelabs.paintings.Paintings;
import be.minelabs.particle.Particles;
import be.minelabs.util.NucleusStabilityTable;
import be.minelabs.world.gen.WorldGeneration;
import be.minelabs.world.village.VillageAdditions;
import be.minelabs.block.Blocks;
import be.minelabs.block.entity.BlockEntities;
import be.minelabs.crafting.CraftingRecipes;
import be.minelabs.effect.Effects;
import be.minelabs.entity.Entities;
import be.minelabs.entity.Villagers;
import be.minelabs.event.ServerModEvents;
import be.minelabs.fluid.Fluids;
import be.minelabs.item.ItemGroups;
import be.minelabs.sound.SoundEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;


public class Minelabs implements ModInitializer {

    public static final String MOD_ID = "minelabs";

    // This logger is used to write text to the console and the log file.
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static InputStream csvFile;

    @Override
    public void onInitialize() {

        //Gasses.registerPotions(); // Must be before Items/Blocks

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(NucleusStabilityTable.INSTANCE);

        Items.registerItems();
        ItemGroups.registerItemGroups();
        Blocks.registerBlocks();
        BlockEntities.registerBlockEntities();
        Entities.registerEntities();
        ExtraDispenserBehavior.registerBehaviors();
        Fluids.registerFluids();
        SoundEvents.registerSounds();
        ScreenHandlers.registerScreens();
        Paintings.registerPaintings();
        WorldGeneration.generateCustomWorld();
        VillageAdditions.registerVillageStructures();
        Effects.registerStatusEffects();
        Particles.registerParticles();

        CraftingRecipes.register();

        ModDimensions.register();

        Villagers.registerVillagers();
        ServerModEvents.registerEvents();

        Criteria.registerCriteria();
    }
}