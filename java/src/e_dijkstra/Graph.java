package e_dijkstra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class Graph {

    public Graph() {
    }


    public Graph(Path p) throws IOException {
        readGraphFromAdjacencyMatrixFile(p);
    }

    private final List<Node> nodes = new ArrayList<>();

    public void readGraphFromAdjacencyMatrixFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        String[] headers = lines.get(0).split(";");
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(";");
            Node node = findOrCreateNode(parts[0]);
            for (int j = 1; j < parts.length; j++) {
                if (!parts[j].isEmpty()) {
                    int distance = Integer.parseInt(parts[j]);
                    node.addEdge(new Edge(distance, findOrCreateNode(headers[j])));
                }
            }
        }
    }

    private Node findOrCreateNode(String id) {
        Optional<Node> node = nodes.stream().filter(a -> Objects.equals(a.getId(), id)).findFirst();
        if (node.isPresent()) return node.get();
        Node newNode = new Node(id);
        nodes.add(newNode);
        return newNode;
    }

    public void calcWithDijkstra(String startNodeId) {

        if (startNodeId == null) {
            throw new IllegalArgumentException("Start node id must not be null.");
        }

        for (Node node : nodes) {
            node.reset();
        }

        Node startNode = findNodeById(startNodeId);
        if (startNode == null) {
            throw new IllegalArgumentException("Start node " + startNodeId + " not found in the graph.");
        }
        startNode.setDistance(0); // Distance from start node to itself is 0

        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::getDistance));
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll(); // Node with the shortest distance
            if (currentNode.isVisited()) continue; // Skip nodes already visited
            currentNode.setVisited(true);

            for (Edge edge : currentNode.getEdges()) {
                Node neighbour = edge.getNeighbour();
                int newDist = currentNode.getDistance() + edge.getDistance();
                if (newDist < neighbour.getDistance()) {
                    neighbour.setDistance(newDist);
                    neighbour.setPrevious(currentNode);
                    queue.add(neighbour);
                }
            }
        }
    }

    private Node findNodeById(String id) {
        for (Node node : nodes) {
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Node node : nodes) {
            builder.append(node.toString()).append("\n");
        }
        return builder.toString();
    }

    public String getAllPaths(String startNodeId) {
//        calcWithDijkstra(startNodeId);
        StringBuilder builder = new StringBuilder();
        for (Node node : nodes) {
            builder.append("Path from ").append(startNodeId).append(" to ").append(node.getId())
                    .append(": ").append(getPath(node)).append(", Distance: ").append(node.getDistance()).append("\n");
        }
        return builder.toString();
    }

    private String getPath(Node node) {
        List<String> path = new ArrayList<>();
        for (Node at = node; at != null; at = at.getPrevious()) {
            path.add(at.getId());
        }
        Collections.reverse(path);
        return String.join(" -> ", path);
    }

    public static void main(String[] args) throws IOException {
        Graph g = new Graph(Path.of("res/e_dijkstra/Graph_A-H.csv"));
        System.out.println(g.nodes);
    }
}