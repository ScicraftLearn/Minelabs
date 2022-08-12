package be.uantwerpen.scicraft.mixins;

import be.uantwerpen.scicraft.Scicraft;
import be.uantwerpen.scicraft.util.Tags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    @Shadow
    protected Identifier lootTableId;

    @Inject(method = "getLootTableId()Lnet/minecraft/util/Identifier;", at = @At("TAIL"), cancellable = true)
    private void getLootTableId(CallbackInfoReturnable<Identifier> ci) {
        Identifier oldIdentifier = this.lootTableId;
        if (Objects.equals(oldIdentifier.getNamespace(), "minecraft")) {
            Identifier identifier = new Identifier(Scicraft.MOD_ID, oldIdentifier.getPath());
            ci.setReturnValue(identifier);
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
