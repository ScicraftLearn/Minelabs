package be.minelabs.item;

import be.minelabs.Minelabs;
import be.minelabs.entity.ChargedEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class ChargedItem extends Item {

    private final int charge;

    public ChargedItem(Settings settings, int charge) {
        super(settings);
        this.charge = charge;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient) {
            ChargedEntity entity = new ChargedEntity(context.getWorld(), context.getBlockPos().up(), this.charge);
            context.getWorld().spawnEntity(entity);
        }
        return super.useOnBlock(context);
    }
}
