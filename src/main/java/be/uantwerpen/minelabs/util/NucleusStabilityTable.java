package be.uantwerpen.minelabs.util;

import be.uantwerpen.minelabs.Minelabs;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.profiler.Profiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class NucleusStabilityTable extends SinglePreparationResourceReloader<Map<Pair<Integer, Integer>, NucleusStabilityInfo>> implements IdentifiableResourceReloadListener {
    private static final Identifier TABLE_CSV_FILE_ID = new Identifier(Minelabs.MOD_ID, "nuclides_table/nndc_nudat_data_export.csv");

    private static Map<Pair<Integer, Integer>, NucleusStabilityInfo> data;

    @Override
    public Identifier getFabricId() {
        return new Identifier(Minelabs.MOD_ID, getName());
    }

    @Override
    protected Map<Pair<Integer, Integer>, NucleusStabilityInfo> prepare(ResourceManager manager, Profiler profiler) {
        profiler.startTick();

        // Multiple datapacks might have this resource.
        // By processing all of them we can allow these values to be overridden.
        for (Resource resource: manager.getAllResources(TABLE_CSV_FILE_ID)){
            // TODO: in the future the resource itself will be closeable it seems. Refactor
            try(BufferedReader reader = resource.getReader()) {
//                reader.readLine();

            } catch(IOException e) {
                Minelabs.LOGGER.error("Error occurred while loading resource '" + TABLE_CSV_FILE_ID + "' from pack '" + resource.getResourcePackName() + "'", e);
            }
        }
        profiler.endTick();
        return null;
    }

    @Override
    protected void apply(Map<Pair<Integer, Integer>, NucleusStabilityInfo> prepared, ResourceManager manager, Profiler profiler) {
        profiler.startTick();
        NucleusStabilityTable.data = prepared;
        profiler.endTick();
    }

}
