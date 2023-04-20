package be.uantwerpen.minelabs.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class NucleusStabilityInfo {
    public static final NucleusStabilityInfo UNSTABLE = new NucleusStabilityInfo(List.of("all"), Duration.ZERO);
    public static final NucleusStabilityInfo STABLE = new NucleusStabilityInfo(List.of(), Duration.of(1, ChronoUnit.FOREVER));

    // There are a lot of decay modes and they also have a value. Will need to update if we want to actually use them.
    private final List<String> decayModes;
    private final Duration halflife;
    // Stability expressed as percent in [0-1]
    private final float stability;

    protected NucleusStabilityInfo(Duration halflife){
        this(new ArrayList<>(), halflife);
    }

    protected NucleusStabilityInfo(List<String> decayModes, Duration halflife){
        this.decayModes = decayModes;
        this.halflife = halflife;

        // TODO: compute
        stability = 0f;
    }


    public List<String> getDecayModes() {
        return decayModes;
    }

    public Duration getHalflife(){
        return halflife;
    }

    public boolean isStable(){
        return stability < 1f;
    }

    public float getStability() {
        return stability;
    }

    public float getInstability(){
        return 1 - stability;
    }
}
