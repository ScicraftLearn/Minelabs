package be.uantwerpen.minelabs.mixins;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.util.Tags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    @Shadow
    protected Identifier lootTableId;

    @Inject(method = "getLootTableId()Lnet/minecraft/util/Identifier;", at = @At("TAIL"), cancellable = true)
    private void getLootTableId(CallbackInfoReturnable<Identifier> ci) {
        Identifier oldIdentifier = this.lootTableId;

        String block = oldIdentifier.getPath().substring(oldIdentifier.getPath().lastIndexOf("/") + 1);
        Identifier blockIdentifier = new Identifier(oldIdentifier.getNamespace(), block);

        if (Registry.BLOCK.get(blockIdentifier).getDefaultState().isIn(Tags.Blocks.LASERTOOL_MINEABLE)) {
            ci.setReturnValue(new Identifier(Minelabs.MOD_ID, "lasertool/" + oldIdentifier.getPath()));
        }
    }

    @Inject(method = "calcBlockBreakingDelta", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getHardness(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void calcBlockBreakingDelta(BlockState state, @NotNull PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        ItemStack stackInHand = player.getStackInHand(Hand.MAIN_HAND);

        if (stackInHand.isIn(Tags.Items.LASERTOOLS) && !state.isIn(Tags.Blocks.LASERTOOL_MINEABLE)) {
            cir.setReturnValue(0.0f);
        }
    }
}
