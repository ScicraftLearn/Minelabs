package be.uantwerpen.scicraft.model;

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
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class SphereModel implements UnbakedModel, BakedModel, FabricBakedModel {

    private Mesh mesh;

    private static final SpriteIdentifier SPRITE_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("scicraft:item/atom_orange"));

    private Sprite SPRITE;

    private static final Identifier DEFAULT_BLOCK_MODEL = new Identifier("minecraft:block/block");

    private ModelTransformation transformation;

    public Collection<Identifier> getModelDependencies() {
        return List.of(DEFAULT_BLOCK_MODEL);
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return List.of(SPRITE_ID);
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
        // Don't need because we use FabricBakedModel instead. However, it's better to not return null in case some mod decides to call this function.
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true; // we want the block to have a shadow depending on the adjacent blocks
    }

    @Override
    public boolean isVanillaAdapter() {
        return false; // False to trigger FabricBakedModel rendering
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
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
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        SPRITE = textureGetter.apply(SPRITE_ID);

        JsonUnbakedModel defaultBlockModel = (JsonUnbakedModel) loader.getOrLoadModel(DEFAULT_BLOCK_MODEL);
        // Get its ModelTransformation
        transformation = defaultBlockModel.getTransformations();

        // Build the mesh using the Renderer API
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        Vec3f center = new Vec3f(0, 0, 0);
        float radius = 0.15f;

        for (Vec3f[] quad: getSphereVertices(center, radius)){
            for(int i=0; i < 4; i++) {
                emitter.pos(i, quad[i].getX(), quad[i].getY(), quad[i].getZ());
                quad[i].subtract(center);
                quad[i].normalize();
                emitter.normal(i, quad[i]);
            }
//                emitter.spriteUnitSquare(0);

            emitter.sprite(0, 0, 8, 8);
            emitter.sprite(1, 0, 8, 8);
            emitter.sprite(2, 0, 8, 8);
            emitter.sprite(3, 0, 8, 8);

            emitter.spriteBake(0, SPRITE, MutableQuadView.BAKE_ROTATE_NONE);

            // Enable texture usage
            emitter.spriteColor(0, -1, -1, -1, -1);

            // Add the quad to the mesh
            emitter.emit();
        }

        mesh = builder.build();

        return this;
    }

    public List<Vec3f[]> getSphereVertices(Vec3f center, float r) {
        center.add(0.5f, 0.5f, 0.5f);

        List<Vec3f[]> quads = new ArrayList<>();
        int RESOLUTION = 2;
        float offset = 0.5f;

        Vec3f[] face = {
                new Vec3f(-offset, offset, -offset),
                new Vec3f(offset, offset, -offset),
                new Vec3f(offset, -offset, -offset),
                new Vec3f(-offset, -offset, -offset),
        };
        recursiveSubdivision(face, RESOLUTION, quads);

        face = new Vec3f[]{
                new Vec3f(-offset, -offset, offset),
                new Vec3f(offset, -offset, offset),
                new Vec3f(offset, offset, offset),
                new Vec3f(-offset, offset, offset),
        };
        recursiveSubdivision(face, RESOLUTION, quads);

        face = new Vec3f[]{
                new Vec3f(-offset, -offset, offset),
                new Vec3f(-offset, offset, offset),
                new Vec3f(-offset, offset, -offset),
                new Vec3f(-offset, -offset, -offset),
        };
        recursiveSubdivision(face, RESOLUTION, quads);

        face = new Vec3f[]{
                new Vec3f(offset, -offset, -offset),
                new Vec3f(offset, offset, -offset),
                new Vec3f(offset, offset, offset),
                new Vec3f(offset, -offset, offset),
        };
        recursiveSubdivision(face, RESOLUTION, quads);

        face = new Vec3f[]{
                new Vec3f(-offset, -offset, -offset),
                new Vec3f(offset, -offset, -offset),
                new Vec3f(offset, -offset, offset),
                new Vec3f(-offset, -offset, offset),
        };
        recursiveSubdivision(face, RESOLUTION, quads);

        face = new Vec3f[]{
                new Vec3f(-offset, offset, offset),
                new Vec3f(offset, offset, offset),
                new Vec3f(offset, offset, -offset),
                new Vec3f(-offset, offset, -offset),
        };
        recursiveSubdivision(face, RESOLUTION, quads);

        for (Vec3f[] quad: quads){
            for(Vec3f vertex:quad){
                vertex.scale(r);
                vertex.add(center);
            }
        }

        return quads;
    }

    private List<Vec3f[]> recursiveSubdivision(Vec3f[] quad, int RESOLUTION, List<Vec3f[]> quads){
        if (RESOLUTION<=0){
            quads.add(quad);
        } else {
            Vec3f va = quad[0].copy();
            va.add(quad[1]);
            va.normalize();

            Vec3f vb = quad[0].copy();
            vb.add(quad[3]);
            vb.normalize();

            Vec3f vc = quad[0].copy();
            vc.add(quad[2]);
            vc.normalize();

            Vec3f vd = quad[2].copy();
            vd.add(quad[1]);
            vd.normalize();

            Vec3f ve = quad[3].copy();
            ve.add(quad[2]);
            ve.normalize();

            recursiveSubdivision(new Vec3f[] {quad[0].copy(), va.copy(), vc.copy(), vb.copy()}, RESOLUTION-1, quads);
            recursiveSubdivision(new Vec3f[] {va.copy(), quad[1].copy(), vd.copy(), vc.copy()}, RESOLUTION-1, quads);
            recursiveSubdivision(new Vec3f[] {vc.copy(), vd.copy(), quad[2].copy(), ve.copy()}, RESOLUTION-1, quads);
            recursiveSubdivision(new Vec3f[] {vb.copy(), vc.copy(), ve.copy(), quad[3].copy()}, RESOLUTION-1, quads);
        }
        return quads;

    }

    @Override
    public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {
        // We just render the mesh
        renderContext.meshConsumer().accept(mesh);
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        context.meshConsumer().accept(mesh);
    }

}
