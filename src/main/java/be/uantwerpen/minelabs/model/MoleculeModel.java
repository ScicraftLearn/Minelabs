package be.uantwerpen.minelabs.model;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.crafting.molecules.Atom;
import be.uantwerpen.minelabs.crafting.molecules.Bond;
import be.uantwerpen.minelabs.crafting.molecules.MoleculeGraphJsonFormat;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import java.io.Reader;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class MoleculeModel implements UnbakedModel, BakedModel, FabricBakedModel {

    private Mesh mesh;

    private static final SpriteIdentifier SPRITE_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Minelabs.MOD_ID, "block/mologram/sphere"));

    private Sprite SPRITE;

    private static final Identifier DEFAULT_BLOCK_MODEL = new Identifier("minecraft:block/block");

    private ModelTransformation transformation;

    Map<String, Pair<Atom, Vector3f>> positions;
    Map<Pair<String, String>, Bond> bonds;

    private final float RADIUS_S = 0.08f;
    private final float RADIUS_M = 0.10f;
    private final float RADIUS_L = 0.12f;
    private final float MARGIN = 0.06f;

    public MoleculeModel(Map<String, Pair<Atom, Vector3f>> positions, Map<Pair<String, String>, Bond> bondMap) {
        this.positions = positions;
        this.bonds = bondMap;
    }

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

    private float getAtomSize(Atom atom) {
        return switch (atom) {
            case HYDROGEN, HELIUM, FLUORINE, LITHIUM, SODIUM -> RADIUS_S; // Small Atoms
            case ARGON, POTASSIUM, CALCIUM, TITANIUM, MANGANESE, IRON, COPPER, ZINC, BROMINE, STRONTIUM, SILVER,
                    CADMIUM, TIN, IODINE, TUNGSTEN, GOLD, MERCURY, LEAD, URANIUM -> RADIUS_L; // Large Atoms (PLACEHOLDERS)
            default -> RADIUS_M; // All others are Medium Atoms
        };
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

        //makeSphere(emitter, center, radius);
        positions.forEach((s, atomVector3fPair) -> makeSphere(emitter, atomVector3fPair.getSecond(), getAtomSize(atomVector3fPair.getFirst()), atomVector3fPair.getFirst().getColor())); //RGB color in hex
        bonds.forEach((s, bond) -> makeBond(emitter, positions.get(s.getFirst()).getSecond(),
                positions.get(s.getSecond()).getSecond(),
                positions.get(s.getFirst()).getFirst().getColor(),
                positions.get(s.getSecond()).getFirst().getColor(),
                bond));

        mesh = builder.build();

        return this;
    }

    /**
     * @param emitter : QuadEmitter
     * @param pos1 : position Atom 1
     * @param pos2 : position Atom 2
     * @param bond : type of bond between Atoms
     */
    private void makeBond(QuadEmitter emitter, Vector3f pos1, Vector3f pos2, int color1, int color2, Bond bond) {
        if (bond == Bond.COVALENT_ZERO){
            return;
        }

        Vector3f pos_diff = new Vector3f(pos2);
        pos_diff.sub(pos1);
        float pos_norm = ModelUtil.norm(pos_diff);

        List<Vector3f[]> bondQuads = switch (bond){
            case COVALENT_SINGLE -> getSingleBondVertices(pos_norm);
            case COVALENT_DOUBLE -> getDoubleBondVertices(pos_norm);
            case COVALENT_TRIPLE -> getTripleBondVertices(pos_norm);
            default -> throw new IllegalStateException("Unknown bond count " + bond);
        };

        // direction is the orientation of the bond
        Vector3f direction = new Vector3f(pos2);
        direction.sub(pos1);
        direction.normalize();
        // compute angle between positive X and direction
        float angle = (float) Math.acos(direction.dot(Vector3f.POSITIVE_X));
        // rotate within plane described by two vectors
        direction.cross(Vector3f.POSITIVE_X);
        direction.normalize();

        Quaternion rotation = direction.getRadialQuaternion(angle);
        rotation.conjugate();
        ModelUtil.transformQuads(bondQuads, v -> v.rotate(rotation));
        ModelUtil.transformQuads(bondQuads, v -> v.add(pos1));

        for (Vector3f[] quad : bondQuads) {
            for (int i = 0; i < 4; i++) {
                emitter.pos(i, quad[i]);
                Vector3f norm = ModelUtil.normalOnVertices(quad[i],quad[(((i+1 % 4) + 4) % 4)], quad[(((i-1 % 4) + 4) % 4)]); //zodat licht van beam lijkt te komen
                emitter.normal(i, norm);
            }

            float p = 15f / 32f;
            emitter.sprite(0, 0, p, p);
            emitter.sprite(1, 0, p, 0.5f);
            emitter.sprite(2, 0, 0.5f, 0.5f);
            emitter.sprite(3, 0, 0.5f, p);

            emitter.spriteBake(0, SPRITE, MutableQuadView.BAKE_ROTATE_NONE);

            // Enable texture usage
            if (color1==color2){
                int color = color1; //TODO darken integer color
                emitter.spriteColor(0, color, color, color, color);
            }
            else {
                emitter.spriteColor(0, color1, color1, color2, color2);
            }
            // Add the quad to the mesh
            emitter.emit();
        }
    }

    public List<Vector3f[]> getSingleBondVertices(float len) {
        float a = 0.05f;
        return getUnitBeam(len, a);
    }

    public List<Vector3f[]> getDoubleBondVertices(float len) {
        float a = 0.045f;
        float offset = 0.035f;
        List<Vector3f[]> quads = new ArrayList<>();
        quads.addAll(ModelUtil.transformQuads(getUnitBeam(len, a), v -> v.add(0, offset, 0)));
        quads.addAll(ModelUtil.transformQuads(getUnitBeam(len, a), v -> v.add(0, -offset, 0)));
        return quads;
    }

    public List<Vector3f[]> getTripleBondVertices(float len) {
        float a = 0.038f;
        float offset = 0.045f;

        List<Vector3f[]> quads = new ArrayList<>();
        Vector3f offsetDirection = new Vector3f(0, offset, 0);
        quads.addAll(ModelUtil.transformQuads(getUnitBeam(len, a), v -> v.add(offsetDirection)));
        offsetDirection.rotate(Vector3f.POSITIVE_X.getDegreesQuaternion(120));
        quads.addAll(ModelUtil.transformQuads(getUnitBeam(len, a), v -> v.add(offsetDirection)));
        offsetDirection.rotate(Vector3f.POSITIVE_X.getDegreesQuaternion(120));
        quads.addAll(ModelUtil.transformQuads(getUnitBeam(len, a), v -> v.add(offsetDirection)));
        return quads;
    }

    /**
     * Create a cuboid (beam) of specified length and square endpoint.
     * Bean follows and is centered around the x-axis.
     * @param len : length of beam
     * @param b   : size of square (endpoint)
     */
    public List<Vector3f[]> getUnitBeam(float len, float b){
        float a = b/2;
        len -= 2 * RADIUS_S - 2 * MARGIN;
        List<Vector3f[]> quads = new ArrayList<>();
        quads.add(new Vector3f[]{//links
                new Vector3f(0, -a, -a),
                new Vector3f(0, a, -a),
                new Vector3f(len, a, -a),
                new Vector3f(len, -a, -a),
        });
        quads.add(new Vector3f[]{//rechts
                new Vector3f(0, a, a),
                new Vector3f(0, -a, a),
                new Vector3f(len, -a, a),
                new Vector3f(len, a, a),
        });
        quads.add(new Vector3f[]{//onder
                new Vector3f(0, -a, a),
                new Vector3f(0, -a, -a),
                new Vector3f(len, -a, -a),
                new Vector3f(len, -a, a),
        });
        quads.add(new Vector3f[]{//boven
                new Vector3f(0, a, -a),
                new Vector3f(0, a, a),
                new Vector3f(len, a, a),
                new Vector3f(len, a, -a),
        });
        ModelUtil.transformQuads(quads, v -> v.add(RADIUS_S - MARGIN, 0, 0));
        return quads;
    }

    private void makeSphere(QuadEmitter emitter, Vector3f center, float radius, int color) {
        for (Vector3f[] quad : getSphereVertices(center, radius)) {
            for (int i = 0; i < 4; i++) {
                emitter.pos(i, quad[i]);
                Vector3f norm = ModelUtil.normalOnVertices(quad[i],quad[(((i+1 % 4) + 4) % 4)], quad[(((i-1 % 4) + 4) % 4)]); //zodat licht van beam lijkt te komen
                emitter.normal(i, norm);
            }

            float p = 1;
            emitter.sprite(0, 0, p, p);
            emitter.sprite(1, 0, p, 0);
            emitter.sprite(2, 0, 0, 0);
            emitter.sprite(3, 0, 0, p);

            emitter.spriteBake(0, SPRITE, MutableQuadView.BAKE_ROTATE_NONE);

            // Enable texture usage
            emitter.spriteColor(0, color, color, color, color);

            // Add the quad to the mesh
            emitter.emit();
        }
    }

    public List<Vector3f[]> getSphereVertices(Vector3f center, float r) {
        center.add(0.5f, 0.5f, 0.5f);

        List<Vector3f[]> quads = new ArrayList<>();
        int RESOLUTION = 2;
        float offset = 1/(float) Math.sqrt(3); //moet genormaliseerd zijn

        Vector3f[] face = {
                new Vector3f(-offset, offset, -offset),
                new Vector3f(offset, offset, -offset),
                new Vector3f(offset, -offset, -offset),
                new Vector3f(-offset, -offset, -offset),
        };
        recursiveSubdivision(face, RESOLUTION, quads);

        face = new Vector3f[]{
                new Vector3f(-offset, -offset, offset),
                new Vector3f(offset, -offset, offset),
                new Vector3f(offset, offset, offset),
                new Vector3f(-offset, offset, offset),
        };
        recursiveSubdivision(face, RESOLUTION, quads);

        face = new Vector3f[]{
                new Vector3f(-offset, -offset, offset),
                new Vector3f(-offset, offset, offset),
                new Vector3f(-offset, offset, -offset),
                new Vector3f(-offset, -offset, -offset),
        };
        recursiveSubdivision(face, RESOLUTION, quads);

        face = new Vector3f[]{
                new Vector3f(offset, -offset, -offset),
                new Vector3f(offset, offset, -offset),
                new Vector3f(offset, offset, offset),
                new Vector3f(offset, -offset, offset),
        };
        recursiveSubdivision(face, RESOLUTION, quads);

        face = new Vector3f[]{
                new Vector3f(-offset, -offset, -offset),
                new Vector3f(offset, -offset, -offset),
                new Vector3f(offset, -offset, offset),
                new Vector3f(-offset, -offset, offset),
        };
        recursiveSubdivision(face, RESOLUTION, quads);

        face = new Vector3f[]{
                new Vector3f(-offset, offset, offset),
                new Vector3f(offset, offset, offset),
                new Vector3f(offset, offset, -offset),
                new Vector3f(-offset, offset, -offset),
        };
        recursiveSubdivision(face, RESOLUTION, quads);

        ModelUtil.transformQuads(quads, v -> v.mul(r));
        ModelUtil.transformQuads(quads, v -> v.add(center));
        return quads;
    }

    private List<Vector3f[]> recursiveSubdivision(Vector3f[] quad, int RESOLUTION, List<Vector3f[]> quads){
        if (RESOLUTION<=0){
            quads.add(quad);
        } else {
            Vector3f va = new Vector3f(quad[0]);
            va.add(quad[1]);
            va.normalize();

            Vector3f vb = new Vector3f(quad[0]);
            vb.add(quad[3]);
            vb.normalize();

            Vector3f vc =new Vector3f( quad[0]);
            vc.add(quad[2]);
            vc.add(quad[1]);
            vc.add(quad[3]);
            vc.normalize();

            Vector3f vd = new Vector3f(quad[2]);
            vd.add(quad[1]);
            vd.normalize();

            Vector3f ve = new Vector3f(quad[3]);
            ve.add(quad[2]);
            ve.normalize();

            recursiveSubdivision(new Vector3f[] {new Vector3f(quad[0]), new Vector3f(va), new Vector3f(vc), new Vector3f(vb)}, RESOLUTION-1, quads);
            recursiveSubdivision(new Vector3f[] {new Vector3f(va), new Vector3f(quad[1]), new Vector3f(vd), new Vector3f(vc)}, RESOLUTION-1, quads);
            recursiveSubdivision(new Vector3f[] {new Vector3f(vc), new Vector3f(vd), new Vector3f(quad[2]), new Vector3f(ve)}, RESOLUTION-1, quads);
            recursiveSubdivision(new Vector3f[] {new Vector3f(vb), new Vector3f(vc), new Vector3f(ve), new Vector3f(quad[3])}, RESOLUTION-1, quads);
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

    public static MoleculeModel deserialize(Reader reader) {

        Map<String, Pair<Atom, Vector3f>> positions = new HashMap<>();
        Map<Pair<String, String>, Bond> bondMap = new HashMap<>();

        JsonObject structure = JsonHelper.deserialize(reader).getAsJsonObject("structure");
        JsonArray atoms = structure.getAsJsonArray("atoms");
        for (JsonElement atom: atoms) {
            JsonObject atomJson = atom.getAsJsonObject();
            String key = atomJson.get("key").getAsString();
            Atom readAtom = Atom.getBySymbol(atomJson.get("atom").getAsString());
            JsonArray pos = atomJson.getAsJsonArray("position");
            Vector3f Vector3f = Vector3f.ZERO.copy();
            float scale_factor = 4.5f;
            int i = 0;
            for (JsonElement position: pos) {
                switch (i) {
                    case 0 -> {
                        Vector3f.add(position.getAsFloat() / scale_factor, 0, 0);
                        i++;
                    }
                    case 1 -> {
                        Vector3f.add(0, position.getAsFloat() / scale_factor, 0);
                        i++;
                    }
                    case 2 -> {
                        Vector3f.add(0, 0, position.getAsFloat() / scale_factor);
                        i++;
                    }
                }
            }
            positions.put(key, new Pair<>(readAtom, Vector3f.copy()));
            Vector3f.set(0,0,0);
        }
        JsonArray bonds = structure.getAsJsonArray("bonds");
        for (JsonElement bond: bonds) {
            MoleculeGraphJsonFormat.BondJson bondJson = new Gson().fromJson(bond.getAsJsonObject(), MoleculeGraphJsonFormat.BondJson.class);
            bondMap.put(new Pair<>(bondJson.from, bondJson.to), Bond.get(bondJson.bondOrder));
        }
        return new MoleculeModel(positions, bondMap);
    }

}
