package be.minelabs.client.renderer.model;

import be.minelabs.Minelabs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class MologramBeamModel implements UnbakedModel, BakedModel, FabricBakedModel {
    private static final SpriteIdentifier SPRITE_ID = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Minelabs.MOD_ID, "block/mologram/mologram_beam"));
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
        return ModelHelper.MODEL_TRANSFORM_BLOCK;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.emptyList();
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

    }

    @Nullable
    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        SPRITE = textureGetter.apply(SPRITE_ID);

        // Build the mesh using the Renderer API
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        //makeBEAM(emitter, center, radius);
        makeBeam(emitter,0.7f, 0.4f);
        makeBeam(emitter,0.55f, 0.45f);
        mesh = builder.build();

        return this;
    }

    private void face(List<Vector3f[]> quads, float height, float x, float z){
        quads.add(new Vector3f[]{
                new Vector3f(0, 2/16f, 2/16f*z/height),
                new Vector3f(2/16f*x/height, 2/16f, 0),
                new Vector3f(x, height, 0),
                new Vector3f(0, height, z)

    });
        quads.add(new Vector3f[]{
                new Vector3f(2/16f*x/height, 2/16f, 0),
                new Vector3f(0, 2/16f, 2/16f*z/height),
                new Vector3f(0, height, z),
                new Vector3f(x, height, 0)
                        });
    }

    public List<Vector3f[]> getBeamVertices(float height, float width) {
        List<Vector3f[]> quads = new ArrayList<>();
        face(quads, height, width,-width);
        face(quads, height,-width,-width);
        face(quads, height, width, width);
        face(quads, height,-width, width);

        ModelUtil.transformQuads(quads, v -> v.add(0.5f, 0.65f, 0.5f));
        return quads;
    }

    private void makeBeam(QuadEmitter emitter, float height, float width) {
        for (Vector3f[] quad : getBeamVertices(height, width)) {
            for (int i = 0; i < 4; i++) {
                emitter.pos(i, quad[i]);
                emitter.normal(i, new Vector3f(0,1,0));
            }

            emitter.sprite(0, 0, 16, 16);
            emitter.sprite(3, 0, 16, 0);
            emitter.sprite(2, 0, 0, 0);
            emitter.sprite(1, 0, 0, 16);

            emitter.spriteBake(0, SPRITE, MutableQuadView.BAKE_ROTATE_NONE);

            // Enable texture usage
            emitter.spriteColor(0, -1, -1, -1, -1);

            emitter.material(RendererAccess.INSTANCE.getRenderer().materialFinder().blendMode(0, BlendMode.TRANSLUCENT).disableAo(0, true).disableDiffuse(0, true).emissive(0, true).find());

            // Add the quad to the mesh
            emitter.emit();
        }
    }
}