package be.uantwerpen.minelabs.model;

import net.minecraft.util.math.Vec3f;

import java.util.List;
import java.util.function.Consumer;

public class ModelUtil {
    public static List<Vec3f[]> transformQuads(List<Vec3f[]> quads, Consumer<Vec3f> op){
        quads.forEach(quad -> ModelUtil.transformQuad(quad, op));
        return quads;
    }

    public static void transformQuad(Vec3f[] quad, Consumer<Vec3f> op){
        for(Vec3f point: quad){
            op.accept(point);
        }
    }

    public static float norm(Vec3f vec) {
        return (float) Math.sqrt(vec.dot(vec));
    }

    public static Vec3f normalOnVertices(Vec3f v1, Vec3f v2, Vec3f v3){
        Vec3f dir1 = v2.copy();
        dir1.subtract(v1);
        Vec3f dir2 = v3.copy();
        dir2.subtract(v1);
        return unit_cross(dir1, dir2);
    }

    public static Vec3f unit_cross(Vec3f dir1, Vec3f dir2){
        Vec3f l = dir2.copy();
        l.cross(dir1);
        l.scale(1/norm(l));
        return l;
    }
}
