package be.minelabs.item.items;

import be.minelabs.Minelabs;
import be.minelabs.entity.ChargedEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class ChargedItem extends Item {

    private final int charge;

    private final float mass;

    public ChargedItem(Settings settings, int charge, float mass) {
        super(settings);
        this.charge = charge;
        this.mass = mass;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient) {
            ChargedEntity entity = new ChargedEntity(context.getWorld(), context.getBlockPos().up(), this.charge, this.mass);
            context.getWorld().spawnEntity(entity);
        }
        return super.useOnBlock(context);
    }
}
