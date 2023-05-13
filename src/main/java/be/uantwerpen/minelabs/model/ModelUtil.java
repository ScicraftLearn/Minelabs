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
        Vector3f dir1 = new Vector3f(v2).sub(v1);
        Vector3f dir2 = new Vector3f(v3).sub(v1);
        return unitCross(dir1, dir2);
    }

    public static Vector3f unitCross(Vector3f dir1, Vector3f dir2){
        Vector3f l = new Vector3f(dir2);
        l.cross(dir1);
        l.mul(1/norm(l));
        return l;
    }
}
