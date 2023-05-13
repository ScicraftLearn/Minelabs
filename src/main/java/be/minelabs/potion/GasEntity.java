package be.minelabs.potion;

import be.minelabs.item.Items;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// TODO: Make "Fabric"-like
public class GasEntity extends PotionEntity { // TODO: better to extends from ThrownItemEntity, but the throw animation is buggy

    private final Molecule molecule;

    public GasEntity(World world, LivingEntity player, Molecule molecule) {
        super(world, player);
        this.molecule = molecule;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ERLENMEYER;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        molecule.react(this.world, this.getPos(), hitResult);
        if (!this.world.isClient) {
            this.world.syncWorldEvent(2002, this.getBlockPos(), molecule.getColor());
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isTouchingWater()) {
            // TODO: Sketchy, replace this
            BlockHitResult blockHitResult = new BlockHitResult(this.getPos(), Direction.UP, this.getBlockPos(), false);
            molecule.react(this.world, this.getPos(), blockHitResult);
//            this.kill();
        }
    }

//    @Override
//    protected void onBlockCollision(BlockState state) {
//        System.out.println(state.getBlock() == Blocks.WATER);
//    }
}
