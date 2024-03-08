package e_dijkstra;

/**
 * @author Zwickelstorfer Felix
 *
 * represents an edge in a mathematical graph
 */
class Edge {

    /**
     * the distance of that edge
     */
    private int distance;

    /**
     * the neighbor of that edge
     */
    private Node neighbour;

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
}
