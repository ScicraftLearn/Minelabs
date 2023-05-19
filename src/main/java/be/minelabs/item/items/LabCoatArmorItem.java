package be.minelabs.item.items;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LabCoatArmorItem extends ArmorItem implements GeoAnimatable, GeoItem {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public LabCoatArmorItem(ArmorMaterial material, Type slot, Settings settings) {
        super(material, slot, settings);
    }

    private PlayState predicate(AnimationState state){
        state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }


    // All you need to do here is add your animation controllers to the
    // AnimationData
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        // implemented in Mixin in client
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }
}
