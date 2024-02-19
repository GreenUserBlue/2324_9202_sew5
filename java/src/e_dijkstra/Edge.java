package e_dijkstra;

class Edge {
    private int distance;
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
