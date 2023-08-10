package be.minelabs.client.renderer.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModelUtil {
    public static List<Vector3f[]> transformQuads(List<Vector3f[]> quads, Consumer<Vector3f> op){
        quads.forEach(quad -> ModelUtil.transformQuad(quad, op));
        return quads;
    }

    public static void transformQuad(Vector3f[] quad, Consumer<Vector3f> op){
        for(Vector3f point: quad){
            op.accept(point);
        }
    }

    public static float norm(Vector3f vec) {
        return (float) Math.sqrt(vec.dot(vec));
    }
}
