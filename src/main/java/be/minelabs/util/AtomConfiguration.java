package be.minelabs.util;

import be.minelabs.recipe.molecules.Atom;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AtomConfiguration{

    private static final int MAX_ELECTRONS_ABOVE_PROTONS = 8;

    private final int protons;
    private final int neutrons;
    private final int electrons;

    private final NucleusStabilityInfo nucleusStability;
    // the atom based on protons
    @Nullable
    private final Atom atom;

    public AtomConfiguration(int protons, int neutrons, int electrons) {
        this(protons, neutrons, electrons, NucleusStabilityTable.INSTANCE.getStabilityInfo(protons, neutrons));
    }

    private AtomConfiguration(int protons, int neutrons, int electrons, NucleusStabilityInfo nucleusStability) {
        this.protons = protons;
        this.neutrons = neutrons;
        this.electrons = electrons;
        this.nucleusStability = nucleusStability;

        atom = Atom.getByNumber(protons);
    }

    @Nullable
    public Item getAtomItem() {
        if (atom == null) return null;
        return atom.getItem();
    }

    public ItemStack getAtomStack() {
        // it's ok if this is null. The ItemStack will be the empty stack.
        Item atomItem = getAtomItem();
        return new ItemStack(atomItem, 1);
    }

    public Optional<String> getSymbol() {
        if (atom != null) return Optional.of(atom.getSymbol());
        return Optional.empty();
    }

    public Optional<String> getName() {
        if (atom != null) return Optional.of(atom.getName());
        return Optional.empty();
    }

    public boolean isStable() {
        return isNucleusStable() && isElectronStable();
    }

    public boolean isNucleusStable() {
        if (nucleusStability == null) return false;
        return nucleusStability.getInstability() < 0.01f;
    }

    public boolean isNucleusDecomposing() {
        if (nucleusStability == null) return true;
        return nucleusStability.getInstability() > 0.99f;
    }

    public float getNucleusInstability() {
        if (isNucleusStable()) return 0f;
        if (isNucleusDecomposing()) return 1f;

        return nucleusStability.getInstability();
    }

    public boolean isElectronStable() {
        return electrons == protons;
    }

    public boolean isElectronDecomposing() {
        return electrons > protons + MAX_ELECTRONS_ABOVE_PROTONS;
    }

    public float getElectronInstability() {
        float delta = (float) (electrons - protons) / (float) MAX_ELECTRONS_ABOVE_PROTONS;
        return MathHelper.clampedLerp(0f, 1f, delta);
    }

    public int getDecomposingElectronCount(){
        return Math.max(0, electrons - protons - MAX_ELECTRONS_ABOVE_PROTONS);
    }

    public @Nullable Atom getAtom() {
        return atom;
    }

    public int getProtons() {
        return protons;
    }

    public int getNeutrons() {
        return neutrons;
    }

    public int getElectrons() {
        return electrons;
    }

    public static final DataHandler DATA_HANDLER = new DataHandler();

    public static class DataHandler implements TrackedDataHandler.ImmutableHandler<AtomConfiguration> {
        private DataHandler(){}

        @Override
        public void write(PacketByteBuf buf, AtomConfiguration atomConfig) {
            buf.writeInt(atomConfig.protons);
            buf.writeInt(atomConfig.neutrons);
            buf.writeInt(atomConfig.electrons);
            atomConfig.nucleusStability.write(buf);
        }

        @Override
        public AtomConfiguration read(PacketByteBuf buf) {
            int protons = buf.readInt();
            int neutrons = buf.readInt();
            int electrons = buf.readInt();
            NucleusStabilityInfo nucleusStability = NucleusStabilityInfo.read(buf);

            return new AtomConfiguration(protons, neutrons, electrons, nucleusStability);
        }
    }
}
