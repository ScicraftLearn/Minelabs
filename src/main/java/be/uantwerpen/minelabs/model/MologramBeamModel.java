package be.uantwerpen.minelabs.model;

import be.uantwerpen.minelabs.Minelabs;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class MologramBeamModel implements UnbakedModel, BakedModel, FabricBakedModel {
    private static final SpriteIdentifier SPRITE_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Minelabs.MOD_ID, "block/helium"));
    private static final Identifier DEFAULT_BLOCK_MODEL = new Identifier("minecraft:block/block");
    private ModelTransformation transformation;
    private Sprite SPRITE;
    private Mesh mesh;
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        context.meshConsumer().accept(mesh);
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false ;
    }

    @Override
    public boolean hasDepth() {
        return true;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }

    @Override
    public Sprite getParticleSprite() {
        return SPRITE;
    }

    @Override
    public ModelTransformation getTransformation() {
        return transformation;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of(DEFAULT_BLOCK_MODEL);
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return List.of(SPRITE_ID);
    }

    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        SPRITE = textureGetter.apply(SPRITE_ID);

        Minelabs.LOGGER.info(SPRITE);

        JsonUnbakedModel defaultBlockModel = (JsonUnbakedModel) loader.getOrLoadModel(DEFAULT_BLOCK_MODEL);
        // Get its ModelTransformation
        transformation = defaultBlockModel.getTransformations();

        // Build the mesh using the Renderer API
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        //makeBEAM(emitter, center, radius);
        makeBeam(emitter,0.7f, 0.4f);

        mesh = builder.build();

        return this;
    }
    private void face(List<Vec3f[]> quads, float height, float x, float z){
        quads.add(new Vec3f[]{
                new Vec3f(0, 0, 0),
                new Vec3f(0, 0, 0),
                new Vec3f(x, height, 0),
                new Vec3f(0, height, z)

    });
        quads.add(new Vec3f[]{
                new Vec3f(0, height, z),
                new Vec3f(x, height, 0),
                new Vec3f(0, 0, 0),
                new Vec3f(0, 0, 0)
                        });

    }
    public List<Vec3f[]> getBeamVertices(float height, float width) {
        List<Vec3f[]> quads = new ArrayList<>();
        face(quads, height, width,-width);
        face(quads, height,-width,-width);
        face(quads, height, width, width);
        face(quads, height,-width, width);


        return quads;
    }

    private void makeBeam(QuadEmitter emitter, float height, float width) {
        for (Vec3f[] quad : getBeamVertices(height, width)) {
            for (int i = 0; i < 4; i++) {
                emitter.pos(i, quad[i]);
                //emitter.normal(i, quad[i]);
            }

            emitter.sprite(0, 0, 1, 1);
            emitter.sprite(1, 0, 1, 0);
            emitter.sprite(2, 0, 0, 0);
            emitter.sprite(3, 0, 0, 1);

            emitter.spriteBake(0, SPRITE, MutableQuadView.BAKE_ROTATE_NONE);

            // Enable texture usage
            emitter.spriteColor(0, -1, -1, -1, -1);

            // Add the quad to the mesh
            emitter.emit();
        }
    }
}