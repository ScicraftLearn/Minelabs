package be.minelabs.block.entity;

import be.minelabs.science.Molecule;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChemicalFluidBlock extends FluidBlock {

    private final Molecule molecule;

    public ChemicalFluidBlock(FlowableFluid fluid, Molecule molecule) {
        super(fluid, FabricBlockSettings.copy(net.minecraft.block.Blocks.WATER));
        this.molecule = molecule;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            this.molecule.react(livingEntity);
        }
        super.onEntityCollision(state, world, pos, entity);
    }
}
