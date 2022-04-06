package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.block.Blocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;


public class BlockEntities {

    // BlockEntities
    // Value of charge here will be used when saved and restored if not saved under NBT-tag. (default value for BlkEn.)
    public static final BlockEntityType<PionMinusBlockEntity> PION_MINUS_BLOCK_ENTITY = register(
            FabricBlockEntityTypeBuilder.create(
                    (BlockPos pos, BlockState state) -> {return new PionMinusBlockEntity(pos, state, 0.0);},
                    Blocks.PION_MINUS).build(null), "pion_minus_block_entity");
    public static final BlockEntityType<PionPlusBlockEntity> PION_PLUS_BLOCK_ENTITY = register(
            FabricBlockEntityTypeBuilder.create(
                    (BlockPos pos, BlockState state) -> {return new PionPlusBlockEntity(pos, state, 0.0);},
                    Blocks.PION_PLUS).build(null), "pion_plus_block_entity");

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
