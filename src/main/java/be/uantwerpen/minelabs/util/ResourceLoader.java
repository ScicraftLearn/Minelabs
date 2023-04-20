package be.uantwerpen.minelabs.util;

import be.uantwerpen.minelabs.Minelabs;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ResourceLoader implements IdentifiableResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return new Identifier(Minelabs.MOD_ID, "resources");
    }

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        Identifier table;
        try(InputStream stream = manager.getResource(table).getInputStream()) {
            // Consume the stream however you want, medium, rare, or well done.
        } catch(Exception e) {
            Minelabs.LOGGER.error("Error occurred while loading resource csv " + table.toString(), e);
        }
        return null;
    }

    @Override
    public String getName() {
        return "MinelabsResourceLoader";
    }
}
