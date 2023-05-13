package be.minelabs.mixins;

import be.minelabs.block.Blocks;
import be.minelabs.block.GreenFire;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractFireBlock.class)
public class FireMixin {

    @Inject(method = "getState", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private static void isCopperBase(BlockView world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        BlockState blockState = world.getBlockState(pos.down());
        if (GreenFire.isCopperBase(blockState)) {
            cir.setReturnValue(Blocks.GREEN_FIRE.getDefaultState());
        }
    }
}
