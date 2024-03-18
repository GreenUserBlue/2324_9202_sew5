package e_dijkstra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Zwickelstorfer Felix
 *
 * This graph represents a mathematical graph with nodes and edges
 */
class Graph {


    /**
     * reads the graph
     * @param p the path of the graph
     * @throws IOException if the file is not found
     */
    public Graph(Path p) throws IOException {
        readGraphFromAdjacencyMatrixFile(p);
    }

    /**
     * all nodes inside the graph
     */
    private final List<Node> nodes = new ArrayList<>();


    /**
     * reads the graph
     * @param path the path of the file
     * @throws IOException if the file is not found
     */
    public void readGraphFromAdjacencyMatrixFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        String[] headers = lines.get(0).split(";");
        if (lines.size() != headers.length)
            throw new IllegalArgumentException("The number of rows and columns must be equal.");
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(";");
            if (!headers[i].equals(parts[0]))
                throw new IllegalArgumentException("The id from the first element of the row '" + parts[0] + "' and first column '" + headers[i] + "' are not equal for index '" + (i - 1) + "'.");
            Node node = findOrCreateNode(parts[0]);

            if (headers.length < parts.length)
                throw new IllegalArgumentException("The number of rows and columns must be equal!");
            for (int j = 1; j < parts.length; j++) {
                if (!parts[j].isEmpty()) {
                    int distance = Integer.parseInt(parts[j]);
                    node.addEdge(new Edge(distance, findOrCreateNode(headers[j])));
                }
            }
        }
    }

    /**
     * finds or creates a node if it doesn't exist
     * @param id the id of the node
     * @return the found or created node
     */
    private Node findOrCreateNode(String id) {
        Optional<Node> node = nodes.stream().filter(a -> Objects.equals(a.getId(), id)).findFirst();
        if (node.isPresent()) return node.get();
        Node newNode = new Node(id);
        nodes.add(newNode);
        return newNode;
    }

    /**
     * calculates the distance and path for each node from the startnode
     * @param startNodeId the start node
     */
    public void calcWithDijkstra(String startNodeId) {

        if (startNodeId == null) {
            throw new IllegalArgumentException("Start node id must not be null.");
        }

        for (Node node : nodes) {
            node.init();
        }

        Node startNode = findNodeById(startNodeId);
        if (startNode == null) {
            throw new IllegalArgumentException("Start node " + startNodeId + " not found in the graph.");
        }
        startNode.setStartNode();

        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::getDistance));
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            if (currentNode.isVisited()) continue;
            currentNode.visit(queue);
        }
    }

    /**
     * finds the node by the id
     * @param id the id of the node
     * @return the node or null if it doesn't exist
     */
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

        Node startNode = nodes.stream().filter(Node::isFirst).findFirst().orElse(null);

        for (Node node : nodes) {
            if (node == startNode) builder
                    .append(node.getId())
                    .append("----> is start node ")
                    .append(node.edgesToStr())
                    .append("\n");
            else builder
                    .append(node.getId())
                    .append(" [totalDistance: ")
                    .append(node.getDistance() != Integer.MAX_VALUE ? node.getDistance() : "?").append("] ")
                    .append(node.edgesToStr())
                    .append("\n");
        }
        return builder.toString();
    }

    /**
     * reads all paths from any to the startnode and returns them in a "beautiful" way
     */
    public String getAllPaths() {
        Node startNode = nodes.stream().filter(Node::isFirst).findFirst().orElse(null);
        StringBuilder builder = new StringBuilder();
        for (Node node : nodes) {
            if (node == startNode) builder
                    .append(node.getId())
                    .append(": is start node")
                    .append("\n");
            else if (node.getDistance() == Integer.MAX_VALUE) builder
                    .append("no path available for ")
                    .append(node.getId())
                    .append(" [totalDistance: ?] ")
                    .append(node.edgesToStr())
                    .append("\n");
            else builder
                        .append(node.getId())
                        .append(" ")
                        .append(getPath(node))
                        .append("\n");
        }
        return builder.toString();
    }

    /**
     * returns the path for a single node to get back to the start node
     * @param node the node where to start from
     * @return the path
     */
    private String getPath(Node node) {
        List<String> path = new ArrayList<>();
        for (Node cur = node; cur.getPrevious() != null; cur = cur.getPrevious()) {
            path.add("--(" + cur.getDistance() + ")-> " + cur.getId());
        }
        Collections.reverse(path);
        return String.join(" ", path);
    }

    public static void main(String[] args) throws IOException {
//        Graph g = new Graph(Path.of("res/e_dijkstra/Graph_A-H.csv"));
//        Graph g = new Graph(Path.of("res/e_dijkstra/kaputt_Graph_A-H_a.csv"));
//        Graph g = new Graph(Path.of("res/e_dijkstra/kaputt_Graph_A-H_b.csv"));
//        Graph g = new Graph(Path.of("res/e_dijkstra/kaputt_Graph_A-H_c.csv"));
//        Graph g = new Graph(Path.of("res/e_dijkstra/kaputt_Graph_A-H_d.csv"));
//        Graph g = new Graph(Path.of("res/e_dijkstra/Graph_12_with_names.csv"));
        Graph g = new Graph(Path.of("res/e_dijkstra/unzusammenhaengend_Graph_A-M.csv"));
        System.out.println(g);
        System.out.println();
        System.out.println(g.getAllPaths());
        System.out.println();

        g.calcWithDijkstra("A");
//        g.calcWithDijkstra("Adonaäis");
        System.out.println(g);
        System.out.println();
        System.out.println(g.getAllPaths());
        System.out.println();


        g.calcWithDijkstra("B");
//        g.calcWithDijkstra("Barthhal");
        System.out.println(g);
        System.out.println();
        System.out.println(g.getAllPaths());
        System.out.println();
    }
}