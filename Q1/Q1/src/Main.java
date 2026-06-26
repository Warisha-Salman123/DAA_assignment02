import java.util.*;

class EdmondsKarp {

    private int vertices;

    public EdmondsKarp(int vertices) {
        this.vertices = vertices;
    }

    // BFS for EdmondsKarp
    private boolean bfs(int[][] residualGraph, int source, int sink, int[] parent) {

        boolean[] visited = new boolean[vertices];

        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;
        parent[source] = -1;

        while (!queue.isEmpty()) {

            int u = queue.poll();

            for (int v = 0; v < vertices; v++) {

                if (!visited[v] && residualGraph[u][v] > 0) {

                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        return visited[sink];
    }

    // Minimum Cut
    private void printMinCut(int[][] originalGraph,
                             int[][] residualGraph,
                             int source) {

        boolean[] visited = new boolean[vertices];

        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;

        while (!queue.isEmpty()) {

            int u = queue.poll();

            for (int v = 0; v < vertices; v++) {

                if (!visited[v] && residualGraph[u][v] > 0) {
                    visited[v] = true;
                    queue.add(v);
                }
            }


        }

        System.out.println("\nMinimum Cut Edges:");

        for (int i = 0; i < vertices; i++) {

            for (int j = 0; j < vertices; j++) {

                if (visited[i]
                        && !visited[j]
                        && originalGraph[i][j] > 0) {

                    System.out.println(
                            getNodeName(i)
                                    + " -> "
                                    + getNodeName(j)
                                    + " (Capacity = "
                                    + originalGraph[i][j]
                                    + ")");
                }
            }
        }
    }

    // EdmondsKarp Maximum Flow
    public int maxFlow(int[][] graph, int source, int sink) {

        int[][] residualGraph = new int[vertices][vertices];

        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                residualGraph[i][j] = graph[i][j];
            }
        }

        int[] parent = new int[vertices];

        int maxFlow = 0;

        while (bfs(residualGraph, source, sink, parent)) {

            int pathFlow = Integer.MAX_VALUE;

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
            }

            System.out.print("Augmenting Path: ");

            List<Integer> path = new ArrayList<>();

            for (int v = sink; v != source; v = parent[v]) {
                path.add(v);
            }

            path.add(source);
            Collections.reverse(path);

            for (int node : path) {
                System.out.print(getNodeName(node) + " ");
            }

            System.out.println("| Flow = " + pathFlow);

            for (int v = sink; v != source; v = parent[v]) {

                int u = parent[v];

                residualGraph[u][v] -= pathFlow;
                residualGraph[v][u] += pathFlow;
            }

            maxFlow += pathFlow;
        }

        printMinCut(graph, residualGraph, source);

        return maxFlow;
    }

    // Converting index numbers to vertex name:
    private static String getNodeName(int index) {

        switch (index) {
            case 0: return "A";
            case 1: return "B";
            case 2: return "C";
            case 3: return "D";
            case 4: return "E";
            case 5: return "F";
            case 6: return "G";
            default: return "?";
        }
    }

    public static void main(String[] args) {

        int V = 7;

        EdmondsKarp ek = new EdmondsKarp(V);

        int[][] graph = new int[V][V];

        // A
        graph[0][3] = 3; // A -> D
        graph[0][1] = 3; // A -> B

        // B
        graph[1][2] = 4; // B -> C

        // C
        graph[2][0] = 3; // C -> A
        graph[2][3] = 1; // C -> D
        graph[2][4] = 2; // C -> E

        // D
        graph[3][4] = 2; // D -> E
        graph[3][5] = 6; // D -> F

        // E
        graph[4][1] = 1; // E -> B
        graph[4][6] = 1; // E -> G

        // F
        graph[5][6] = 9; // F -> G

        int source = 0; // A
        int sink = 6;   // G

        int maxFlow = ek.maxFlow(graph, source, sink);

        System.out.println("\nMaximum Flow = " + maxFlow);
    }
}