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

import java.util.*;

import static be.uantwerpen.scicraft.util.NuclidesTable.calculateNrOfElectrons;


@Environment(EnvType.CLIENT)
public class BohrBlockEntityRenderer<T extends BohrBlockEntity> implements BlockEntityRenderer<T> {

	private static final ItemStack proton_stack = new ItemStack(Blocks.PROTON, 1);
	private static final ItemStack neutron_stack = new ItemStack(Blocks.NEUTRON, 1);
//	private static ItemStack electron_shell_stack = new ItemStack(Blocks.PROTON, 1); // drawing of the electron shells
	private static final ItemStack electron_stack = new ItemStack(Blocks.ELECTRON, 1);

	private static final List<Vec3f> icosahedron = new ArrayList<>(); // (icosahedron) figure for the nucleus
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

		/**
		 * rendering of the nucleus:
		 */
		makeNucleus(protonCount, neutronCount, matrices, lightAbove, vertexConsumerProvider);

		/**
		 * rendering of the electrons:
		 */
		makeElectrons(electronCount, matrices, lightAbove, vertexConsumerProvider, blockEntity, tickDelta);



		matrices.pop();

	}

	/**
	 * Handles the scaling and stuff for the nucleus (and protons and neutrons).
	 *
	 * @param protonCount : amount of protons
	 * @param neutronCount : amount of neutrons
	 * @param matrices : matrices
	 * @param lightAbove : used in renderItem function to avoid all blackness in the rendering above the block.
	 * @param vertexConsumerProvider : vertexConsumerProvider
	 */
	public void makeNucleus(int protonCount, int neutronCount, MatrixStack matrices, int lightAbove, VertexConsumerProvider vertexConsumerProvider) {

		// variables for placing the particles (they get decreased)
		int nrOfprotonsLeft = protonCount;
		int nrOfneutronsLeft = neutronCount;

		float startingOffsetScale = 15f; // the scaling offset we start with, for our icosahedron figure.
		if (protonCount+neutronCount >= 12) {
			startingOffsetScale = 11f;
		}
		boolean isProtonNext = true; // true if a proton entity needs to be placed in the core next, false = neutron next.
		boolean isProtonAndNeutronLeft = true; // true if both protons and neutrons still need to be placed
		int particlesCounter = 0; // used to count to 12 to restart (increase) the icosahedron scaleOffset.

		// each time we pass a multiple of 12, this value gets increased and used
		// in the function to calculate the total scale factor for our current icosahedron figure.
		float scaleOffset = 0f;
		int dec_index = 0; // variable to stay inside the list indexes of the icosahedron points.

		for (int i = 0; i < protonCount+neutronCount; i++) {

			if (particlesCounter == 12) {
				particlesCounter = 0; // gets increased with one at end of for loop.
				scaleOffset += 0.75f;
				dec_index += 12;
				matrices.multiply(Direction.UP.getUnitVector().getDegreesQuaternion(30));
			}

			// calculating the x,y,z offsets to place the protons/neutrons on the icosahedron outer points.
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

	}

	/**
	 * Handles the scaling and stuff for the electron shells.
	 */
	public void makeElectronshells() {

	}

	/**
	 * Handles the scaling and stuff for the electrons.
	 *
	 * @param electronCount
	 * @param matrices
	 * @param lightAbove
	 * @param vertexConsumerProvider
	 * @param blockEntity
	 * @param tickDelta
	 */
	public void makeElectrons(int electronCount, MatrixStack matrices, int lightAbove, VertexConsumerProvider vertexConsumerProvider, T blockEntity, float tickDelta) {

//		float angle = 0;
//		float speed = (float)(2*Math.PI)/40;
//		float radius = 0.5f;
//
//		angle += speed*(blockEntity.getWorld().getTime() + tickDelta);
//		float x = (float)Math.cos(angle)*radius;
//		float y = (float)Math.sin(angle)*radius;
//
//
//		matrices.translate(0+x, 0+y, 0);
//		matrices.scale(0.1f, 0.1f, 0.1f);
//
//		MinecraftClient.getInstance().getItemRenderer().renderItem(electron_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
//
//		matrices.scale(10, 10, 10);
//		matrices.translate(0-x, 0-y, 0);
//
//		angle = 0;
//		speed = (float)(2*Math.PI)/45;
//		radius = 0.5f;
//
//		angle += speed*(blockEntity.getWorld().getTime() + tickDelta)+Math.PI;
//		x = (float)Math.cos(angle)*radius;
//		y = (float)Math.sin(angle)*radius;
//
//		matrices.translate(x, 0, y);
//		matrices.scale(0.1f, 0.1f, 0.1f);
//
//		MinecraftClient.getInstance().getItemRenderer().renderItem(electron_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
//
//		matrices.scale(10, 10, 10);
//		matrices.translate(0-x, 0-y, 0);
//
//		Map<Integer, Integer> shells = new HashMap<>();
//
//		shells.put(1, 2);
//		shells.put(2, 8);
//		shells.put(3, 18);
//		shells.put(4, 32);

		int currentShell = 1;
		int electronCounter = 0;
		for (int el = 0; el < electronCount; el++) {

			int currentNrOfElectrons = calculateNrOfElectrons(currentShell);
			if (electronCounter == currentNrOfElectrons) {
				currentShell++;
				electronCounter = 0;
			}

			float speedMultiplier = 40+20*(currentShell-1);
			float radiusMultiplier = 0.1f*(currentShell-1);

			float speed = (float)(2*Math.PI)/speedMultiplier;
			float radius = 0.4f+radiusMultiplier;
			float angle = speed*(blockEntity.getWorld().getTime()+tickDelta) + (float)((2*Math.PI/currentNrOfElectrons)*(electronCounter));

			float x = (float)Math.cos(angle)*radius;
			float y = (float)Math.sin(angle)*radius;
			float z = (float)Math.sin(angle)*radius*Math.min(currentShell-1, 1);

			if (currentShell != 1) {
				float rotateAngle = (float)Math.PI/(2f*(currentShell-1));
//				if (currentShell == 2) {
//					rotateAngle = (float)Math.PI/(2f);
//				}
//				else if (currentShell == 3) {
//					rotateAngle = (float)Math.PI/(6f);
//				}
				ArrayList<Float> y_z = rotateAroundXAxis(y, z, rotateAngle);
				y = y_z.get(0);
				z = y_z.get(1);
			}

			matrices.translate(x, y, z);
			matrices.scale(0.1f, 0.1f, 0.1f);

			MinecraftClient.getInstance().getItemRenderer().renderItem(electron_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);

			matrices.scale(10, 10, 10);
			matrices.translate(-x, -y, -z);

			electronCounter++;
		}

	}

	public ArrayList<Float> rotateAroundXAxis(float y, float z, float angle) {
		y = y*(float)Math.cos(angle)-z*(float)Math.sin(angle);
		z = z*(float)Math.cos(angle)+y*(float)Math.sin(angle);
		return new ArrayList<>(Arrays.asList(y, z));
	}

	public ArrayList<Float> rotateAroundYAxis(float x, float z, float angle) {
		x = x*(float)Math.cos(angle)-z*(float)Math.sin(angle);
		z = z*(float)Math.cos(angle)+x*(float)Math.sin(angle);
		return new ArrayList<>(Arrays.asList(x, z));
	}

}


