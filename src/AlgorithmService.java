import models.CityModel;
import models.ShortestPathAlgorithmModel;
import models.VRP1Model;

import java.io.File;
import java.util.ArrayList;

/**
 * Service qui permet de faire le lien entre le back-end et ls classes métiers
 */
public class AlgorithmService {

    /**
     * Méthode qui permet de lancer l'algorithme de Dijkstra et d'afficher le résultat
     * -> Le temps de la simulation
     * -> La distance trouvée
     * -> Le plus court chemin déterminé entre les 2 villes données
     * @param dmax la distance maximale entre 2 villes
     * @param popmin la population minimale de chaque ville
     * @param srcVertex le sommet de départ
     * @param endVertex le sommet d'arrivée
     * @return le modèle du résultat de l'algorithme
     */
    private static ShortestPathAlgorithmModel dijkstra(int dmax, int popmin, int srcVertex, int endVertex) {

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
        return new ShortestPathAlgorithmModel(d.getDistance(graph.getVertices().get(endVertex)), (timeElapsed / 1000000.0), d.getPathModel(graph.getVertices().get(endVertex), graph));
    }

    /**
     * Méthode qui permet d'initialiser les algorithmes de Dijkstra
     *  - Génère le nom du fichier
     *  - Crée le fichier txt du graphe
     *  - Crée la liste d'adjacence
     * @param dmax la distance maximale entre 2 villes
     * @param popmin la population minimale de chaque ville
     * @return le graphe créé
     */
    private static Graph initializeDijkstra(int dmax, int popmin) {
        CSVtoTXT fileCSV = new CSVtoTXT();

        String fileName = "src/files/Communes_" + dmax + "_" + popmin + ".txt";
        File file = new File(fileName);
        fileCSV.fileConversion(dmax, popmin, !file.isFile());

        Graph graph = new Graph(fileName);
        graph.setFileCSV(fileCSV);
        graph.createAdjacentList();

        return graph;
    }

    /**
     * Méthode qui permet de lancer l'algorithme de Dijkstra avec le tas de Fibonacci et d'afficher le résultat
     * -> Le temps de la simulation
     * -> La distance trouvée
     * -> Le plus court chemin déterminé entre les 2 villes données
     * @param dmax la distance maximale entre 2 villes
     * @param popmin la population minimale de chaque ville
     * @param srcVertex le sommet de départ
     * @param endVertex le sommet d'arrivée
     * @return le modèle du résultat de l'algorithme
     */
    private static ShortestPathAlgorithmModel dijkstraFibo(int dmax, int popmin, int srcVertex, int endVertex) {

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
        return new ShortestPathAlgorithmModel(df.getDistance(graph.getVertices().get(endVertex)), (timeElapsed2 / 1000000.0), df.getPathModel(graph.getVertices().get(endVertex), graph));

    }

    /**
     * Méthode qui permet de lancer l'algorithme A* et d'afficher le résultat
     * -> Le temps de la simulation
     * -> La distance trouvée
     * -> Le plus court chemin déterminé entre les 2 villes données
     * @param dmax la distance maximale entre 2 villes
     * @param popmin la population minimale de chaque ville
     * @param srcVertex le sommet de départ
     * @param endVertex le sommet d'arrivée
     * @return le modèle du résultat de l'algorithme
     */
    private static ShortestPathAlgorithmModel astar(int dmax, int popmin, int srcVertex, int endVertex) {
        CSVtoTXT fileCSV = new CSVtoTXT();

        String fileName = "src/files/Communes_" + dmax + "_" + popmin + ".txt";
        File file = new File(fileName);
        fileCSV.fileConversion(dmax, popmin, !file.isFile());

        Graph graph = new Graph(fileName);
        graph.setFileCSV(fileCSV);
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
        return new ShortestPathAlgorithmModel(as.getDistance(graph.getVertices().get(endVertex)), (timeElapsed3 / 1000000.0), as.getPathModel(graph.getVertices().get(endVertex), graph));

    }

    /**
     * Méthode qui permet de lancer l'algorithme de Dijkstra
     * @return le modèle du résultat de l'algorithme
     */
    public ShortestPathAlgorithmModel runDijkstra(int dmax, int popmin, int srcVertex, int endVertex) {
        return dijkstra(dmax, popmin, srcVertex, endVertex);
    }

    /**
     * Méthode qui permet de lancer l'algorithme de Dijkstra avec le tas de Fibonacci
     * @return le modèle du résultat de l'algorithme
     */
    public ShortestPathAlgorithmModel runDijkstraFibo(int dmax, int popmin, int srcVertex, int endVertex) {
        return dijkstraFibo(dmax, popmin, srcVertex, endVertex);
    }

    /**
     * Méthode qui permet de lancer l'algorithme d'A*
     * @return le modèle du résultat de l'algorithme
     */
    public ShortestPathAlgorithmModel runAstar(int dmax, int popmin, int srcVertex, int endVertex) {
        return astar(dmax, popmin, srcVertex, endVertex);
    }

    /**
     * Méthode qui permet de lancer l'algorithme du VRP1 et d'afficher le résultat
     * @return le modèle du résultat de l'algorithme
     */
    public VRP1Model runVRP1(){
        VRP1 vrp = new VRP1();
        vrp.runAlgorithm();
        System.out.println("-------------------");
        return vrp.displayResults();

    }

    /**
     * Méthode qui permet de lancer l'algorithme du VRP2 et d'afficher le résultat
     * @return le modèle du résultat de l'algorithme
     */
    public ShortestPathAlgorithmModel runVRP2(int popmin, int srcVertex){
        CSVtoTXT fileCSV = new CSVtoTXT();
        fileCSV.fileConversion(0, popmin, false);

        String filename = "src/files/File_VRP2_" + popmin + ".txt";
        Graph graph = new Graph(filename);
        graph.setFileCSV(fileCSV);
        graph.createAdjacentList();

        VRP2 vrp = new VRP2(graph, graph.getVertices().get(srcVertex));
        System.out.println("-------------------");
        long startTime = System.nanoTime();
        vrp.runAlgorithm();
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        final ArrayList<CityModel> cycle = vrp.getCycle();
        return new ShortestPathAlgorithmModel(vrp.getDistance(), (timeElapsed / 1000000.0), cycle);


    }
}
