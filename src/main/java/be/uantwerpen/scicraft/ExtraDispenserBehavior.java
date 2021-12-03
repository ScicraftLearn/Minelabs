package be.uantwerpen.scicraft;

import be.uantwerpen.scicraft.entity.ElectronEntity;
import be.uantwerpen.scicraft.entity.NeutronEntity;
import be.uantwerpen.scicraft.entity.ProtonEntity;
import be.uantwerpen.scicraft.item.Items;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class ExtraDispenserBehavior {
    /**
     * Main class method
     * Registers all dispenser behaviors
     */
    public static void registerBehaviors() {
        /**
         * Register dispenser behavior for shooting out electrons
         * Implements abstract class ProjectileDispenserBehavior to create the correct entity
         *
         * The entity is shot up slight as defined in dispenseSilently in ProjectileDispenserBehavior:
         * direction.getOffsetY() + 0.1F
         */
        DispenserBlock.registerBehavior(Items.ELECTRON_ITEM, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return Util.make(new ElectronEntity(world, position.getX(), position.getY(), position.getZ()), (electronEntity) -> {
                    electronEntity.setItem(stack);
                });
            }
        });

        /**
         * Register dispenser behavior for shooting out protons
         * Implements abstract class ProjectileDispenserBehavior to create the correct entity
         *
         * The entity is shot up slight as defined in dispenseSilently in ProjectileDispenserBehavior:
         * direction.getOffsetY() + 0.1F
         */
        DispenserBlock.registerBehavior(Items.PROTON_ITEM, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return Util.make(new ProtonEntity(world, position.getX(), position.getY(), position.getZ()), (protonEntity) -> {
                    protonEntity.setItem(stack);
                });
            }
        });

        /**
         * Register dispenser behavior for shooting out neutrons
         * Implements abstract class ProjectileDispenserBehavior to create the correct entity
         *
         * The entity is shot up slight as defined in dispenseSilently in ProjectileDispenserBehavior:
         * direction.getOffsetY() + 0.1F
         */
        DispenserBlock.registerBehavior(Items.NEUTRON_ITEM, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return Util.make(new NeutronEntity(world, position.getX(), position.getY(), position.getZ()), (neutronEntity) -> {
                    neutronEntity.setItem(stack);
                });
            }
        });
    }
}
