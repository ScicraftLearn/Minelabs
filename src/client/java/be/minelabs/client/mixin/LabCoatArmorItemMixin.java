package be.minelabs.client.mixin;


import be.minelabs.Minelabs;
import be.minelabs.client.renderer.geo.LabCoatRenderer;
import be.minelabs.item.items.LabCoatArmorItem;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.client.RenderProvider;

import java.util.function.Consumer;

@Mixin(LabCoatArmorItem.class)
public class LabCoatArmorItemMixin{
    @Inject(method="createRenderer(Ljava/util/function/Consumer;)V", at=@At("HEAD"), remap = false)
    public void injectCreateRenderer(Consumer<Object> consumer, CallbackInfo info) {
        consumer.accept(new RenderProvider() {
            private final LabCoatRenderer renderer = new LabCoatRenderer();

            @Override
            @SuppressWarnings("unchecked")
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                //Minelabs.LOGGER.info("getting labcoat renderer");
                renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return (BipedEntityModel<LivingEntity>) renderer;
            }
        });
    }
}
