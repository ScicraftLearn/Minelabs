package be.minelabs.block.entity;

import be.minelabs.Minelabs;
import be.minelabs.item.Items;
import be.minelabs.block.Blocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;


public class BlockEntities {
    public static BlockEntityType<ChargedBlockEntity> ELECTRON_BLOCK_ENTITY;
    public static BlockEntityType<ChargedBlockEntity> POSTIRON_BLOCK_ENTITY;
    public static BlockEntityType<ChargedBlockEntity> PROTON_BLOCK_ENTITY;
    public static BlockEntityType<ChargedBlockEntity> NEUTRON_BLOCK_ENTITY;
    public static BlockEntityType<ChargedBlockEntity> ANTI_NEUTRON_BLOCK_ENTITY;
    public static BlockEntityType<ChargedBlockEntity> WEAK_BOSON_BLOCK_ENTITY;
    public static BlockEntityType<ChargedBlockEntity> ANTI_PROTON_BLOCK_ENTITY;
    public static BlockEntityType<ChargedBlockEntity> PION_MINUS_BLOCK_ENTITY;
    public static BlockEntityType<ChargedBlockEntity> PION_PLUS_BLOCK_ENTITY;
    public static BlockEntityType<ChargedBlockEntity> PION_NUL_BLOCK_ENTITY;
    public static BlockEntityType<AnimatedChargedBlockEntity> ANIMATED_CHARGED_BLOCK_ENTITY;
    public static BlockEntityType<ChargedPlaceholderBlockEntity> CHARGED_PLACEHOLDER_BLOCK_ENTITY;
    public static BlockEntityType<ChargedPointBlockEntity> CHARGED_POINT_BLOCK_ENTITY;

    public static BlockEntityType<LewisBlockEntity> LEWIS_BLOCK_ENTITY;
    public static BlockEntityType<IonicBlockEntity> IONIC_BLOCK_ENTITY;
    public static BlockEntityType<ElectricFieldSensorBlockEntity> ELECTRIC_FIELD_SENSOR;

    public static BlockEntityType<TimeFreezeBlockEntity> TIME_FREEZE_BLOCK_ENTITY;

    public static BlockEntityType<QuantumFieldBlockEntity> QUANTUM_FIELD_BLOCK_ENTITY;

    public static BlockEntityType<LabChestBlockEntity> LAB_CHEST_BLOCK_ENTITY;

    public static BlockEntityType<MologramBlockEntity> MOLOGRAM_BLOCK_ENTITY;


    static {
        ArrayList<ItemStack> weak_boson_drop = new ArrayList<>();
        weak_boson_drop.add(new ItemStack(Items.WEAK_BOSON));

        ArrayList<ItemStack> photons_drop = new ArrayList<>();
        photons_drop.add(new ItemStack(Items.PHOTON, 2));

        ArrayList<ItemStack> electron_drop = new ArrayList<>();
        electron_drop.add(new ItemStack(Items.ELECTRON));
        electron_drop.add(new ItemStack(Items.ANTINEUTRINO));

        PION_MINUS_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedBlockEntity(PION_MINUS_BLOCK_ENTITY, p, s, -1, Blocks.PION_PLUS, 44, weak_boson_drop, null), Blocks.PION_MINUS).build(null),
                "pion_minus_block_entity");

        PION_PLUS_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedBlockEntity(PION_PLUS_BLOCK_ENTITY, p, s, 1, Blocks.PION_MINUS, 44, weak_boson_drop, null), Blocks.PION_PLUS).build(null),
                "pion_plus_block_entity");

        PION_NUL_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p, s) -> new ChargedBlockEntity(PION_NUL_BLOCK_ENTITY, p, s, 0, Blocks.PION_NUL, 44, photons_drop, null), Blocks.PION_NUL).build(null),
                "pion_nul_block_entity");

    	ELECTRON_BLOCK_ENTITY = register(
    			FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedBlockEntity(ELECTRON_BLOCK_ENTITY, p, s, -1, Blocks.POSITRON, 0, null, null), Blocks.ELECTRON).build(null),
    			"electron_block_entity");

    	POSTIRON_BLOCK_ENTITY = register(
    			FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedBlockEntity(POSTIRON_BLOCK_ENTITY, p, s, 1, Blocks.ELECTRON, 0, null, null), Blocks.POSITRON).build(null),
    			"positron_block_entity");

        PROTON_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedBlockEntity(PROTON_BLOCK_ENTITY, p, s, 1, Blocks.ANTI_PROTON, 0, null, null), Blocks.PROTON).build(null),
                "proton_block_entity");

        ANTI_PROTON_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p, s) -> new ChargedBlockEntity(ANTI_PROTON_BLOCK_ENTITY, p, s, -1, Blocks.PROTON, 0, null, null), Blocks.ANTI_PROTON).build(null),
                "anti_proton_block_entity");

        NEUTRON_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedBlockEntity(NEUTRON_BLOCK_ENTITY, p, s, 0, Blocks.ANTI_NEUTRON, 361, weak_boson_drop, Blocks.PROTON), Blocks.NEUTRON).build(null),
                "neutron_block_entity");

        ANTI_NEUTRON_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedBlockEntity(ANTI_NEUTRON_BLOCK_ENTITY, p, s, 0, Blocks.NEUTRON, 361, weak_boson_drop, Blocks.ANTI_PROTON), Blocks.ANTI_NEUTRON).build(null),
                "anti_neutron_block_entity");

        WEAK_BOSON_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p,s) -> new ChargedBlockEntity(WEAK_BOSON_BLOCK_ENTITY, p, s, 0, null, 15, electron_drop, null), Blocks.WEAK_BOSON).build(null),
                "weak_boson_block_entity");

        ANIMATED_CHARGED_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p, s) -> new AnimatedChargedBlockEntity(ANIMATED_CHARGED_BLOCK_ENTITY, p, s), Blocks.ANIMATED_CHARGED).build(null),
                "animated_charged_block_entity");

        CHARGED_PLACEHOLDER_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p, s) -> new ChargedPlaceholderBlockEntity(CHARGED_PLACEHOLDER_BLOCK_ENTITY, p, s), Blocks.CHARGED_PLACEHOLDER).build(null),
                "charged_placeholder_block_entity");

        CHARGED_POINT_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p, s) -> new ChargedPointBlockEntity(p, s, 0, null, 0, null, null), Blocks.CHARGED_POINT_BLOCK).build(null),
                "charged_point_block_entity");

        IONIC_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Minelabs.MOD_ID, "ionic_block"),
                FabricBlockEntityTypeBuilder.create(IonicBlockEntity::new,
                        Blocks.IONIC_BLOCK).build(null));

        LEWIS_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create(LewisBlockEntity::new, Blocks.LEWIS_BLOCK).build(null), "lewis_block");


        ELECTRIC_FIELD_SENSOR = register(FabricBlockEntityTypeBuilder.create((p, s) -> new ElectricFieldSensorBlockEntity(ELECTRIC_FIELD_SENSOR, p, s), Blocks.ELECTRIC_FIELD_SENSOR_BLOCK).build(null), "electric_field_sensor");

        TIME_FREEZE_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create((p, s) -> new TimeFreezeBlockEntity(TIME_FREEZE_BLOCK_ENTITY, p, s), Blocks.TIME_FREEZE_BLOCK).build(null), "time_freeze_block_entity");

        QUANTUM_FIELD_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create(QuantumFieldBlockEntity::new,
                        Blocks.ELECTRON_QUANTUMFIELD, Blocks.GLUON_QUANTUMFIELD,
                        Blocks.NEUTRINO_QUANTUMFIELD, Blocks.DOWNQUARK_QUANTUMFIELD,
                        Blocks.PHOTON_QUANTUMFIELD, Blocks.WEAK_BOSON_QUANTUMFIELD,
                        Blocks.UPQUARK_QUANTUMFIELD).build(null), "quantum_field_entity");

        LAB_CHEST_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create(LabChestBlockEntity::new,
                        Blocks.LAB_CABIN, Blocks.LAB_DRAWER).build(null), "lab_chest_block_entity");

        MOLOGRAM_BLOCK_ENTITY = register(FabricBlockEntityTypeBuilder.create(MologramBlockEntity::new,
                Blocks.MOLOGRAM_BLOCK).build(null), "mologram_block_entity");
    }

    /**
     * Register a BlockEntity
     * <p>
     *
     * @param blockEntityType : BlockEntityType Object to register
     * @param identifier      : String name of the Item
     * @return {@link Block}
     */
    private static <T extends BlockEntity> BlockEntityType<T> register(BlockEntityType<T> blockEntityType, String identifier) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Minelabs.MOD_ID, identifier), blockEntityType);
    }

    /**
     * Main class method
     * Registers all BlocksEntities
     */
    public static void registerBlockEntities() {
        Minelabs.LOGGER.info("registering blockEntities");
    }
}
