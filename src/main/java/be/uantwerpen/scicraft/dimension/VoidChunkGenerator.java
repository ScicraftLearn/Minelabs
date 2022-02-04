package be.uantwerpen.scicraft.dimension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

// https://github.com/FabricMC/fabric/blob/1.18/fabric-dimensions-v1/src/testmod/java/net/fabricmc/fabric/test/dimension/VoidChunkGenerator.java
public class VoidChunkGenerator extends ChunkGenerator {
    // Just an example of adding a custom boolean
    protected final boolean customBool;

    public static final Codec<VoidChunkGenerator> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                            BiomeSource.CODEC.fieldOf("biome_source")
                                    .forGetter((generator) -> generator.biomeSource),
                            Codec.BOOL.fieldOf("custom_bool")
                                    .forGetter((generator) -> generator.customBool)
                    )
                    .apply(instance, instance.stable(VoidChunkGenerator::new))
    );

    public VoidChunkGenerator(BiomeSource biomeSource, boolean customBool) {
        super(biomeSource, new StructuresConfig(false));
        this.customBool = customBool;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return this;
    }

    @Override
    public MultiNoiseUtil.MultiNoiseSampler getMultiNoiseSampler() {
        // Mirror what Vanilla does in the debug chunk generator
        return (x, y, z) -> MultiNoiseUtil.createNoiseValuePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long l, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carver) {
    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structureAccessor, Chunk chunk) {
    }

    @Override
    public void populateEntities(ChunkRegion region) {
    }

    @Override
    public int getWorldHeight() {
        return 0;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinimumY() {
        return 0;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmapType, HeightLimitView heightLimitView) {
        return 0;
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView heightLimitView) {
        return new VerticalBlockSample(0, new BlockState[0]);
    }
}
