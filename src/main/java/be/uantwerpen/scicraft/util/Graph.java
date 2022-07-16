package be.uantwerpen.scicraft.util;

import java.util.*;
import java.util.stream.Collectors;

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

        private final Set<Edge> edges = new HashSet<>();

        private Vertex(V data) {
            this.data = data;
        }

        public Collection<Vertex> getNeighbours() {
            return edges.stream().flatMap(edge -> edge.getVertices().stream().filter(vertex -> vertex != this)).collect(Collectors.toSet());
        }

        public Collection<V> getNeighboursData() {
            return getNeighbours().stream().map(vertex -> vertex.data).collect(Collectors.toList());
        }

        public Collection<Edge> getEdges() {
            return edges;
        }

        public Collection<E> getEdgesData() {
            return edges.stream().map(edge -> edge.data).collect(Collectors.toList());
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append(data.toString()).append("\n");
            for (Edge edge : getEdges()) {
                s.append("\t-- (").append(edge.data.toString()).append(")\t").append(edge.getOtherVertex(this).data.toString()).append("\n");
            }
            return s.toString();
        }
    }

    public class Edge {
        private final Set<Vertex> vertices;
        public E data;

        protected Edge(Set<Vertex> vertices, E data) {
            assert vertices.size() == 2;
            this.vertices = vertices;
            this.data = data;
        }

        public Collection<Vertex> getVertices() {
            return vertices;
        }

        private Vertex getOtherVertex(Vertex ignore) {
            if (!vertices.contains(ignore))
                throw new RuntimeException("Tried to find other end of edge from a vertex that isn't connected.");
            return vertices.stream().filter(v -> v != ignore).findFirst().orElseThrow(() -> new RuntimeException("Invalid edge: has less than 2 entries"));
        }
    }

    private final Set<Vertex> vertices = new HashSet<>();

    private final Map<Set<Vertex>, Edge> edges = new HashMap<>();

    public Set<Vertex> getVertices() {
        return vertices;
    }

    public Vertex addVertex(V data) {
        Vertex v = new Vertex(data);
        vertices.add(v);
        return v;
    }

    public Edge addEdge(Vertex v1, Vertex v2, E data) throws IllegalArgumentException {
        Set<Vertex> vertices = Set.of(v1, v2);
        if (edges.containsKey(vertices))
            throw new IllegalArgumentException("Edge already exists");
        Edge edge = new Edge(vertices, data);

        // keep vertices consistent
        v1.edges.add(edge);
        v2.edges.add(edge);

        // store in graph
        edges.put(vertices, edge);
        return edge;
    }

    public Collection<V> getVertexData() {
        return vertices.stream().map(vertex -> vertex.data).collect(Collectors.toList());
    }

    public boolean isIsomorphicTo(Graph<V, E> other) {
        // TODO: implement
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Undirected graph\n");
        for (Vertex vertex : vertices) {
            s.append(vertex.toString()).append("\n");
        }
        return s.toString();
    }
}
