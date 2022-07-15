package be.uantwerpen.scicraft.util;

import java.util.*;

/**
 * Basic implementation of undirected graph.
 * Intended to be used for Molecule structure.
 *
 * @param <V> Vertex data class
 * @param <E> Edge data class
 */
public class Graph<V, E> {

    public class Vertex {
        public V data;

        private Vertex(V data) {
            this.data = data;
        }
    }

    public class Edge {
        public final Vertex v1;
        public final Vertex v2;
        public E data;

        protected Edge(Vertex v1, Vertex v2, E data) {
            this.v1 = v1;
            this.v2 = v2;
            this.data = data;
        }
    }

    private final Set<Vertex> vertices = new HashSet<>();

    class EdgeKey{
        Vertex v1;
        Vertex v2;

        private EdgeKey(Vertex v1, Vertex v2){
            this.v1 = v1;
            this.v2 = v2;
        }
    }

    private final Map<EdgeKey, Edge> edges = new HashMap<>();

    public Set<Vertex> getVertices(){
        return vertices;
    }

    public Vertex addVertex(V data) {
        Vertex v = new Vertex(data);
        vertices.add(v);
        return v;
    }

    public Edge addEdge(Vertex v1, Vertex v2, E data) throws IllegalArgumentException {
        EdgeKey edgeKey = new EdgeKey(v1, v2);
        if (edges.containsKey(edgeKey))
            throw new IllegalArgumentException("Edge already exists");
        Edge edge = new Edge(v1, v2, data);
        edges.put(edgeKey, edge);
        edges.put(new EdgeKey(v2, v1), edge);       // undirected graph
        return edge;
    }

}
