import io.vertx.core.Vertx;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Démarrage de l'application");
        final Vertx vertx = Vertx.vertx();
        //vertx.deployVerticle(new ApiClass());

        int dmax = 10;
        int popmin = 5000;
        int srcVertex = 5;
        int endVertex = 6;

        //createdFile(dmax, popmin);
        //dijkstra(dmax, popmin, srcVertex, endVertex);
        //dijkstraFibo(dmax, popmin, srcVertex, endVertex);
        //VRP1();
        Astar(dmax, popmin, srcVertex, endVertex);
        //VRP2(popmin, srcVertex);
    }

    private static void createdFile(int dmax, int popmin) {
        CSVtoTXT fileCSV = new CSVtoTXT();

        String fileName = "src/files/Communes_" + dmax + "_" + popmin + ".txt";
        File file = new File(fileName);
        fileCSV.fileConversion(dmax, popmin, !file.isFile());
    }

    private static Graph initializeDijkstra(int dmax, int popmin) {
        CSVtoTXT fileCSV = new CSVtoTXT();

        String fileName = "src/files/Communes_" + dmax + "_" + popmin + ".txt";
        File file = new File(fileName);
        fileCSV.fileConversion(dmax, popmin, !file.isFile());

        Graph graph = new Graph(fileName);
        graph.createAdjacentList();

        return graph;
    }

    private static void dijkstra(int dmax, int popmin, int srcVertex, int endVertex) {

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

    private static void dijkstraFibo(int dmax, int popmin, int srcVertex, int endVertex) {

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

    private static void Astar(int dmax, int popmin, int srcVertex, int endVertex) {
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

    private static void VRP2(int popmin, int srcVertex) {
        CSVtoTXT fileCSV = new CSVtoTXT();
        fileCSV.fileConversion(0, popmin, false);

        if (popmin == 100000 || popmin == 150000 || popmin == 200000) {
            String filename = "src/files/File_VRP2_" + popmin + ".txt";
            Graph graph = new Graph(filename);
            graph.createAdjacentList();

            VRP2 vpr = new VRP2(graph, graph.getVertices().get(srcVertex));
            System.out.println("-------------------");
            vpr.runAlgorithm();
        } else {
            System.out.println("La valeur de la population minimum n'est pas correcte, elle doit valoir soit 100000, 150000 ou 200000");
        }

    }

}
