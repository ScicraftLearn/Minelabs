package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.item.Items;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class BlockEntities {
    public static BlockEntityType<ChargedBlockEntityNEW> ELECTRON_BLOCK_ENTITY;
    public static BlockEntityType<ChargedBlockEntityNEW> POSTIRON_BLOCK_ENTITY;
    public static BlockEntityType<ChargedBlockEntity> PROTON_BLOCK_ENTITY;
    public static BlockEntityType<ChargedBlockEntity> PION_MINUS_BLOCK_ENTITY;
    public static BlockEntityType<ChargedBlockEntity> PION_PLUS_BLOCK_ENTITY;
    public static BlockEntityType<AnimatedChargedBlockEntity> ANIMATED_CHARGED_BLOCK_ENTITY;
    public static BlockEntityType<ChargedPlaceholderBlockEntity> CHARGED_PLACEHOLDER_BLOCK_ENTITY;
    
    static {
        PION_MINUS_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedBlockEntity(PION_MINUS_BLOCK_ENTITY, p, s, -1, Blocks.PION_PLUS, 50, new ItemStack(Items.WEAK_BOSON)), Blocks.PION_MINUS).build(null),
                "pion_minus_block_entity");

        PION_PLUS_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedBlockEntity(PION_PLUS_BLOCK_ENTITY, p, s, 1, Blocks.PION_MINUS, 50, new ItemStack(Items.WEAK_BOSON)), Blocks.PION_PLUS).build(null),
                "pion_plus_block_entity");

    	ELECTRON_BLOCK_ENTITY = register(
    			FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedBlockEntityNEW(ELECTRON_BLOCK_ENTITY, p, s, -1, Blocks.POSITRON, 0, null), Blocks.ELECTRON).build(null),
    			"electron_block_entity");
    	
    	POSTIRON_BLOCK_ENTITY = register(
    			FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedBlockEntityNEW(POSTIRON_BLOCK_ENTITY, p, s, 1, Blocks.ELECTRON, 0, null), Blocks.POSITRON).build(null),
    			"positron_block_entity");

        PROTON_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedBlockEntity(PROTON_BLOCK_ENTITY, p, s, 1, null, 50, new ItemStack(Items.WEAK_BOSON)), Blocks.PROTON).build(null),
                "proton_block_entity");

        ANIMATED_CHARGED_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p,s) -> new AnimatedChargedBlockEntity(ANIMATED_CHARGED_BLOCK_ENTITY, p, s), Blocks.ANIMATED_CHARGED).build(null),
                "animated_charged_block_entity");

        CHARGED_PLACEHOLDER_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedPlaceholderBlockEntity(CHARGED_PLACEHOLDER_BLOCK_ENTITY, p, s), Blocks.CHARGED_PLACEHOLDER).build(null),
                "charged_placeholder_block_entity");
    }

    /**
     * Register a BlockEntity
     * <p>
     *
     * @param blockEntityType      : BlockEntityType Object to register
     * @param identifier : String name of the Item
     * @return {@link Block}
     */
    private static <T extends BlockEntity> BlockEntityType<T> register(BlockEntityType<T> blockEntityType, String identifier) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Scicraft.MOD_ID, identifier), blockEntityType);
    }

    /**
     * Main class method
     * Registers all BlocksEntities
     */
    public static void registerBlockEntities() {
        Scicraft.LOGGER.info("registering blockEntities");
    }
}
