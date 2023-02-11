package be.uantwerpen.minelabs.potion;

import be.uantwerpen.minelabs.potion.reactions.*;
import com.ibm.icu.text.MessagePattern;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum Molecule {
    // TODO: add lore with "reacts with stone, is toxic, is poisonous, ..."

    O2("O2", 0xAFAFAF, List.of()),
    N2("N2", 0xAFAFAF, Arrays.asList()),
    CH4("CH4", 0xAFAFAF, List.of(new FlammableReaction(20*3))),
    H2("H2", 0xAFAFAF, Arrays.asList()),
    NO("NO", 0xAFAFAF, Arrays.asList()),
    NO2("NO2", 0x991c00, Arrays.asList()),
    CL2("CL2", 0xE8F48C, List.of(new PoisonousReaction(2, 200, 1))),
    CO2("CO2", 0xAFAFAF, Arrays.asList()),
    CO("CO", 0xAFAFAF, Arrays.asList()),
    NH3("NH3", 0xAFAFAF, Arrays.asList()),
    N2O("N2O", 0xAFAFAF, Arrays.asList()),
    HCL("HCl", 0xAFAFAF, List.of(new CorrosiveReaction())),
    HE("He", 0xAFAFAF, Arrays.asList()),
    NE("Ne", 0xAFAFAF, Arrays.asList()),
    AR("Ar", 0xAFAFAF, Arrays.asList()),
    CL2O("Cl2O", 0xAFAFAF, Arrays.asList()),
    H2CO3("H2CO3", 0xAFAFAF, Arrays.asList()),
    CH4S("CH4S", 0xAFAFAF, Arrays.asList()),
    CH2O("CH2O", 0xAFAFAF, Arrays.asList()),
    BH3("BH3", 0xFFFFFF, Arrays.asList()),
    HF("HF", 0xFFFFFF, Arrays.asList()),
    SIH4("SiH4", 0xFFFFFF, Arrays.asList()),
    PH3("PH3", 0xFFFFFF, Arrays.asList()),
    H2S("H2S", 0xFFFFFF, Arrays.asList()),
    CF4("CF4", 0xFFFFFF, Arrays.asList()),
    BF3("BF3", 0xFFFFFF, Arrays.asList()),
    BCL3("BCl3", 0xFFFFFF, Arrays.asList()),
    SO2("SO2", 0xFFFFFF, Arrays.asList()),
    CLF("ClF", 0xFFFFFF, Arrays.asList()),
    F2("F2", 0xFFFFFF, Arrays.asList()),

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

    public int getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name;
    }

}
