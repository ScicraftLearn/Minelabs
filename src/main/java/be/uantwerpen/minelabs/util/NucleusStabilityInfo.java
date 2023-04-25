package be.uantwerpen.minelabs.util;

import be.uantwerpen.minelabs.Minelabs;
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
        if (first.getHalflife() != second.getHalflife())
            Minelabs.LOGGER.warn("Merging two NucleusStabilityInfos with different duration");

        return new NucleusStabilityInfo(
                first.getHalflife(),
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
}


/*
    public static LinkedHashMap<Float, ArrayList<Float>> makeHalflifeRanges() {

        // ranges:
        // [0sec - 1sec], ]1sec - 1uur], ]1uur - 1dag], ]1dag-1maand],
        // ]1maand-1jaar],]1jaar - 10jaar], ]10jaar - 10000jaar], ]10000jaar-oneindig[

        LinkedHashMap<Float, ArrayList<Float>> _halflifeRanges = new LinkedHashMap<>();
        _halflifeRanges.put(1f, new ArrayList<>(Arrays.asList(0.04f, 10f))); // 1 second
        _halflifeRanges.put(3600f, new ArrayList<>(Arrays.asList(0.035f, 20f))); // 1 hour
        _halflifeRanges.put(86400f, new ArrayList<>(Arrays.asList(0.03f, 30f))); // 1 day
        _halflifeRanges.put(2629743.83f, new ArrayList<>(Arrays.asList(0.025f, 40f))); // 1 month
        _halflifeRanges.put(31556926f, new ArrayList<>(Arrays.asList(0.02f, 55f))); // 1 year
        _halflifeRanges.put(315569260f, new ArrayList<>(Arrays.asList(0.015f, 75f))); // 10 years
        _halflifeRanges.put(315569260000f, new ArrayList<>(Arrays.asList(0.01f, 90f))); // 10000 years
        return  _halflifeRanges;
    }
 */