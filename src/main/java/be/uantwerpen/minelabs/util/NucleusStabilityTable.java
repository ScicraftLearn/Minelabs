package be.uantwerpen.minelabs.util;

import be.uantwerpen.minelabs.Minelabs;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NucleusStabilityTable extends SinglePreparationResourceReloader<Map<Pair<Integer, Integer>, NucleusStabilityInfo>> implements IdentifiableResourceReloadListener {
    private static final Identifier TABLE_CSV_FILE_ID = new Identifier(Minelabs.MOD_ID, "nuclides_table/nndc_nudat_data_export.csv");

    private static final String CSV_DELIMITER = ",";

    // Converstion between units in csv file and java temporal units.
    private static final Map<String, TemporalUnit> unitMap = ImmutableMap.<String, TemporalUnit>builder()
            // following units are mapped to very unstable. We use nanoseconds as a proxy.
            .put("ns", ChronoUnit.NANOS)
            .put("mev", ChronoUnit.NANOS)
            .put("ev", ChronoUnit.NANOS)
            .put("kev", ChronoUnit.NANOS)
            .put("as", ChronoUnit.NANOS)
            .put("ps", ChronoUnit.NANOS)
            // until here
            .put("us", ChronoUnit.MICROS)
            .put("ms", ChronoUnit.MILLIS)
            // everything below seconds is mapped to zero
            .put("s", ChronoUnit.SECONDS)
            .put("m", ChronoUnit.MINUTES)
            .put("h", ChronoUnit.HOURS)
            .put("d", ChronoUnit.DAYS)
            .put("y", ChronoUnit.YEARS)
            .build();

    // Map from (protons, neutrons) to stabilityInfo
    private static Map<Pair<Integer, Integer>, NucleusStabilityInfo> data;

    public static NucleusStabilityInfo getStabilityInfo(int protons, int neutrons) {
        if (protons == 0 && neutrons == 0)
            return NucleusStabilityInfo.STABLE;
        return data.getOrDefault(new Pair<>(protons, neutrons), NucleusStabilityInfo.UNSTABLE);
    }

    private Map<Pair<Integer, Integer>, NucleusStabilityInfo> parseTable(BufferedReader reader) throws IOException {
        String header = reader.readLine();
        return reader.lines()
                .map(this::parseLine)
                .collect(Collectors.toMap(
                        Pair::getFirst,
                        Pair::getSecond,
                        NucleusStabilityInfo::merge)
                );
    }

    private Pair<Pair<Integer, Integer>, NucleusStabilityInfo> parseLine(String line) {
        return parseLine(line.split(CSV_DELIMITER, -1));
    }

    private Pair<Pair<Integer, Integer>, NucleusStabilityInfo> parseLine(String[] values) {
        // header: z,n,name,levelEnergy(MeV),halflife,decayModes
        int p = Integer.parseInt(values[0]);
        int n = Integer.parseInt(values[1]);
        String name = values[2];
        // not used (need to check for empty string etc. if we want to parse)
//        float energy = Float.parseFloat(values[3]);
        Duration halfLife = parseDuration(values[4]);
        String decayMode = values.length < 6 ? null : values[5];
        return parseLine(p, n, name, 0, halfLife, decayMode);
    }

    private Pair<Pair<Integer, Integer>, NucleusStabilityInfo> parseLine(int p, int n, String name, float energy, Duration halfLife, @Nullable String decayMode) {
        return new Pair<>(
                new Pair<>(p, n),
                new NucleusStabilityInfo(halfLife, decayMode)
        );
    }

    private Duration parseDuration(@Nullable String text) {
        if (text == null)
            return Duration.ZERO;
        if (text.equals("STABLE"))
            return NucleusStabilityInfo.MAX_HALFLIFE;

        // format: "float unit uncertainty", examples:
        //  806.7 ms 15
        //  4.41E+14 y 25
        // However, format can also be: "[2 letters] float unit", examples:
        //  GT 180 ns
        //  AP 4 ms
        // Then there is also apparently a reason for yet another format with these values:
        //  7 7 21.2 ms
        //  4    4 9.8 ms
        String[] parts = text.split(" ");

        if (parts.length < 2)
            return Duration.ZERO;

        // likely third funky format
        if (parts.length > 3) {
            // only keep last two entries
            parts[0] = parts[parts.length - 2];
            parts[1] = parts[parts.length - 1];
        }

        // when parsing fails, it's likely due to the second format -> shift everything by one
        float value;
        try {
            value = Float.parseFloat(parts[0]);
        } catch (NumberFormatException e) {
            value = Float.parseFloat(parts[1]);
            parts[1] = parts[2];
        }

        TemporalUnit unit = unitMap.get(parts[1]);
        if (unit == null) {
            Minelabs.LOGGER.warn("When parsing nucleus stability table, encountered invalid unit: " + parts[1]);
            Minelabs.LOGGER.info("string: " + text);
            return Duration.ZERO;
        }

        // seconds is our base unit (in order to convert from float to seconds)
        long multiplier = unit.getDuration().dividedBy(Duration.of(1, NucleusStabilityInfo.MIN_HALFLIFE_UNIT));

        // all units below seconds are mapped to zero
        if (multiplier == 0)
            return Duration.ZERO;

        return Duration.of((long) (value * multiplier), NucleusStabilityInfo.MIN_HALFLIFE_UNIT);
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(Minelabs.MOD_ID, getName().toLowerCase());
    }

    @Override
    protected Map<Pair<Integer, Integer>, NucleusStabilityInfo> prepare(ResourceManager manager, Profiler profiler) {
        profiler.startTick();

        Map<Pair<Integer, Integer>, NucleusStabilityInfo> allData = new HashMap<>();

        // Multiple datapacks might have this resource.
        // By processing all of them we can allow these values to be overridden.
        for (Resource resource : manager.getAllResources(TABLE_CSV_FILE_ID)) {
            // TODO: in the future the resource itself will be closeable it seems. Refactor
            try (BufferedReader reader = resource.getReader()) {
                Map<Pair<Integer, Integer>, NucleusStabilityInfo> fileData = parseTable(reader);
                allData.putAll(fileData);
            } catch (IOException e) {
                Minelabs.LOGGER.error("Error occurred while loading resource '" + TABLE_CSV_FILE_ID + "' from pack '" + resource.getResourcePackName() + "'", e);
            }
        }
        profiler.endTick();
        return allData;
    }

    @Override
    protected void apply(Map<Pair<Integer, Integer>, NucleusStabilityInfo> prepared, ResourceManager manager, Profiler profiler) {
        profiler.startTick();
        NucleusStabilityTable.data = prepared;
        profiler.endTick();
    }

}
