package be.uantwerpen.minelabs.model;

import org.joml.Vector3f;

import java.util.List;
import java.util.function.Consumer;

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

    public static Vector3f normalOnVertices(Vector3f v1, Vector3f v2, Vector3f v3){
        Vector3f dir1 = v2.copy();
        dir1.subtract(v1);
        Vector3f dir2 = v3.copy();
        dir2.subtract(v1);
        return unit_cross(dir1, dir2);
    }

    public static Vector3f unit_cross(Vector3f dir1, Vector3f dir2){
        Vector3f l = dir2.copy();
        l.cross(dir1);
        l.scale(1/norm(l));
        return l;
    }
}
