package e_dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

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

    public void reset() {
        isVisited = false;
        previous = null;
        distance = Integer.MAX_VALUE;
    }

    public void setStartNode() {
        distance = 0;
    }

    public void visit(PriorityQueue<Node> queue) {
        isVisited = true;
        for (Edge edge : edges) {
            Node neighbour = edge.getNeighbour();
            int newDist = distance + edge.getDistance();
            if (newDist < neighbour.distance) {
                neighbour.distance = newDist;
                neighbour.previous = this;
                queue.add(edge.getNeighbour());
            }
        }
    }

    public String edgesToStr() {
        return edges.stream().map(it -> it.getNeighbour().id + ":" + it.getDistance()).collect(Collectors.joining(" ", "", ""));
    }

    public boolean isFirst() {
        //man braucht beides, da die distance zu einem neighbor 0 sein kann, oder wenn etwas nicht erreichbar das previous auch null ist
        return previous == null && distance == 0;
    }
}