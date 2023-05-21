package be.minelabs.util;

import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NucleusStabilityInfo {

    // min and max halfilfe values to consider. Everything below is seen as zero, above as stable.
    public static final TemporalUnit MIN_HALFLIFE_UNIT = ChronoUnit.SECONDS;
    public static final Duration MAX_HALFLIFE = Duration.of(1000 * ChronoUnit.YEARS.getDuration().toSeconds(), ChronoUnit.SECONDS);

    public static final NucleusStabilityInfo UNSTABLE = new NucleusStabilityInfo(Duration.ZERO, "all");
    public static final NucleusStabilityInfo STABLE = new NucleusStabilityInfo(MAX_HALFLIFE);

    private final Duration halflife;
    // There are a lot of decay modes and they also have a value. Will need to update if we want to actually use them.
    private final List<String> decayModes;
    // Stability expressed as percent in [0-1]
    private final float stability;

    protected NucleusStabilityInfo(Duration halflife) {
        this(halflife, new ArrayList<>());
    }

    protected NucleusStabilityInfo(Duration halflife, @Nullable String decayMode) {
        this(halflife, decayMode == null ? List.of() : List.of(decayMode));
    }

    protected NucleusStabilityInfo(Duration halflife, List<String> decayModes) {
        this.halflife = halflife;
        this.decayModes = decayModes;

        stability = computeStability(halflife);
    }

    private static float computeStability(Duration halflife) {
        // above max halflife: fully stable
        if (halflife.compareTo(MAX_HALFLIFE) >= 0)
            return 1;

        // delta between 0 and 1
        double delta = (float) halflife.toSeconds() / (float) MAX_HALFLIFE.toSeconds();
        return (float) Math.pow(delta, 1d / 10d);
    }

    protected static NucleusStabilityInfo merge(NucleusStabilityInfo first, NucleusStabilityInfo second) {
        // This happens quite often and we don't know why.
//        if (!first.getHalflife().equals(second.getHalflife()))
//            Minelabs.LOGGER.warn("Merging two NucleusStabilityInfos with different duration " + first.getHalflife() + " and " + second.getHalflife());

        return new NucleusStabilityInfo(
                first.getHalflife().compareTo(second.getHalflife()) > 0 ? first.getHalflife() : second.getHalflife(),
                Stream.concat(first.getDecayModes().stream(), second.getDecayModes().stream()).toList()
        );
    }



    public List<String> getDecayModes() {
        return decayModes;
    }

    public Duration getHalflife() {
        return halflife;
    }

    public float getStability() {
        return stability;
    }

    public float getInstability() {
        return 1 - stability;
    }

    public void write(PacketByteBuf buf) {
        // TODO: decay modes not synced because they are not used yet.
        buf.writeLong(halflife.toSeconds());
    }

    public static NucleusStabilityInfo read(PacketByteBuf buf) {
        // TODO: decay modes not synced because they are not used yet.
        Duration halflife = Duration.ofSeconds(buf.readLong());
        return new NucleusStabilityInfo(halflife);
    }
}