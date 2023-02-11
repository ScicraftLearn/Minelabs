package be.uantwerpen.minelabs.potion.reactions;

import be.uantwerpen.minelabs.potion.GasEntity;
import be.uantwerpen.minelabs.potion.Molecule;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;

public abstract class Reaction {

    public void react(World world, Vec3d position, HitResult hitResult) {
        Objects.requireNonNull(world);
        Objects.requireNonNull(position);
        Objects.requireNonNull(hitResult);
        if (hitResult instanceof BlockHitResult blockHitResult)
            react(world, position.x, position.y, position.z, blockHitResult);
        else if(hitResult instanceof EntityHitResult entityHitResult)
            react(world, position.x, position.y, position.z, entityHitResult);
    }

    protected abstract void react(World world, double x, double y, double z, BlockHitResult hitResult);
    protected abstract void react(World world, double x, double y, double z, EntityHitResult hitResult);
}
