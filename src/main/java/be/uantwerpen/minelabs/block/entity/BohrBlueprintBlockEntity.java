package be.uantwerpen.minelabs.block.entity;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Stack;

public class BohrBlueprintBlockEntity extends BlockEntity {

    // A reference to this inventory is kept in the entity.
//    private final Stack<ItemStack> inventory = new Stack<>();

    public BohrBlueprintBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.BOHR_BLUEPRINT_BLOCK_ENTITY, pos, state);
    }

//    public Stack<ItemStack> getInventory(){
//        return inventory;
//    }

    /**
     * This block entity is only used to persist the nbt data of the bohr entity.
     */
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

//        Minelabs.LOGGER.info("Writing nbt data to bohr plate be");

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

//        Minelabs.LOGGER.info("Reading nbt data of bohr plate be");
    }

}
