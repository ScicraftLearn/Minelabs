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

        public Collection<Vertex> getNeighbours(){
            return edges.stream().flatMap(edge -> edge.getVertices().stream().filter(vertex -> vertex != this)).collect(Collectors.toSet());
        }

        public Collection<V> getNeighboursData(){
            return getNeighbours().stream().map(vertex -> vertex.data).collect(Collectors.toSet());
        }

        public Collection<Edge> getEdges(){
            return edges;
        }

        public Collection<E> getEdgesData(){
            return edges.stream().map(edge -> edge.data).collect(Collectors.toSet());
        }
    }

    public class Edge {
        private final Set<Vertex> vertices;
        public E data;

        protected Edge(Set<Vertex> vertices, E data) {
            this.vertices = vertices;
            this.data = data;
        }

        public Collection<Vertex> getVertices(){
            return vertices;
        }
    }

    private final Set<Vertex> vertices = new HashSet<>();

    private final Map<Set<Vertex>, Edge> edges = new HashMap<>();

    public Set<Vertex> getVertices(){
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

}
