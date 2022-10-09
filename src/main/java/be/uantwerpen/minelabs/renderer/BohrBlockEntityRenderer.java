package be.uantwerpen.minelabs.renderer;

import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.block.entity.BohrBlockEntity;
import be.uantwerpen.minelabs.util.NuclidesTable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static be.uantwerpen.minelabs.util.NuclidesTable.calculateNrOfElectrons;


@Environment(EnvType.CLIENT)
public class BohrBlockEntityRenderer<T extends BohrBlockEntity> implements BlockEntityRenderer<T> {

	float startingOffsetScale = 15f; // the scaling offset we start with, for our icosahedron figure.

	private boolean shakeSwitch = true; // (shaking of atom)
	private int switchCounter = 0; // (shaking of atom) used to know when to 'shake'
	int switchCounterModulo = 10; // (shaking of atom) determines how fast the particles move back and forth (minimum 5)

	int implodeCounter = 0; // till 20
	boolean isImploding = false;

	private static final ItemStack proton_stack = new ItemStack(Blocks.PROTON, 1);
	private static final ItemStack neutron_stack = new ItemStack(Blocks.NEUTRON, 1);
	private static final ItemStack electron_stack = new ItemStack(Blocks.ELECTRON, 1);

	private static final List<Vec3f> icosahedron = new ArrayList<>(); // (icosahedron) figure for the nucleus
	static {
		// icosahedron points:
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
		if (blockEntity.isMaster() ) {

			matrices.push();

			int lightAbove = WorldRenderer.getLightmapCoordinates(Objects.requireNonNull(blockEntity.getWorld()), blockEntity.getPos().up());

			// origin
			switch (blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(Properties.HORIZONTAL_FACING)) {
				case NORTH -> {
					matrices.translate(1f, 1.75f, 1f);
				}
				case EAST -> {
					matrices.translate(0f, 1.75f, 1f);
				}
				case SOUTH -> {
					matrices.translate(0f, 1.75f, 0f);
				}
				case WEST -> {
					matrices.translate(1f, 1.75f, 0f);
				}
			}

			matrices.scale(1.5f, 1.5f, 1.5f);

			// for facing the player
			PlayerEntity player = MinecraftClient.getInstance().player;
			assert player != null;
			Vec3f field = new Vec3f(
					(float) player.getX() - blockEntity.getPos().getX(),
					(float) player.getY() - blockEntity.getPos().getY(),
					(float) player.getZ() - blockEntity.getPos().getZ()
			);
			if (field.equals(Vec3f.ZERO)) {
				// default field should be north iso east.
				// positive x is east, so we want to rotate -90 degrees along the y-axis.
				matrices.multiply(Direction.UP.getUnitVector().getDegreesQuaternion(90));
			} else {
				/*
				 * This algorithm determines the normal vector of the plane described by the original orientation of the arrow (v) and the target direction (field).
				 * It then rotates around this vector with the angle theta between the two vectors to point the arrow in the direction of the field.
				 */
				// By default, the arrow points in positive x (EAST)
				Vec3f v = new Vec3f(1, 0, 0);

				// Compute theta with cosine formula.
				double theta = Math.acos(v.dot(field) / Math.sqrt(Math.pow(field.getX(), 2) + Math.pow(field.getY(), 2) + Math.pow(field.getZ(), 2)));

				if (theta == 0 || theta == Math.PI) {
					// When the two vectors are parallel, their cross product does not produce the normal vector of the plane.
					// Instead, we set in to one of the infinite valid normal vectors: positive Y.
					v = Direction.UP.getUnitVector();
				} else {
					v.cross(field);
					v.normalize();
				}
				matrices.multiply(v.getRadialQuaternion((float) theta));
			}
			Vec3f y_rotation = new Vec3f(0, 1, 0);
			matrices.multiply(y_rotation.getDegreesQuaternion(-90));


			int protonCount = blockEntity.getProtonCount();
			int neutronCount = blockEntity.getNeutronCount();
			int electronCount = blockEntity.getElectronCount();

			/**
			 * rendering of the nucleus:
			 */
			makeNucleus(protonCount, neutronCount, electronCount, matrices, lightAbove, vertexConsumerProvider, blockEntity);

			/**
			 * rendering of the electrons:
			 */
			makeElectrons(electronCount, matrices, lightAbove, vertexConsumerProvider, blockEntity, tickDelta);


			matrices.pop();
		}

	}

	/**
	 * Handles the scaling and placement for the nucleus (protons and neutrons).
	 *
	 * Data members used: startingOffsetScale, shakeSwitch, icosahedron, neutron_stack, proton_stack, switchCounter, switchCounterModulo
	 *
	 * @param protonCount : amount of protons
	 * @param neutronCount : amount of neutrons
	 * @param electronCount : amount of electrons
	 * @param matrices : matrices
	 * @param lightAbove : used in renderItem function to avoid all blackness in the rendering above the block.
	 * @param vertexConsumerProvider : vertexConsumerProvider
	 * @param blockEntity : blockEntity
	 */
	public void makeNucleus(int protonCount, int neutronCount, int electronCount, MatrixStack matrices, int lightAbove, VertexConsumerProvider vertexConsumerProvider, T blockEntity) {

		int mass = protonCount+neutronCount;

		if (mass >= 12) {startingOffsetScale = 11f;}
		if (mass >= 120) {startingOffsetScale = 12f;}
		if (mass >= 180) {startingOffsetScale = 13f;}
		if (mass >= 240) {startingOffsetScale = 15f;}

		// variables for placing the particles (they get decreased)
		int nrOfprotonsLeft = protonCount;
		int nrOfneutronsLeft = neutronCount;
		// controls the shaking
		float shake = getShakingFactor(protonCount, neutronCount, electronCount);

		boolean isProtonNext = true; // true if a proton entity needs to be placed in the core next, false = neutron next.
		boolean isProtonAndNeutronLeft = true; // true if both protons and neutrons still need to be placed
		int particlesCounter = 0; // used to count to 12 to restart (increase) the icosahedron scaleOffset.

		// each time we reach a multiple of 12, this value gets increased and used
		// in the function to calculate the total scale factor for our current icosahedron figure.
		float scaleOffset = 0f;
		int dec_index = 0; // variable to stay inside the list indexes of the icosahedron points.

		if (shakeSwitch) {
			shakeSwitch = false;
			shake = -shake;
		}
		else {
			shakeSwitch = true;
		}

//		if (!isImploding) {
//			isImploding = (remaining < 1+0.1 && remaining > 1-0.1);
//		}
//		else {
//			if (remaining < 0+0.05) {
//				isImploding = false;
//				implodeCounter = 0;
//			}
//		}

		for (int i = 0; i < mass; i++) {

			float scaleFactor = 2.5f; // lower value => closer to core origin

			if (mass > 50) {
				scaleFactor = 1.75f;
			}
			if (particlesCounter == 12) {
				particlesCounter = 0; // gets increased with one at end of for loop.
				if (mass < 36) {
					scaleOffset += 2.5f;
				}
				else {
					scaleOffset += 0.75f;
				}
				dec_index += 12;
//				if (isImploding) {
//					scaleOffset -= 0.5f*implodeCounter;
//				}
			}

			// calculating the x,y,z offsets to place the protons/neutrons on the icosahedron outer points.
			float totalScale = startingOffsetScale-scaleOffset+scaleOffset/scaleFactor;
			float offset_x = icosahedron.get(i-dec_index).getX()/totalScale;
			float offset_y = icosahedron.get(i-dec_index).getY()/totalScale;
			float offset_z = icosahedron.get(i-dec_index).getZ()/totalScale;

			if (dec_index > 12) {
				float rotateXAngle = (float)Math.PI*(0.125f*((dec_index/12) %4));
				ArrayList<Float> new_y_z = rotateAroundXAxis(offset_y, offset_z, rotateXAngle);
				offset_y = new_y_z.get(0);
				offset_z = new_y_z.get(1);
//				float rotateYAngle = (float)Math.PI*(0.75f*((dec_index/12) %4));
//				ArrayList<Float> new_x_z = rotateAroundYAxis(offset_x, offset_z, rotateYAngle);
//				offset_x = new_x_z.get(0);
//				offset_z = new_x_z.get(1);
			}

			matrices.translate(offset_x, offset_y+shake, offset_z);
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
			matrices.translate(-offset_x, -offset_y-shake, -offset_z);

			particlesCounter++;
		}
		switchCounter++;
//		if (isImploding) {
//			implodeCounter++;
//		}
	}

	/**
	 * Handles the "rendering" of the electrons shells themselves.
	 */
	public void makeElectronshells() {
		// may be implemented and used, but it looks fine with how it is now.
	}

	/**
	 * Handles the scaling and spinning of the electrons.
	 *
	 * @param electronCount : amount of electrons in the borhblock
	 * @param matrices : matrices
	 * @param lightAbove : used in renderItem function to avoid all blackness in the rendering above the block.
	 * @param vertexConsumerProvider : vertexConsumerProvider
	 * @param blockEntity : blockEntity
	 * @param tickDelta : tickDelta
	 */
	public void makeElectrons(int electronCount, MatrixStack matrices, int lightAbove, VertexConsumerProvider vertexConsumerProvider, T blockEntity, float tickDelta) {

		// for the electron-shell distribution, check the NuclidesTable class static declaration/definition.

		int currentShell = 1;
		int electronCounter = 0;
		for (int el = 0; el < electronCount; el++) {

			int currentNrOfElectrons = calculateNrOfElectrons(currentShell);
			if (electronCounter == currentNrOfElectrons) {
				currentShell++;
				electronCounter = 0;
			}

			// evenly distribution of electrons around core. Used for the electron point calculation.
			int electronsOnCurShell = calcPlaceableElectronsOnShell(electronCount, currentShell, currentNrOfElectrons);

			ArrayList<Float> point = calculateElectronPoint(currentShell, blockEntity, tickDelta, electronsOnCurShell, electronCounter);
			float x = point.get(0);
			float y = point.get(1);
			float z = point.get(2);

			matrices.translate(x, y, z);
			matrices.scale(0.1f, 0.1f, 0.1f);

			MinecraftClient.getInstance().getItemRenderer().renderItem(electron_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);

			matrices.scale(10, 10, 10);
			matrices.translate(-x, -y, -z);

			electronCounter++;
		}

		// example for drawing of the electron shells, this method is laggy though
//		currentShell = 1;
//		electronCounter = 0;
//		for (int el = 0; el < electronCount; el++) {
//
//			int currentNrOfElectrons = calculateNrOfElectrons(currentShell);
//			if (electronCounter == currentNrOfElectrons) {
//				currentShell++;
//				electronCounter = 0;
//			}
//
//			for (int i = 0; i < 120; i++) {
//				float speedMultiplier = 40+20*(currentShell-1);
//				float radiusMultiplier = 0.1f*(currentShell-1); // multiplier for how much further each new shell is from the nucleus
//
//				float speed = (float)(2*Math.PI)/speedMultiplier; // how fast the electrons rotate
//				float radius = 0.4f+radiusMultiplier; // distance from core, used to calculate the points
//				float angle = (float)((2*Math.PI/(120)))*(i);
//
//				float x = (float)Math.cos(angle)*radius;
//				float y = (float)Math.sin(angle)*radius;
//				float z = (float)Math.sin(angle)*radius*Math.min(currentShell-1, 1); // 0 on first shell, z on every other shell.
//
//				if (currentShell != 1) {
//					float rotateAngle = (float)Math.PI/(2f*(currentShell-1));
//					if (currentShell > 5) {
//						rotateAngle = (float)Math.PI/(8f*(currentShell-1));
//					}
//					ArrayList<Float> new_y_z = rotateAroundXAxis(y, z, rotateAngle);
//					y = new_y_z.get(0);
//					z = new_y_z.get(1);
//				}
//				matrices.translate(x, y, z);
//				matrices.scale(0.02f, 0.02f, 0.02f);
//				MinecraftClient.getInstance().getItemRenderer().renderItem(electron_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
//				matrices.scale(50, 50, 50);
//				matrices.translate(-x, -y, -z);
//			}
//
//
//			electronCounter++;
//		}

	}

	/**
	 * Calculates how much the nucleus should shake based on the stability deviation (from the black line) (this depends on halflife now)
	 *
	 * @param protonCount : amount of protons in the core
	 * @param neutronCount : amount of neutrons in the core
	 * @param electronCount : amount of electrons around the core
	 * @return : (float) shake factor (range = [0.01, 0.04])
	 */
	public float getShakingFactor(int protonCount, int neutronCount, int electronCount) {
		float shakeMultiplier = NuclidesTable.getStabilityDeviation(protonCount, neutronCount, electronCount);
		float shake = 0f;
		boolean isStable = NuclidesTable.isStable(protonCount, neutronCount, electronCount);
		if (shakeMultiplier == -1) { // if the amount of protons and amount of neutrons don't represent an atom.
			shakeMultiplier = 0.04f; // we set it to the hardest shaking
		}
		if (!isStable) {
			if (switchCounter%switchCounterModulo != 0) {
				shake = 0.01f+(float)Math.min(shakeMultiplier, 0.05); // [0.01 ; 0.05]
			}
		}
		return shake;
	}

	/**
	 * rotates y and z around x-axis
	 *
	 * @param y : y-coordinate
	 * @param z : z-coordinate
	 * @param angle : rotate angle
	 * @return : array of two elements: new y and z
	 */
	public ArrayList<Float> rotateAroundXAxis(float y, float z, float angle) {
		y = y*(float)Math.cos(angle)-z*(float)Math.sin(angle);
		z = z*(float)Math.cos(angle)+y*(float)Math.sin(angle);
		return new ArrayList<>(Arrays.asList(y, z));
	}

	/**
	 * rotates x and z around x-axis
	 *
	 * @param x : x-coordinate
	 * @param z : z-coordinate
	 * @param angle : rotate angle
	 * @return : array of two elements: new x and z
	 */
	public ArrayList<Float> rotateAroundYAxis(float x, float z, float angle) {
		x = x*(float)Math.cos(angle)-z*(float)Math.sin(angle);
		z = z*(float)Math.cos(angle)+x*(float)Math.sin(angle);
		return new ArrayList<>(Arrays.asList(x, z));
	}

	/**
	 * Calculates current total amount of electrons to be placed (outermost shell)
	 *
	 * @param electronCount : electron counter
	 * @param currentShell : integer value for which shell we are on (starts with 1)
	 * @param currentNrOfElectrons : amount of electrons on the current shell
	 * @return :
	 */
	public int calcPlaceableElectronsOnShell(int electronCount, int currentShell, int currentNrOfElectrons) {
		int	cur_e = electronCount;
		for (int i = 1; i < currentShell; i++) {
			cur_e -= calculateNrOfElectrons(i);
		}
		return Math.min(cur_e, currentNrOfElectrons);
	}

	/**
	 * Calculates the x,y and z coordinate for the electron
	 *
	 * @param currentShell : integer value for which shell we are on (starts with 1)
	 * @param blockEntity : blockEntity
	 * @param tickDelta : tickDelta
	 * @param electronsOnCurShell : amount of electrons on the current shell
	 * @param electronCounter : electron counter
	 * @return : array of three elements [x, y, z] for our point
	 */
	public ArrayList<Float> calculateElectronPoint(int currentShell, T blockEntity, float tickDelta, int electronsOnCurShell, int electronCounter) {

		// multiplier for how fast the electrons will spin around, the greater this value, the slower it will be.
		float speedMultiplier = 40+20*(currentShell-1);
		float radiusMultiplier = 0.1f*(currentShell-1); // multiplier for how much further each new shell is from the nucleus

		float speed = (float)(2*Math.PI)/speedMultiplier; // how fast the electrons rotate
		float radius = 0.4f+radiusMultiplier; // distance from core, used to calculate the points
		float angle = speed*(blockEntity.getWorld().getTime()+tickDelta) + (float)((2*Math.PI/(electronsOnCurShell)))*(electronCounter);

		float x = (float)Math.cos(angle)*radius;
		float y = (float)Math.sin(angle)*radius;
		float z = (float)Math.sin(angle)*radius*Math.min(currentShell-1, 1); // 0 on first shell, z on every other shell.

		if (currentShell != 1) {
			float rotateAngle = (float)Math.PI/(2f*(currentShell-1));
			if (currentShell > 5) {
				rotateAngle = (float)Math.PI/(8f*(currentShell-1));
			}
			ArrayList<Float> new_y_z = rotateAroundXAxis(y, z, rotateAngle);
			y = new_y_z.get(0);
			z = new_y_z.get(1);
		}

		return new ArrayList<>(Arrays.asList(x, y, z));
	}

}


