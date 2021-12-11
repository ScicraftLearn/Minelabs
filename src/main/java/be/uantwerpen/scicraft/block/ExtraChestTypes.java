package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.entity.Entities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public enum ExtraChestTypes {

    SCHRODINGERS_BOX(54, 9, new Identifier(Scicraft.MOD_ID, "textures/entity/chest/schrodingers_box"));

    public final int size;
    public final int rowLength;
    public final Identifier texture;

    ExtraChestTypes(int size, int rowLength, Identifier texture) {
        this.size = size;
        this.rowLength = rowLength;
        this.texture = texture;
    }

    public int getRowCount() {
        return this.size / this.rowLength;
    }

    public static Block get(ExtraChestTypes type) {
        return switch (type) {
            case SCHRODINGERS_BOX -> Blocks.SCHRODINGERS_BOX;
            default -> Blocks.SCHRODINGERS_BOX;
        };
    }

    public ChestBlockEntity getEntity(BlockPos pos, BlockState state) {
        return switch (this) {
            case SCHRODINGERS_BOX -> Entities.SCHRODINGERS_BOX_ENTITY.instantiate(pos, state);
            default -> new ChestBlockEntity(pos, state);
        };
    }

    public BlockEntityType<? extends ChestBlockEntity> getBlockEntityType() {
        return switch (this) {
            case SCHRODINGERS_BOX -> Entities.SCHRODINGERS_BOX_ENTITY;
            default -> BlockEntityType.CHEST;
        };
    }

//    public ScreenHandlerType<ExtraChestScreenHandler> getScreenHandlerType() {
//        return switch (this) {
//            case IRON -> ModScreenHandlerType.IRON_CHEST;
//            case GOLD -> ModScreenHandlerType.GOLD_CHEST;
//            case DIAMOND -> ModScreenHandlerType.DIAMOND_CHEST;
//            case COPPER -> ModScreenHandlerType.COPPER_CHEST;
//            case SILVER -> ModScreenHandlerType.SILVER_CHEST;
//            case CRYSTAL -> ModScreenHandlerType.CRYSTAL_CHEST;
//            case OBSIDIAN -> ModScreenHandlerType.OBSIDIAN_CHEST;
//            case DIRT -> ModScreenHandlerType.DIRT_CHEST;
//            default -> ModScreenHandlerType.HOLIDAY_CHEST;
//        };
//    }
}