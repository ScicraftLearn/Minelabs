package be.uantwerpen.scicraft.mixins;

import be.uantwerpen.scicraft.Scicraft;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    @Shadow
    protected Identifier lootTableId;

    @Inject(method = "getLootTableId()Lnet/minecraft/util/Identifier;", at = @At("TAIL"), cancellable = true)
    private void getLootTableId(CallbackInfoReturnable<Identifier> ci) {
        Identifier oldIdentifier = this.lootTableId;
        if (Objects.equals(oldIdentifier.getNamespace(), "minecraft")) {
            // Add minecraft to the path, so we can put all the loot tables extending the minecraft ones in a separate folder
            Identifier identifier = new Identifier(Scicraft.MOD_ID, "lasertool/" + oldIdentifier.getPath());
            ci.setReturnValue(identifier);
        }
    }
}
