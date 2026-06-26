import java.util.*;

class Dijkstra {

    static class Node {
        int vertex;
        int distance;

        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    static final int V = 9;

    // Add Edge
    static void addEdge(List<List<Node>> graph, int u, int v, int weight) {
        graph.get(u).add(new Node(v, weight));
        graph.get(v).add(new Node(u, weight));
    }

    // Print shortest path
    static void printPath(int vertex, int[] pred) {

        if (vertex == -1)
            return;

        printPath(pred[vertex], pred);
        System.out.print(vertex + " ");
    }

    // Get edge weight between two vertices
    static int getWeight(List<List<Node>> graph, int u, int v) {

        for (Node neighbor : graph.get(u)) {

            if (neighbor.vertex == v) {
                return neighbor.distance;
            }
        }

        return -1;
    }

    static void Dijkstra(List<List<Node>> graph, int s) {

        int[] d = new int[V];
        int[] pred = new int[V];
        String[] color = new String[V];

        // Initialize
        for (int u = 0; u < V; u++) {
            d[u] = Integer.MAX_VALUE;
            pred[u] = -1;
            color[u] = "WHITE";
        }

        d[s] = 0;

        PriorityQueue<Node> Q =
                new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));

        Q.add(new Node(s, 0));

        while (!Q.isEmpty()) {

            Node current = Q.poll();      // Extract-Min(Q)

            int u = current.vertex;

            if (color[u].equals("BLACK"))
                continue;

            for (Node neighbor : graph.get(u)) {

                int v = neighbor.vertex;
                int weight = neighbor.distance;

                // Relaxation
                if (d[u] != Integer.MAX_VALUE &&
                        d[u] + weight < d[v]) {

                    d[v] = d[u] + weight;

                    pred[v] = u;

                    // Decrease-Key(Q,v,d[v])
                    Q.add(new Node(v, d[v]));
                }
            }

            color[u] = "BLACK";
        }

        // Print Distances and Paths
        System.out.println("\n===== SHORTEST PATHS CALCULATED =====\n");

        System.out.printf("%-10s %-12s %-25s%n",
                "Vertex", "Distance", "Path");

        for (int i = 0; i < V; i++) {

            System.out.printf("%-10d %-12d ", i, d[i]);

            printPath(i, pred);

            System.out.println();
        }

        // Print Shortest Path Tree
        System.out.println("\n===== SHORTEST PATH TREE =====\n");

        System.out.printf("%-10s %-10s %-10s%n",
                "From", "To", "Weight");

        for (int i = 0; i < V; i++) {

            if (pred[i] != -1) {

                int weight = getWeight(graph, pred[i], i);

                System.out.printf("%-10d %-10d %-10d%n",
                        pred[i], i, weight);
            }
        }
    }

    public static void main(String[] args) {

        List<List<Node>> graph = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            graph.add(new ArrayList<>());
        }

        addEdge(graph, 0, 1, 4);
        addEdge(graph, 0, 7, 8);

        addEdge(graph, 1, 2, 8);
        addEdge(graph, 1, 7, 11);

        addEdge(graph, 2, 3, 7);
        addEdge(graph, 2, 5, 4);
        addEdge(graph, 2, 8, 2);

        addEdge(graph, 3, 4, 9);
        addEdge(graph, 3, 5, 14);

        addEdge(graph, 4, 5, 10);

        addEdge(graph, 5, 6, 2);

        addEdge(graph, 6, 7, 1);
        addEdge(graph, 6, 8, 6);

        addEdge(graph, 7, 8, 7);

        int source = 0;

        System.out.println("Source Vertex = " + source);

        Dijkstra(graph, source);
    }
}