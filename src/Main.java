import io.vertx.core.Vertx;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Démarrage de l'application");
        final Vertx vertx = Vertx.vertx();
        // à commenter pour utiliser la console et pas l'interface graphique
        vertx.deployVerticle(new ApiClass());

        // A changer pour adapter les paramètres
        int dmax = 50;
        int popmin = 5000;
        int srcVertex = 5;
        int endVertex = 6;

        // à décommenter cette ligne pour utiliser le main dans la console
        //createdFile(dmax, popmin);

        // à décommenter pour lancer Dijkstra
        //dijkstra(dmax, popmin, srcVertex, endVertex);

        // à décommenter pour lancer Dijkstra avec un tas de Fibonacci
        //dijkstraFibo(dmax, popmin, srcVertex, endVertex);

        // à décommenter pour lancer l'algorithme du 1er VRP
        //VRP1();

        // à décommenter pour lancer Astar
        //Astar(dmax, popmin, srcVertex, endVertex);

        // à décommenter pour lancer l'algorithme du 2ème VRP
        //VRP2(popmin, srcVertex);
    }

    /**
     * Méthode qui permet de créer un fichier avec les paramètres entrés (s'il n'existe pas déjà)
     * @param dmax la distance maximale entre 2 villes
     * @param popmin la population minimale de chaque ville
     */
    private static void createdFile(int dmax, int popmin) {
        CSVtoTXT fileCSV = new CSVtoTXT();

        String fileName = "src/files/Communes_" + dmax + "_" + popmin + ".txt";
        File file = new File(fileName);
        fileCSV.fileConversion(dmax, popmin, !file.isFile());
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
        graph.createAdjacentList();

        return graph;
    }

    /**
     * Méthode qui permet de lancer l'algorithme de Dijkstra et d'afficher le résultat
     * -> Le temps de la simulation
     * -> La distance trouvée
     * -> Le plus court chemin déterminé entre les 2 villes données
     * @param dmax la distance maximale entre 2 villes
     * @param popmin la population minimale de chaque ville
     * @param srcVertex le sommet de départ
     * @param endVertex le sommet d'arrivée
     */
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

    /**
     * Méthode qui permet de lancer l'algorithme de Dijkstra avec le tas de Fibonacci et d'afficher le résultat
     * -> Le temps de la simulation
     * -> La distance trouvée
     * -> Le plus court chemin déterminé entre les 2 villes données
     * @param dmax la distance maximale entre 2 villes
     * @param popmin la population minimale de chaque ville
     * @param srcVertex le sommet de départ
     * @param endVertex le sommet d'arrivée
     */
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

    /**
     * Méthode qui permet de lancer l'algorithme A* et d'afficher le résultat
     * -> Le temps de la simulation
     * -> La distance trouvée
     * -> Le plus court chemin déterminé entre les 2 villes données
     * @param dmax la distance maximale entre 2 villes
     * @param popmin la population minimale de chaque ville
     * @param srcVertex le sommet de départ
     * @param endVertex le sommet d'arrivée
     */
    private static void Astar(int dmax, int popmin, int srcVertex, int endVertex) {
        CSVtoTXT fileCSV = new CSVtoTXT();

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

    /**
     * Méthode qui permet de lancer l'algorithme du VRP1 et d'afficher le résultat
     */
    private static void VRP1() {
        VRP1 vrp = new VRP1();
        vrp.runAlgorithm();
        System.out.println("-------------------");
        vrp.displayResults();
    }

    /**
     * Méthode qui permet de lancer l'algorithme du VRP2 et d'afficher le résultat
     */
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
