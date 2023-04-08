package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.crafting.molecules.Atom;
import be.uantwerpen.minelabs.entity.BohrBlueprintEntity;
import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.util.NucleusState;
import be.uantwerpen.minelabs.util.NuclidesTable;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static be.uantwerpen.minelabs.util.NuclidesTable.calculateNrOfElectrons;
import static net.minecraft.client.gui.DrawableHelper.drawCenteredText;
import static net.minecraft.client.gui.DrawableHelper.drawTexture;

@Environment(EnvType.CLIENT)
public class BohrBlueprintEntityRenderer extends EntityRenderer<BohrBlueprintEntity> {
    private static final ItemStack PROTON = new ItemStack(Items.PROTON, 1);
    private static final ItemStack NEUTRON = new ItemStack(Items.NEUTRON, 1);
    private static final ItemStack ELECTRON = new ItemStack(Items.ELECTRON, 1);

    private static final List<Vec3f> NUCLEUS_COORDINATES = createIcosahedron();


    private final ItemRenderer itemRenderer;

    private static List<Vec3f> createIcosahedron() {
        float c = 2f * (float) Math.PI / 5f;
        List<Vec3f> icosahedron = new ArrayList<>();
        icosahedron.add(new Vec3f(0, 0, (float) Math.sqrt(5) / 2));
        for (int i = 0; i < 5; i++)
            icosahedron.add(new Vec3f((float) Math.cos((i) * c), (float) Math.sin((i) * c), 0.5f));
        for (int i = 0; i < 5; i++)
            icosahedron.add(new Vec3f((float) Math.cos((Math.PI / 5) + i * c), (float) Math.sin((Math.PI / 5) + i * c), -0.5f));
        icosahedron.add(new Vec3f(0, 0, (float) -Math.sqrt(5) / 2));

        return icosahedron;
    }

    public BohrBlueprintEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public Identifier getTexture(BohrBlueprintEntity entity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    @Override
    public void render(BohrBlueprintEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        int nP = entity.getProtons();
        int nN = entity.getNeutrons();
        int nE = entity.getElectrons();

        float time = entity.age + tickDelta;
        boolean stable = entity.isStable();

        matrices.push();
        matrices.translate(0, entity.getHeight() / 2f, 0f);

        renderNucleus(nP, nN, time, matrices, vertexConsumers, light);
        renderElectrons(nE, time, matrices, vertexConsumers, light);

        matrices.pop();
    }

    private void renderElectrons(int nE, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

    }

    private void renderNucleus(int nP, int nN, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

    }

//    /**
//     * Set up the matrices to render everything facing the player.
//     */
//    private void transformToFacePlayer(BohrBlueprintEntity entity, MatrixStack matrices) {
//        Vec3d pos = this.dispatcher.camera.getPos();
//        Vec3f entityToPlayer = new Vec3f(entity.getPos().add(0,0.75,0).relativize(pos));
//        entityToPlayer.normalize();
//        double pitch = Math.asin(-entityToPlayer.getY());
//        double yaw = Math.atan2(entityToPlayer.getX(), entityToPlayer.getZ());
//        entityToPlayer.cross(Vec3f.POSITIVE_Y);
//        entityToPlayer.normalize();
//        matrices.multiply(entityToPlayer.getRadialQuaternion((float)-pitch));
//        matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion((float) yaw));
//    }

}
