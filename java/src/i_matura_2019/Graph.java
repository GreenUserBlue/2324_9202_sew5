package i_matura_2019;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Der Graph mit dem Dijkstra-Algorithmus.
 *
 * @author DI Franz Breunig, HTL3R, Oktober 2015
 */
public class Graph implements IChangeDistance {

    /**
     * Alle Knoten des Graphen
     */
    private List<Node> nodes = new ArrayList<>();


    /**
     * Die Priority-Queue fuer den Dijkstra-Algorithmus
     * Die Knoten werden in der Priority-Queue zuerst nach ihrer Entfernung zum Startknoten
     * ({@link Node#getTotalDistance()} gereiht, danach nach ihrem Namen ({@link Node#getId()}).
     */
    private PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparing(Node::getTotalDistance));


    /**
     * Liest eine Adjazenz-Matrix aus einem File ein.<br>
     * (siehe auch {@link #setAdjacencMatrix(List)})
     *
     * @param file das File mit der Adjazenz-Matrix
     * @throws IOException Wenn das File nicht gelesen werden konnte.
     */
    @SuppressWarnings("WeakerAccess")
    public void readGraphFromAdjazenzMatrixFile(Path file) throws IOException {
        setAdjacencMatrix(clearEmptyLines(Files.readAllLines(file)));
    }


    /**
     * Erzeugt einen neuen Graphen aus einer Adjazenz-Matrix.<br>
     * Beispiel fuer eine Adjazenz-Matrix mit Knoten von A bis H:<br>
     * ;A;B;C;D;E;F;G;H<br>
     * A;;1;3;1;;;;<br>
     * B;1;;;;3;3;;<br>
     * C;3;;;1;;;1;<br>
     * D;1;;1;;1;;2;<br>
     * E;;3;;1;;1;;5<br>
     * F;;3;;;1;;;1<br>
     * G;;;1;2;;;;1<br>
     * H;;;;;5;1;1;<br>
     * (Kein Wert --> unendliche Entfernung)
     *
     * @param matrixLines die Adjazenz-Matrix
     */
    @SuppressWarnings("WeakerAccess")
    public void setAdjacencMatrix(List<String> matrixLines) {
        List<Node> nodes = new ArrayList<>();

        String[][] a = to2dArray(matrixLines);
        String[] nodeIds = a[0];

        if (nodeIds.length > a.length) {
            throw new IllegalArgumentException("to less lines: "
                    + a.length + ", expected: " + nodeIds.length);
        }

        for (int col = 1; col < nodeIds.length; col++) {
            Node node = new Node(nodeIds[col]);
            if (nodes.contains(node)) {
                throw new IllegalArgumentException("duplicated Nodes " + nodeIds[col] + " in the first line");
            }
            nodes.add(node);
        }

        String[] rowHeaders = getRowHeaders(a);
        if (!Arrays.equals(nodeIds, rowHeaders)) {
            throw new IllegalArgumentException(String.format("row headers != col headers (row: %s, col: %s)",
                    Arrays.toString(nodeIds), Arrays.toString(rowHeaders)));
        }

        for (int row = 1; row < a.length; row++) {
            for (int col = 1; col < a[row].length; col++) {
                String distance = a[row][col];
                if (!distance.isEmpty()) {
                    nodes.get(row - 1).addNeighbour(new Neighbour(nodes.get(col - 1), Integer.parseInt(distance)));
                }
            }
        }

        this.nodes = nodes;
    }


    /**
     * Wandelt ein eindimensionales String-Array in ein zweidimensionales Array um, indem am ";" gesplittet wird.
     *
     * @param matrixLines die Adjazenz-Matrix
     * @return die Adjazenz-Matrix
     * @throws IllegalArgumentException wenn die Anzahl der Zeilen nicht die Anzahl der Spalten der ersten Zeile ist.
     */
    private String[][] to2dArray(List<String> matrixLines) {
        String[][] a = new String[matrixLines.size()][];

        for (int row = 0; row < matrixLines.size(); row++) {
            String[] line = (matrixLines.get(row) + " ").split(";");
            a[row] = new String[line.length];

            if (a[0].length != line.length) {
                throw new IllegalArgumentException(
                        String.format("line number %d has the wrong size (%d), expected was the size of the first line (%d)",
                                row + 1, line.length, a[0].length));
            }

            for (int col = 0; col < line.length; col++) {
                a[row][col] = line[col].trim();
            }
        }

        return a;
    }


    /**
     * Liefert die Zeilenbeschriftungen.
     *
     * @param a das Array
     * @return die Zeilen-Beschriftungen
     */
    private static String[] getRowHeaders(String[][] a) {
        String[] col = new String[a.length];

        for (int row = 0; row < a.length; row++) {
            col[row] = a[row][0];
        }

        return col;
    }


    /**
     * Loescht alle Leerzeilen aus der Liste.<br>
     * (Leerzeile = leere Zeile oder Zeile die nur whitespaces hat)
     *
     * @param list Liste mit den Zeilen
     * @return Liste mit allen Zeile, ohne Leerzeilen
     */
    private static List<String> clearEmptyLines(List<String> list) {
        list.removeIf(s -> s.trim().isEmpty());

        return list;
    }


    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Node n : nodes) {
            b.append(n).append('\n');
        }
        return b.toString();
    }


    @Override
    public void changeDistance(Node node, Node previous, int newTotalDistance) {
        if ((!pq.contains(node)) || node.getTotalDistance() > newTotalDistance) {
            pq.remove(node);
            node.setTotalDistance(newTotalDistance);
            node.setPrevious(previous);
            pq.add(node);
        }
    }


    /**
     * Baut einen Dijkstra-Graphen auf.<br>
     * Ausgabe siehe: {@link #getAllPaths()}
     *
     * @param startNodeNumber die Nummer des Startknotens
     */
    @SuppressWarnings("WeakerAccess")
    public void calcWithDijkstra(int startNodeNumber) {
        for (Node node : nodes) {
            node.init();
        }

        nodes.get(startNodeNumber).setStartNode(this);

        while (!pq.isEmpty()) {
            pq.poll().visit(this);
        }

        for (Node node : nodes) {
            if (!node.isVisited()) {
                throw new IllegalArgumentException("disconnected Graph, node unreachable: " + node);
            }
        }
    }


    /**
     * Baut einen Dijkstra-Graphen auf.<br>
     * Ausgabe siehe: {@link #getAllPaths()}
     *
     * @param startNodeId der Name des Knoten, siehe: {@link Node#getId()}
     */
    @SuppressWarnings("WeakerAccess")
    public void calcWithDijkstra(String startNodeId) {
        for (int nodeNumber = 0; nodeNumber < nodes.size(); nodeNumber++) {
            if (nodes.get(nodeNumber).getId().equals(startNodeId)) {
                calcWithDijkstra(nodeNumber);
                return;
            }
        }

        throw new IllegalArgumentException(String.format("start node %s not found", startNodeId));
    }


    /**
     * Liefert einen String mit den kuerzesten Pfaden für alle Knoten im Format:<br>
     * A --(1)-> D --(2)-> E --(3)-> F --(4)-> H<br>
     * Wobei in Klammer immer die Distanz vom Startknoten steht.)
     *
     * @return die Pfad-Beschreibung
     */
    @SuppressWarnings("WeakerAccess")
    public String getAllPaths() {
        if (nodes.size() <= 0) {
            return "";
        }

        StringBuilder b = new StringBuilder();

        for (Node node : nodes) {
            b.append("\n").append(node.getPath());
        }

        return b.substring(1);
    }


    /**
     * Liest einen Graph aus einem map-File ein.<br>
     * (siehe auch {@link #setGraphFromMap(List)})
     *
     * @param file das File mit der Map
     * @throws IOException Wenn das File nicht gelesen werden konnte.
     */
    @SuppressWarnings("WeakerAccess")
    public void readGraphFromMap(Path file) throws IOException {
        setGraphFromMap(Files.readAllLines(file));
    }


    /**
     * Erzeugt eine Knoten-ID aus dem Knoten-Namen.<br>
     * Falls der Name ein Leerzeichen ist, wird für die Knoten-ID ein 't' verwendet. (Fliese: tile)
     *
     * @param kind Leerzeichen, 'A' und 'R'
     * @param col  die Spalten-Nummer des Knotens
     * @param row  die Zeile-Nummer des Knotens
     * @return die Knoten-ID, Bsp.: "t:1/3", "R:1/4" bzw. "A:1/1"
     */
    @SuppressWarnings("WeakerAccess")
    public static String getNodeID(char kind, int col, int row) {
        return switch (kind) {
            case 'A', 'R' -> kind + ":" + col + "/" + row;
            case ' ' -> "t:" + col + "/" + row;
            case '#' -> "W:" + col + "/" + row;
            default -> throw new IllegalArgumentException("Unknown type of tile: '" + kind + "'");
        };
    }


    /**
     * Sucht aus der Collection »nodes« den Node mit der »nodeID«.<br>
     * Falls kein passender Node gefunden werden konnnte, wird »null« zurück gegeben
     *
     * @param nodes  die Node-Collection, die durchsucht wird
     * @param nodeID die Node-ID des gesuchten Nodes
     * @return der gesuchte Node, oder null, falls er nicht gefunden werden konnte
     */
    @SuppressWarnings("WeakerAccess")
    public static Node getNode(Collection<Node> nodes, String nodeID) {
        return nodes.stream().filter(a -> a.getId().equals(nodeID)).findFirst().orElse(null);
    }


    /**
     * Erzeugt alle Nodes mit ihren Nachbarn aus der »map«<br>
     *
     * @param map die map mit allen "Knoten"-Infos
     */
    @SuppressWarnings("WeakerAccess")
    public void setGraphFromMap(List<String> map) {
        int width = map.get(0).length();
        int height = map.size();
        List<Node> nodes = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map.get(i).charAt(j) != '#') nodes.add(new Node(getNodeID(map.get(i).charAt(j), j, i)));
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                addNeighbor(nodes, getNodeID(map.get(i).charAt(j), j, i), map, i, j);
            }
        }

        this.nodes = nodes;

//        System.out.println(nodes.stream().map(it->it.getId()).collect(Collectors.joining(",  ","","")));
    }

    private void addNeighbor(List<Node> nodes, String myId, List<String> map, int i, int j) {
        Node myself = getNode(nodes, myId);
        if (myself == null) return;
        Node[] possibleNeighbors = new Node[]{
                getNode(nodes, getNodeID(map.get(i - 1).charAt(j), j, i - 1)),
                getNode(nodes, getNodeID(map.get(i + 1).charAt(j), j, i + 1)),
                getNode(nodes, getNodeID(map.get(i).charAt(j + 1), j + 1, i)),
                getNode(nodes, getNodeID(map.get(i).charAt(j - 1), j - 1, i)),
        };
        for (Node n : possibleNeighbors) {
            if (n != null) {
                int dist = getDist(myself, n);
                n.addNeighbour(new Neighbour(myself, dist));
                myself.addNeighbour(new Neighbour(n, dist));
            }
        }
    }

    private int getDist(Node myself, Node n) {
        return (myself.getId().startsWith("R") && !n.getId().startsWith("R")) || (!myself.getId().startsWith("R") && n.getId().startsWith("R")) ? 0 : 1;
    }


    /**
     * Liefert eine Collection mit allen AWAB-Nodes
     *
     * @return die AWABs
     */
    @SuppressWarnings("WeakerAccess")
    public Collection<Node> getAllAWABs() {
        return new ArrayList<Node>(nodes.stream().filter(a -> a.getId().startsWith("A")).toList());
    }


    /**
     * Liefert die nächstliegende Ladestation ('R')
     *
     * @return die nächstliegende Ladestation oder »null«, falls keine Ladestation erreichbar ist
     */
    @SuppressWarnings("WeakerAccess")
    public Node getNearestRechargeStation() {
        return nodes.stream().filter(a -> a.getId().startsWith("R") && a.isVisited()).min(Comparator.comparingInt(Node::getTotalDistance)).orElse(null);
    }


    /**
     * Liefere den Pfad (als String) zur nächstliegenden Ladestation.<br>
     * Falls keine Ladestation erreichbar ist, dann wird der Text "no recharge station found"
     * geliefert.
     *
     * @return der Pfad als Text oder eine Fehlermeldung ("no recharge station found"), falls keine Ladestation erreichbar ist
     */
    @SuppressWarnings("WeakerAccess")
    public String getPathToNearestRechargeStation() {
        Node nearestRechargeStation = getNearestRechargeStation();
        if (nearestRechargeStation == null) return "no recharge station found";
        return nearestRechargeStation.getPath();
    }

    /**
     * Demonstriert wie für alle AWABs einer Map der kürzeste Pfad zur nächstgelegenen Ladestation ermittelt werden kann.
     * Das File mit der Map wird durch die Variable »filename« festgelegt.
     * <p>
     * An dieser Methode sollte nichts geändert werden müssen.
     *
     * @param args unused
     * @throws IOException falls der Graph nicht gelesen werden konnte
     */
    public static void main(String[] args) throws IOException {
//        Graph graph = new Graph();
//
//        String filename = "example_1.map";
//        graph.readGraphFromMap(new File("res/i_matura_2019/" + filename).toPath());
//
//        System.out.println("loaded map: " + filename);
//        System.out.println();
//
//        System.out.println();
//        System.out.println("print all paths:");
//        System.out.println(graph.getAllPaths());
//        System.out.println();
//
//        System.out.println("print graph:");
//        System.out.println(graph);
//        System.out.println();
//
//        for (Node AWAB : graph.getAllAWABs()) {
//            graph.calcWithDijkstra(AWAB.getId());
//            System.out.println();
//            System.out.println("--------------------------------------------------------------------------------------");
//            System.out.println();
//            System.out.println("calc for AWAB " + AWAB.getId());
//            System.out.println();
//            System.out.println(graph.getAllPaths());
//            System.out.println();
//            System.out.println("AWAB " + AWAB.getId() + " drives to the recharge station: " + graph.getNearestRechargeStation().getId());
//            System.out.println("the path to the recharge station is: " + graph.getPathToNearestRechargeStation());
//        }

        testGraphWithAdjazenzMatrix();
    }


    /**
     * Demonstriert den Dijkstra-Algorithmus für einen normalen Graphen mit Adjazenz-Matrix.<br>
     * Wird derzeit nicht aufgerufen!
     *
     * @throws IOException falls die Datei nicht gelesen werden konnte
     */
    @SuppressWarnings(value = "unused")
    public static void testGraphWithAdjazenzMatrix() throws IOException {
        Graph graph = new Graph();

        String filename = "Graph_A-H.csv";
        graph.readGraphFromAdjazenzMatrixFile(new File("res/i_matura_2019/" + filename).toPath());
        System.out.println("loaded map: " + filename);
        System.out.println();

        System.out.println("print all paths (no paths available because dijkstra wasn't started:");
        System.out.println(graph.getAllPaths());
        System.out.println();

        System.out.println("print graph:");
        System.out.println(graph);
        System.out.println();

        System.out.println("calc and print all shortest paths for startnode: " + graph.nodes.get(0).getId());
        graph.calcWithDijkstra(0); // Knoten 0 --> Knoten "A"
        System.out.println(graph.getAllPaths());
        System.out.println();

        System.out.println("calc and print all shortest paths for startnode: " + graph.nodes.get(4).getId());
        graph.calcWithDijkstra(4);
        System.out.println(graph.getAllPaths());
    }
}
