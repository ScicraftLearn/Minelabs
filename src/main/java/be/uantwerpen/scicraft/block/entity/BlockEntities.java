package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.Scicraft;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import static be.uantwerpen.scicraft.block.Blocks.PION_MINUS;
import static be.uantwerpen.scicraft.block.Blocks.PION_PLUS;


public class BlockEntities {

    // BlockEntities
    // Value of charge here will be used when saved and restored if not saved under NBT-tag. (default value for BlkEn.)
    public static final BlockEntityType<ChargedBlockEntity> ABSTRACT_CHARGED_BLOCK_ENTITY = register(
            FabricBlockEntityTypeBuilder.create(
                    (BlockPos pos, BlockState state) -> {return new ChargedBlockEntity(pos, state, 0.0);},
                    PION_MINUS).build(null), "charged_block");

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
