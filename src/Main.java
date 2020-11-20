import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {


        int dmax = 50;
        int popmin = 5000;
        int srcVertex = 1;
        int endVertex = 50;

        dijkstra(dmax, popmin, srcVertex, endVertex);
        dijkstraFibo(dmax, popmin, srcVertex, endVertex);
        //VRP1();
        Astar(dmax, popmin, srcVertex, endVertex);


    }

    private static Graph initializeDijkstra(int dmax, int popmin) throws IOException {
        CSVtoTXT fileCSV = new CSVtoTXT();

        // boolean createFile = existance de fileName
        String fileName = "src/files/Communes_" + dmax + "_" + popmin + ".txt";
        File file = new File(fileName);
        fileCSV.fileConversion(dmax, popmin, !file.isFile());

        Graph graph = new Graph(fileName);
        graph.createAdjacentList();

        return graph;
    }

    private static void dijkstra(int dmax, int popmin, int srcVertex, int endVertex) throws IOException {

        Graph graph = initializeDijkstra(dmax, popmin);

        long startTime = System.nanoTime();
        Dijkstra d = new Dijkstra(graph, graph.getVertices().get(srcVertex));
        d.runAlgorithm();
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("-------------------");
        System.out.println("Dijkstra : Execution time: " + (timeElapsed / 1000000.0) + " ms");
        System.out.print("Path :");
        System.out.println(d.getPath(graph.getVertices().get(endVertex)));
        System.out.println("Distance : " + d.getDistance(graph.getVertices().get(endVertex)) + " km");
    }

    private static void dijkstraFibo(int dmax, int popmin, int srcVertex, int endVertex) throws IOException {

        Graph graph = initializeDijkstra(dmax, popmin);

        DijkstraFibonacciHeap df = new DijkstraFibonacciHeap(graph, graph.getVertices().get(srcVertex));
        long startTime2 = System.nanoTime();
        df.runAlgorithm();
        long endTime2 = System.nanoTime();
        long timeElapsed2 = endTime2 - startTime2;
        System.out.println("-------------------");
        System.out.println("Dijkstra Fibonacci Heap: Execution time : " + (timeElapsed2 / 1000000.0) + " ms");
        System.out.print("Path :");
        System.out.println(df.getPath(graph.getVertices().get(endVertex)));
        System.out.println("Distance : " + df.getDistance(graph.getVertices().get(endVertex)) + " km");
    }

    private static void Astar(int dmax, int popmin, int srcVertex, int endVertex) throws IOException {
        CSVtoTXT fileCSV = new CSVtoTXT();

        // boolean createFile = existance de fileName
        String fileName = "src/files/Communes_" + dmax + "_" + popmin + ".txt";
        File file = new File(fileName);
        fileCSV.fileConversion(dmax, popmin, !file.isFile());

        Graph graph = new Graph(fileName);
        graph.createAdjacentList();

        Astar as = new Astar(graph, graph.getVertices().get(srcVertex), graph.getVertices().get(endVertex), fileCSV.getCoordinates());
        long startTime3 = System.nanoTime();
        as.runAlgorithm();
        long endTime3 = System.nanoTime();
        long timeElapsed3 = endTime3 - startTime3;
        System.out.println("-------------------");
        System.out.println("Astar : Execution time: " + (timeElapsed3 / 1000000.0) + " ms");
        System.out.print("Path :");
        System.out.println(as.getPath(graph.getVertices().get(endVertex)));
        System.out.println("Distance : " + as.getDistance(graph.getVertices().get(endVertex)) + " km");
    }

    private static void VRP1() throws IOException {
        VRP1 vrp = new VRP1();
        vrp.runAlgorithm();
        System.out.println("-------------------");
        vrp.displayResults();
    }

}
