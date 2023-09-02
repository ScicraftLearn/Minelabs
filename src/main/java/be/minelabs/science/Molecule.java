package be.minelabs.science;

import be.minelabs.item.reaction.*;
import be.minelabs.util.Tags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public enum Molecule {

    // Gasses
    O2("O2", 0xAFAFAF, List.of()), // TODO: oxiderend
    N2("N2", 0xAFAFAF, List.of(new ExtinguishReaction(3))),
    CH4("CH4", 0xAFAFAF, List.of(new FlammableReaction(20 * 5, 3, false))),
    H2("H2", 0xAFAFAF, List.of(new ExplosiveReaction(1, false, false))),
    NO("NO", 0xAFAFAF, List.of(new PoisonousReaction(3, 20*5, 1))),
    NO2("NO2", 0x991c00, List.of(new PoisonousReaction(3, 20*5, 1))),
    CL2("CL2", 0xE8F48C, List.of(new PoisonousReaction(3, 20*5, 2))), // TODO: oxiderend
    CO2("CO2", 0xAFAFAF, List.of(new ExtinguishReaction(3))),
    CO("CO", 0xAFAFAF, List.of(new PoisonousReaction(3, 20*5, 1))),
    NH3("NH3", 0xAFAFAF, List.of(
            new DissolveReaction(),
            new PoisonousReaction(3, 20*5, 1)
    )),
    N2O("N2O", 0xAFAFAF, List.of()), // TODO: lachgas, oxiderend
    HCL("HCl", 0xAFAFAF, List.of(
            new CorrosiveReaction(3).addToBlackList(Tags.Blocks.HCL_BLACKLIST)
    )),
    HE("He", 0xAFAFAF, List.of()), // TODO: verandering stem zoals lachgas
    NE("Ne", 0xAFAFAF, List.of()),
    AR("Ar", 0xAFAFAF, List.of()),
    CL2O("Cl2O", 0xAFAFAF, List.of(
            new CorrosiveReaction(3).addToBlackList(Tags.Blocks.HCL_BLACKLIST),
            new ExplosiveReaction(1, false, false),
            new PoisonousReaction(3, 20*5, 3)
    )),
    H2CO3("H2CO3", 0xAFAFAF, List.of()),
    CH4S("CH4S", 0xAFAFAF, List.of(
            new FlammableReaction(20*5, 3, false),
            new PoisonousReaction(3, 20*5, 3)
    )),
    CH2O("CH2O", 0xAFAFAF, List.of(
            new PoisonousReaction(3, 20*5, 1)
    )),
    BH3("BH3", 0xFFFFFF, List.of(
            new FlammableReaction(20 * 5, 3, true),
            new PoisonousReaction(3, 20*5, 1)
    )),
    HF("HF", 0xFFFFFF, List.of(
            new PoisonousReaction(3, 20*5, 3),
            new CorrosiveReaction(3)
    )),
    SIH4("SiH4", 0xFFFFFF, List.of(
            new FlammableReaction(20 * 5, 3, true)
    )),
    PH3("PH3", 0xFFFFFF, List.of(
            new ExplosiveReaction(1, true, false),
            new PoisonousReaction(3, 20*5, 3),
            new CorrosiveReaction(3).addToBlackList(Tags.Blocks.HCL_BLACKLIST)
    )),
    H2S("H2S", 0xFFFFFF, List.of(
            new ExplosiveReaction(1, true, false),
            new PoisonousReaction(3, 20*5, 2)
    )),
    CF4("CF4", 0xFFFFFF, List.of()),
    BF3("BF3", 0xFFFFFF, List.of(
            new PoisonousReaction(3, 20*5, 2),
            new CorrosiveReaction(3)
    )),
    BCL3("BCl3", 0xFFFFFF, List.of(
            new PoisonousReaction(3, 20*5, 2),
            new CorrosiveReaction(3)
    )),
    SO2("SO2", 0xFFFFFF, List.of(
            new PoisonousReaction(3, 20*5, 1),
            new CorrosiveReaction(3)
    )),
    CLF("ClF", 0xFFFFFF, List.of(
            new CorrosiveReaction(3).addToWhiteList(Tags.Blocks.HCL_BLACKLIST)
    )),
    F2("F2", 0xFFFFFF, List.of(
            new PoisonousReaction(3, 20*5, 3),
            new CorrosiveReaction(3)
    )),

    // Fluids
    HNO3("HNO3", 0xFFCC33, List.of(
            new CorrosiveReaction(3)
    )),
    H2O("H2O", 0x3495eb, List.of(
            new ExtinguishReaction(3))),
    NCL3("NCl3", 0xe8dc5a, List.of(
            new ExplosiveReaction(1, false, true)
    )),
    CS2("CS2", 0xAFAFAF, List.of(
            new FlammableReaction(20*5, 3, false)
    )),
    CCL4("CCl4", 0xAFAFAF, List.of(
            new PoisonousReaction(3, 20*5, 1)
    )),
    PCL3("PCl3", 0xAFAFAF, List.of(
            new PoisonousReaction(3, 20*5, 3),
            new CorrosiveReaction(3).addToBlackList(Tags.Blocks.HCL_BLACKLIST)
    )),
    SCL2("SCl2", 0xAFAFAF, List.of(
            new PoisonousReaction(3, 20*5, 3),
            new CorrosiveReaction(3).addToBlackList(Tags.Blocks.HCL_BLACKLIST)
    )),
    HCN("HCN", 0xCCCCFF, List.of(
            new PoisonousReaction(3, 20*5, 3),
            new FlammableReaction(20*5, 3, false)
    )),
    CH4O("CH4O", 0xAFAFAF, List.of(
            new FlammableReaction(20*5, 3, false),
            new PoisonousReaction(3, 20*5, 1)
    )),
    SICL4("SiCl4", 0xAFAFAF, List.of(
            new PoisonousReaction(3, 20*5, 3),
            new CorrosiveReaction(3).addToBlackList(Tags.Blocks.HCL_BLACKLIST)
    )),
    C2H6O("C2H6O", 0xAFAFAF, List.of(
            new FlammableReaction(20*5, 3, false)));


    private final int color;
    private final String name;
    private final List<Reaction> reactions;

    Molecule(String name, int color, List<Reaction> reactions) {
        Objects.requireNonNull(name);
        assert !name.isEmpty();
        this.name = name;
        this.color = color;
        this.reactions = reactions;
    }

    public void react(World world, Vec3d position, HitResult hitResult) {
        Objects.requireNonNull(world);
        Objects.requireNonNull(position);
        Objects.requireNonNull(hitResult);
        reactions.forEach(reaction -> reaction.react(world, position, hitResult));
    }

    public void react(LivingEntity entity) {
        Objects.requireNonNull(entity);
        reactions.forEach(reaction -> reaction.react(entity));
    }

    public int getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name;
    }

    public void makeTooltip(List<Text> tooltip) {
        reactions.forEach(reaction -> tooltip.add(reaction.getTooltipText()));
    }
}
