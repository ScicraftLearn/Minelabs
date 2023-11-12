package be.minelabs.client.mixin;

import be.minelabs.item.Items;
import net.minecraft.block.Block;
import net.minecraft.block.StainedGlassPaneBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Shadow
    private void renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices) {}

    @Inject(method="getModel", at=@At(value="HEAD"), cancellable = true)
    private void getModelInject(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed,
                                CallbackInfoReturnable<BakedModel> ci) {
        // Always yield the balloon item in hand -- will ensure GUI lightning to be correct
        if(stack.isOf(Items.BALLOON)) {
            ItemModels models = ((ItemRenderer)(Object)this).getModels();
            ci.setReturnValue(models.getModel(Items.BALLOON_IN_HAND));
        }
    }

    @Inject(method="renderItem", at=@At(value="INVOKE", target="Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V"),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderItemInject(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices,
                                  VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model,
                                  CallbackInfo ci, boolean bl, boolean bl2, RenderLayer renderLayer, VertexConsumer vertexConsumer) {
        // When the balloon is not in hand: obtain the other model
        if(stack.isOf(Items.BALLOON) && !bl) {
            // NOTE: might ignore enchanting... not tested
            matrices.pop(); // cancels previous setup

            matrices.push();
            ItemModels models = ((ItemRenderer)(Object)this).getModels();
            model = models.getModel(Items.BALLOON);
            model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
            matrices.translate(-0.5f, -0.5f, -0.5f);
            this.renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer);
            matrices.pop();

            ci.cancel();
        }
    }
}
