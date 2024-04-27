package e_dijkstra;

/**
 * @author Zwickelstorfer Felix
 * <p>
 * represents an edge in a mathematical graph
 */
class Edge implements Comparable<Edge> {

    /**
     * the distance of that edge
     */
    private final int distance;

    /**
     * the neighbor of that edge
     */
    private final Node neighbour;

    public Edge(int distance, Node neighbour) {
        this.distance = distance;
        this.neighbour = neighbour;
    }


    public Node getNeighbour() {
        return neighbour;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "d=" + distance +
                ", neigh=" + neighbour.getId() +
                '}';
    }

    @Override
    public int compareTo(Edge o) {
        return neighbour.getId().compareTo(o.neighbour.getId());
    }
}
