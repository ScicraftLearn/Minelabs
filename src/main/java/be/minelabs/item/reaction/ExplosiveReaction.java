package be.minelabs.item.reaction;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;


public class ExplosiveReaction extends Reaction {

    private final int power;
    private final boolean flammable;
    private final boolean pyrophoric;

    public ExplosiveReaction(int power, boolean flammable, boolean pyrophoric) {
        this.power = power;
        this.flammable = flammable;
        this.pyrophoric = pyrophoric;
    }

    @Override
    protected void react(World world, Vec3d sourcePos) {
        GameRules gameRules = world.getGameRules();
        if (!gameRules.getBoolean(GameRules.DO_MOB_GRIEFING))
            return;
        if (this.pyrophoric || Utils.isFlameNearby(world, sourcePos, power))
            world.createExplosion(null, sourcePos.x, sourcePos.y, sourcePos.z,
                    power, flammable && gameRules.getBoolean(GameRules.DO_FIRE_TICK), World.ExplosionSourceType.BLOCK);

    }

    @Override
    public void react(LivingEntity entity) {

    }

    public Text getTooltipText() {
        return getTooltipText("explosive").formatted(Formatting.RED);
    }
}
