package be.uantwerpen.minelabs.potion;

import be.uantwerpen.minelabs.potion.reactions.*;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum Molecule {

    // Gasses
    O2("O2", 0xAFAFAF, List.of()),
    N2("N2", 0xAFAFAF, List.of()),
    CH4("CH4", 0xAFAFAF, List.of(new FlammableReaction(20 * 3, 4, false))),
    H2("H2", 0xAFAFAF, List.of(new ExplosiveReaction(1, false, false))),
    NO("NO", 0xAFAFAF, List.of()),
    NO2("NO2", 0x991c00, List.of()),
    CL2("CL2", 0xE8F48C, List.of(new PoisonousReaction(2, 200, 1))),
    CO2("CO2", 0xAFAFAF, List.of(new ExtinguishReaction(4))),
    CO("CO", 0xAFAFAF, List.of()),
    NH3("NH3", 0xAFAFAF, Arrays.asList(new DissolveReaction(), new PoisonousReaction(4, 400, 1))),
    N2O("N2O", 0xAFAFAF, List.of()),
    HCL("HCl", 0xAFAFAF, Arrays.asList(new CorrosiveReaction(4), new DissolveReaction())),
    HE("He", 0xAFAFAF, List.of()),
    NE("Ne", 0xAFAFAF, List.of()),
    AR("Ar", 0xAFAFAF, List.of()),
    CL2O("Cl2O", 0xAFAFAF, List.of()),
    H2CO3("H2CO3", 0xAFAFAF, List.of()),
    CH4S("CH4S", 0xAFAFAF, List.of()),
    CH2O("CH2O", 0xAFAFAF, List.of()),
    BH3("BH3", 0xFFFFFF, Arrays.asList(new FlammableReaction(20 * 3, 4, true), new PoisonousReaction(4, 400, 1))),
    HF("HF", 0xFFFFFF, List.of(new PoisonousReaction(4, 400, 1))),
    SIH4("SiH4", 0xFFFFFF, List.of(new FlammableReaction(20 * 3, 4, true))),
    PH3("PH3", 0xFFFFFF, Arrays.asList(new FlammableReaction(20 * 3, 4, false), new PoisonousReaction(5, 800, 2), new CorrosiveReaction(4))),
    H2S("H2S", 0xFFFFFF, Arrays.asList(new ExplosiveReaction(1, true, false), new PoisonousReaction(4, 400, 1))),
    CF4("CF4", 0xFFFFFF, List.of()),
    BF3("BF3", 0xFFFFFF, List.of()),
    BCL3("BCl3", 0xFFFFFF, List.of()),
    SO2("SO2", 0xFFFFFF, List.of()),
    CLF("ClF", 0xFFFFFF, List.of()),
    F2("F2", 0xFFFFFF, List.of()),

    // Fluids
    HNO3("HNO3", 0xFFCC33, Arrays.asList(new PoisonousReaction(4, 100, 1))),
    H2O("H2O", 0x3495eb, Arrays.asList(new ExtinguishReaction(5))),
    NCL3("NCl3", 0xe8dc5a, Arrays.asList(new FlammableReaction(100, 5, true))),
    CS2("CS2", 0xAFAFAF, Arrays.asList()),
    CCL4("CCl4", 0xAFAFAF, Arrays.asList()),
    PCL3("PCl3", 0xAFAFAF, Arrays.asList()),
    SCL2("SCl2", 0xAFAFAF, Arrays.asList()),
    HCN("HCN", 0xCCCCFF, Arrays.asList()),
    CH4O("CH4O", 0xAFAFAF, Arrays.asList()),
    SICL4("SiCl4", 0xAFAFAF, Arrays.asList()),
    ;


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

}
