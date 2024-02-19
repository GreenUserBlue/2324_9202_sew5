package e_dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

class Node implements Comparable<Node> {

    public String getId() {
        return id;
    }

    private final String id;

    private final List<Edge> edges = new ArrayList<>();

    private int distance = Integer.MAX_VALUE;

    private Node previous = null;

    public boolean isVisited() {
        return isVisited;
    }

    private boolean isVisited = false;

    public Node(String id) {
        this.id = id;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    @Override
    public int compareTo(Node other) {
        return this.distance == other.distance ? this.id.compareTo(other.id) : Integer.compare(this.distance, other.distance);
    }

    public int getDistance() {
        return distance;
    }

    public Node getPrevious() {
        return previous;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", edges=" + edges +
                '}';
    }
}