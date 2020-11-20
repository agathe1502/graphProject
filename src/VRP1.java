import org.jgrapht.util.FibonacciHeap;
import org.jgrapht.util.FibonacciHeapNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Classe qui représente un VRP qui doit se rendre chaque jour dans une grande ville.
 */
public class VRP1 {

    /**
     * La liste d'adjacence de l'algorithme
     */
    private ArrayList<LinkedList<Integer>> adjacentList;
    /**
     * Le graphe utilisé par l'algorithme
     */
    private Graph graph;
    /**
     * Un tas de Fibonacci déterminant les valeurs moyennes pour aller de chacune des communes jusqu'au grandes villes.
     */
    private FibonacciHeap<Integer> averages;

    /**
     * Constructeur qui initialise le nom de fichier avec le fichier spécifique au VRP1
     */
    public VRP1() {
        graph = new Graph("src/files/File_VRP1.txt");
        adjacentList = graph.getAdjacentList();
    }

    /**
     * Permet de lancer l'algorithme
     * - Création de la liste d'adjacence
     * - Calcul des moyennes pour aller de chacune des communes vers chacune des grandes villes.
     *
     * @throws IOException
     */
    public void runAlgorithm() throws IOException {
        // Read the file to create the adjacent list
        CSVtoTXT fileCSV = new CSVtoTXT();
        fileCSV.fileConversion(0, 0, false);
        graph.createAdjacentListVRP1(fileCSV.getPopulations(), 200000);

        // Given
        ArrayList<Edge> edges = graph.getEdges();
        ArrayList<Vertex> vertices = graph.getVertices();
        averages = new FibonacciHeap<>();

        for (LinkedList<Integer> startCityList : adjacentList) {
            double sum = 0;
            int loop = 0;
            for (Integer edgeId : startCityList) {
                if (loop != 0) {
                    Edge e = edges.get(edgeId);
                    double value = graph.getValueByVertices(vertices.get(e.getInitialVertex()), vertices.get(e.getFinalVertex()));
                    if (value == -1) {
                        value = graph.getValueByVertices(vertices.get(e.getFinalVertex()), vertices.get(e.getInitialVertex()));
                    }
                    sum += value;
                }
                loop++;
            }
            averages.insert(new FibonacciHeapNode<>(startCityList.get(0)), sum / Double.parseDouble(String.valueOf(startCityList.size() - 1)));
        }
    }

    /**
     * Retourne le graphe utilisé par l'algorithme
     *
     * @return
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Permet l'affichage des résultats de l'algorithme:
     * - Ou est ce que le VRP doit habiter
     * - La distance à vol d'oiseau avec chacune des grandes villes
     * - La distance moyenne pour aller vers chacune de ces villes
     */
    public void displayResults() {
        int cityId = averages.min().getData();
        ArrayList<Edge> edges = graph.getEdges();
        ArrayList<Vertex> vertices = graph.getVertices();

        System.out.println("The VRP should live in " + graph.getVertices().get(cityId).getName());
        int loop = 0;
        for (Integer edgeId : adjacentList.get(cityId)) {
            if (loop != 0) {
                Edge e = edges.get(edgeId);
                int otherVertex = e.getOtherVertex(cityId);

                double value = graph.getValueByVertices(vertices.get(e.getInitialVertex()), vertices.get(e.getFinalVertex()));
                if (value == -1) {
                    value = graph.getValueByVertices(vertices.get(e.getFinalVertex()), vertices.get(e.getInitialVertex()));
                }
                System.out.println("- " + vertices.get(otherVertex).getName() + " : " + value + " km");

            }
            loop++;
        }
        System.out.println("--> Average : " + String.format("%.2f", averages.min().getKey()) + " km");

    }
}
