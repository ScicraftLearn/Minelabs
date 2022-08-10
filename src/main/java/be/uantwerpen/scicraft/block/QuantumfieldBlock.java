package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.dimension.ModDimensions;
import be.uantwerpen.scicraft.event.ModEvents;
import be.uantwerpen.scicraft.util.QuantumFieldSpawner;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import net.minecraft.state.StateManager;

public class QuantumfieldBlock extends Block {
    private static final java.util.Random r = new java.util.Random();
    public static final IntProperty AGE = IntProperty.of("age",0,25);


    public QuantumfieldBlock() {
        // Properties of all quantumfield blocks
        // Change the first value in strength to get the wanted mining speed
        super(FabricBlockSettings.of(Material.METAL).noCollision().strength(0.5f, 2.0f).ticksRandomly());
        this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
    }


    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(AGE);
        int decayrate = 1;
        int max_age = 25;


        if (age+decayrate >= max_age) {
            QuantumFieldSpawner.breakCluster(world, pos);
        } else {
            state = state.with(AGE, Math.min(max_age, age + decayrate));
            world.setBlockState(pos, state);

        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, state));
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        if (!world.isClient()){
            world.setBlockState(pos, state);
        }
    }


}
