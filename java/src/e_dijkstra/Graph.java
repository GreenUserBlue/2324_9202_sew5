package e_dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Graph {

    private PriorityQueue<Node> priorityQueue = new PriorityQueue<>((a, b) -> {
        return 0;
    });

    private List<Node> allNodes = new ArrayList<>();
}
