package be.uantwerpen.scicraft.renderer;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.block.entity.AnimatedChargedBlockEntity;
import be.uantwerpen.scicraft.block.entity.BohrBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;


@Environment(EnvType.CLIENT)
public class BohrBlockEntityRenderer<T extends BohrBlockEntity> implements BlockEntityRenderer<T> {

	private static ItemStack nucleus_stack = new ItemStack(Items.FIREWORK_STAR, 1); // minecraft Items firework star
	private static ItemStack proton_stack = new ItemStack(Blocks.PROTON, 1);
	private static ItemStack neutron_stack = new ItemStack(Blocks.NEUTRON, 1);
//	private static ItemStack electron_shell_stack = new ItemStack(Blocks.PROTON, 1);
	private static ItemStack electron_stack = new ItemStack(Blocks.ELECTRON, 1);

	private static final List<Vec3f> icosahedron = new ArrayList<>();
	static {
		for (int i = 1; i < 13; i++) {
			Vec3f punt1 = new Vec3f();
			if (i == 1) {
				punt1 = new Vec3f(0, 0, (float)Math.sqrt(5)/2);
			}
			else if (i > 1 && i < 7) {
				punt1 = new Vec3f((float)Math.cos((i-2)*(2*Math.PI)/5), (float)Math.sin((i-2)*(2*Math.PI)/5), 0.5f);
			}
			else if (i > 6 && i < 12) {
				double a = (Math.PI / 5) + (i - 7) * (2 * Math.PI) / 5;
				punt1 = new Vec3f((float)Math.cos(a), (float)Math.sin(a), -0.5f);
			}
			else if (i == 12) {
				punt1 = new Vec3f(0, 0, (float)-Math.sqrt(5)/2);
			}
			icosahedron.add(punt1);
		}
	}


    private Context context;

	public BohrBlockEntityRenderer(Context ctx) {
    	this.context = ctx;
    }

	@Override
	public void render(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		World world = blockEntity.getWorld();
		matrices.push();

		int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());

		double offset = Math.sin((blockEntity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;
//		matrices.translate(0.5, 1.25 + offset, 0.5);

		// origin
		matrices.translate(0.5f, 1.75f, 0.5f);
		matrices.scale(1.5f,1.5f,1.5f);

		PlayerEntity player = MinecraftClient.getInstance().player;
		assert player != null;
		Vec3f field = new Vec3f(
				(float)player.getX()-blockEntity.getPos().getX(),
				(float)player.getY()-blockEntity.getPos().getY(),
				(float)player.getZ()-blockEntity.getPos().getZ()
		);
		if(field.equals(Vec3f.ZERO)) {
			// default field should be north iso east.
			// positive x is east, so we want to rotate -90 degrees along the y-axis.
			matrices.multiply(Direction.UP.getUnitVector().getDegreesQuaternion(90));
		}else{
			/*
			 * This algorithm determines the normal vector of the plane described by the original orientation of the arrow (v) and the target direction (field).
			 * It then rotates around this vector with the angle theta between the two vectors to point the arrow in the direction of the field.
			 */
			// By default, the arrow points in positive x (EAST)
			Vec3f v = new Vec3f(1,0,0);

			// Compute theta with cosine formula.
			double theta = Math.acos(v.dot(field) / Math.sqrt(Math.pow(field.getX(), 2) + Math.pow(field.getY(), 2) + Math.pow(field.getZ(), 2)));

			if(theta == 0 || theta == Math.PI) {
				// When the two vectors are parallel, their cross product does not produce the normal vector of the plane.
				// Instead, we set in to one of the infinite valid normal vectors: positive Y.
				v = Direction.UP.getUnitVector();
			} else {
				v.cross(field);
				v.normalize();
			}
			matrices.multiply(v.getRadialQuaternion((float)theta));
		}
		Vec3f y_rotation = new Vec3f(0, 1, 0);
		matrices.multiply(y_rotation.getDegreesQuaternion(-90));

		// Rotate the item
//		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((blockEntity.getWorld().getTime() + tickDelta) * 4));

		//MinecraftClient.getInstance().getItemRenderer().renderItem(nucleus_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);

		int protonCount = blockEntity.getProtonCount();
		int neutronCount = blockEntity.getNeutronCount();
		int electronCount = blockEntity.getElectronCount();

		// variables for placing the particles (they get decreased)
		int nrOfprotonsLeft = protonCount;
		int nrOfneutronsLeft = neutronCount;
		int nrOfelectronsLeft = electronCount;

		/**
		 * rendering of the nucleus
		 */

		float startingOffsetScale = 15f;
//		if (0 < protonCount+neutronCount && protonCount+neutronCount < 12) {
//			float offsetScale = 15f;
////			boolean isProtonNext = true;
////			boolean isProtonAndNeutronLeft = true;
////			for (int i = 0; i < protonCount+neutronCount; i++) {
////				float offset_x = icosahedron.get(i).getX()/15f;
////				float offset_y = icosahedron.get(i).getY()/15f;
////				float offset_z = icosahedron.get(i).getZ()/15f;
////
////				matrices.translate(offset_x, offset_y, offset_z);
////				matrices.scale(0.2f, 0.2f, 0.2f);
////
////				if (nrOfprotonsLeft == 0) {
////					MinecraftClient.getInstance().getItemRenderer().renderItem(neutron_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
////					nrOfneutronsLeft -= 1;
////					isProtonAndNeutronLeft = false;
////				}
////				else if (nrOfneutronsLeft == 0) {
////					MinecraftClient.getInstance().getItemRenderer().renderItem(proton_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
////					nrOfprotonsLeft -= 1;
////					isProtonAndNeutronLeft = false;
////				}
////				if (isProtonAndNeutronLeft) {
////					if (isProtonNext) {
////						MinecraftClient.getInstance().getItemRenderer().renderItem(proton_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
////						isProtonNext = false;
////						nrOfprotonsLeft -= 1;
////					}
////					else {
////						MinecraftClient.getInstance().getItemRenderer().renderItem(neutron_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
////						isProtonNext = true;
////						nrOfneutronsLeft -= 1;
////					}
////				}
////
////				matrices.scale(5, 5, 5);
////				matrices.translate(-offset_x, -offset_y, -offset_z);
////			}
//		}
		if (protonCount+neutronCount >= 12) {
			startingOffsetScale = 11f;
		}

		boolean isProtonNext = true;
		boolean isProtonAndNeutronLeft = true;
		int particlesCounter = 0;
		float scaleOffset = 0f;
		int dec_index = 0;

		for (int i = 0; i < protonCount+neutronCount; i++) {

			if (particlesCounter == 12) {
				particlesCounter = 0;
				scaleOffset += 0.75f;
				dec_index += 12;
				matrices.multiply(Direction.UP.getUnitVector().getDegreesQuaternion(30));
			}

			float totalScale = startingOffsetScale-scaleOffset+scaleOffset/2.3f;
			float offset_x = icosahedron.get(i-dec_index).getX()/totalScale;
			float offset_y = icosahedron.get(i-dec_index).getY()/totalScale;
			float offset_z = icosahedron.get(i-dec_index).getZ()/totalScale;

			matrices.translate(offset_x, offset_y, offset_z);
			matrices.scale(0.2f, 0.2f, 0.2f);

			if (nrOfprotonsLeft == 0) {
				MinecraftClient.getInstance().getItemRenderer().renderItem(neutron_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
				nrOfneutronsLeft -= 1;
				isProtonAndNeutronLeft = false;
			}
			else if (nrOfneutronsLeft == 0) {
				MinecraftClient.getInstance().getItemRenderer().renderItem(proton_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
				nrOfprotonsLeft -= 1;
				isProtonAndNeutronLeft = false;
			}
			if (isProtonAndNeutronLeft) {
				if (isProtonNext) {
					MinecraftClient.getInstance().getItemRenderer().renderItem(proton_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
					isProtonNext = false;
					nrOfprotonsLeft -= 1;
				}
				else {
					MinecraftClient.getInstance().getItemRenderer().renderItem(neutron_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
					isProtonNext = true;
					nrOfneutronsLeft -= 1;
				}
			}

			matrices.scale(5, 5, 5);
			matrices.translate(-offset_x, -offset_y, -offset_z);

			particlesCounter++;
		}


		/**
		 * rendering of the electrons
		 */




		matrices.pop();
//		double offset = 0;
//		if (blockEntity.time != 0) {
//			double time_fraction = Math.max(0, Math.min(1, (blockEntity.getWorld().getTime() + tickDelta - blockEntity.time) / AnimatedChargedBlockEntity.time_move_ticks));
//			if (blockEntity.annihilation) {
//				offset = .5 * time_fraction * time_fraction;
//			} else {
//				offset = time_fraction < 0.5 ? 2 * time_fraction * time_fraction : 2 * time_fraction * (-time_fraction + 2) - 1;
//			}
//		}
//		if (!(blockEntity.annihilation && offset ==.5)) {
//			matrices.translate(blockEntity.movement_direction.getX() * offset, blockEntity.movement_direction.getY() * offset, blockEntity.movement_direction.getZ() * offset);
//			BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
//			blockRenderManager.getModelRenderer().render(
//					world,
//					blockRenderManager.getModel(blockEntity.render_state),
//					blockEntity.render_state,
//					blockEntity.getPos(),
//					matrices,
//					vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(blockEntity.render_state)),
//					false, net.minecraft.util.math.random.Random.create(),
//					blockEntity.render_state.getRenderingSeed(blockEntity.getPos()),
//					OverlayTexture.DEFAULT_UV);
//		}

	}

	/**
	 * Handles the scaling and stuff for the nucleus (and protons and neutrons).
	 */
	public void makeNucleus() {

	}

	/**
	 * Handles the scaling and stuff for the electron shells.
	 */
	public void makeElectronshells() {

	}

	/**
	 * Handles the scaling and stuff for the electrons.
	 */
	public void makeElectrons() {

	}

}


