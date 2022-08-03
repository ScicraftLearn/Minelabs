package be.uantwerpen.scicraft.event;

import be.uantwerpen.scicraft.block.BohrBlock;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface BohrBlockCallBack {
    Event<BohrBlockCallBack> EVENT = EventFactory.createArrayBacked(BohrBlockCallBack.class,
            (listeners) -> (playerEntity, bohrBlock) -> {
                for (BohrBlockCallBack listener : listeners) {
                    ActionResult result = listener.interact(playerEntity, bohrBlock);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(PlayerEntity player, BohrBlock bohrBlock);

}
