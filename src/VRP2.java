import java.util.ArrayList;

/**
 * Classe qui représente un VRP qui doit visiter des villes l'une après après sans y passer deux fois et en revenant à son point de départ
 */
public class VRP2 {
    /**
     * Le graphe utilisé par l'algorithme
     */
    private final Graph graph;
    /**
     * Tableau qui permet de savoir si un sommet a été traité
     */
    private final boolean[] isUsed;
    /**
     * Le sommet de départ
     */
    private final Vertex startVertex;
    /**
     * Liste contenant les sommets qui correpond au chemin effectué par le VRP
     */
    private final ArrayList<Vertex> cycle;
    /**
     * Distance parcourue par le VRP
     */
    private double distance;

    /**
     * Constructeur qui initialise les paramètres permettant de faire fonctionner l'algorithme
     * @param g graphe dans lequel l'algorithme va s'appliquer
     * @param startVertex Sommet de départ
     */
    public VRP2(Graph g, Vertex startVertex) {
        this.graph = g;
        this.isUsed = new boolean[graph.getVertices().size()];
        this.startVertex = startVertex;
        this.cycle = new ArrayList<>();
        this.distance = 0;
    }

    /**
     * Méthode qui permet de lancer l'algorithme
     * - A partir d'un sommet, on parcourt tous les arcs liés pour trouver la istance la plus courte
     * - A la fin, Affichage du cycle obtenu et de la distance totale
     */
    public void runAlgorithm() {
        cycle.add(startVertex);
        int idVertex = startVertex.getId();
        while(cycle.size() < graph.getVertices().size()) {
            idVertex = getIndexOfMin(idVertex);
            if (idVertex != -1){
                isUsed[idVertex] = true;
                cycle.add(graph.getVertices().get(idVertex));
            }
        }
        distance += graph.getValueByVertices(graph.getVertices().get(idVertex), startVertex);
        cycle.add(startVertex);
        System.out.println("Distance : " + distance + " km");
        displayCycle();
    }

    /**
     * Méthode qui permet de renvoie l'id du sommet ayant la distance de la plus courte
     * Le sommet doit être différent du sommet de départ tant que tous les autres sommets n'ont pas été parcourus et pas encore traité
     * @param idVertex id du sommet initial de l'arc
     * @return l'id du sommet final de l'arc avec la distance la plus courte
     */
    private int getIndexOfMin(int idVertex){
        int loop = 0;
        double min = Double.POSITIVE_INFINITY;
        int index = -1;
        double tmpDistance = 0;
        for (Integer edgeId : graph.getAdjacentList().get(idVertex)) {
            if (loop != 0) {
                Edge edge = graph.getEdges().get(edgeId);
                double value = edge.getValue();
                int idSuccessorVertex = edge.getOtherVertex(idVertex);
                if (!isUsed[idSuccessorVertex] && idSuccessorVertex != startVertex.getId()) {
                    if (value < min) {
                        min = value;
                        index = idSuccessorVertex;
                        tmpDistance = value;
                    }
                }
            }
            loop++;
        }
        if (index != -1) {
            distance += tmpDistance;
        }
        return index;
    }

    /**
     * Affichage du cycle
     */
    private void displayCycle() {
        System.out.print("[ ");
        for (Vertex vertex : cycle) {
            System.out.print(vertex.getName() + " ");
        }
        System.out.print("]");
    }

}
