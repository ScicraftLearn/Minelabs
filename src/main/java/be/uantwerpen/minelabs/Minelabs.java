package be.uantwerpen.minelabs;

import be.uantwerpen.minelabs.advancement.criterion.Criteria;
import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.block.entity.BlockEntities;
import be.uantwerpen.minelabs.crafting.CraftingRecipes;
import be.uantwerpen.minelabs.dimension.ModDimensions;
import be.uantwerpen.minelabs.effect.Effects;
import be.uantwerpen.minelabs.entity.Entities;
import be.uantwerpen.minelabs.entity.Villagers;
import be.uantwerpen.minelabs.event.ServerModEvents;
import be.uantwerpen.minelabs.fluid.Fluids;
import be.uantwerpen.minelabs.gui.ScreenHandlers;
import be.uantwerpen.minelabs.item.ItemGroups;
import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.paintings.Paintings;
import be.uantwerpen.minelabs.particle.Particles;
import be.uantwerpen.minelabs.sound.SoundEvents;
import be.uantwerpen.minelabs.util.AtomConfiguration;
import be.uantwerpen.minelabs.util.NucleusStabilityTable;
import be.uantwerpen.minelabs.world.gen.OreGenerations;
import be.uantwerpen.minelabs.world.village.VillageAdditions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
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
        OreGenerations.generateOres();
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