import java.util.ArrayList;

/**
 * Classe abstraite qui permet de lancer un algorithme de Dijkstra
 */
public abstract class DijkstraAlgorithm extends ShortestPathAlgorithm {

    /**
     * Le tableau des plus courtes distances de chacun des sommets (par rapport au sommet de référence)
     */
    protected static double[] lembda;
    /**
     * Les sommets du graphe
     */
    protected static ArrayList<Vertex> vertices;
    /**
     * Les sommets non traités du graphe (ceux dont on ne connait pas encore la distance la plus courte)
     */
    protected static ArrayList<Vertex> Z;
    /**
     * Un tableau pour savoir si les sommets sont traités ou non
     */
    protected static boolean[] isUsedTab;
    /**
     * Le graphe sur lequel l'algorithme va s'appliquer
     */
    protected final Graph G;
    /**
     * Le sommet de référence
     */
    protected final Vertex startVertex;


    /**
     * Constructeur de l'algorithme
     *
     * @param g      le graphe auquel il va être appliqué
     * @param vertex le sommet de référence
     */
    protected DijkstraAlgorithm(Graph g, Vertex vertex) {
        G = g;
        startVertex = vertex;
        lembda = new double[G.getVertices().size()];
        vertices = G.getVertices();
        Z = (ArrayList<Vertex>) vertices.clone();
        isUsedTab = new boolean[G.getVertices().size()];
        pathArray = new Vertex[vertices.size()];
    }

    /**
     * Méthode qui permet de lancer l'algorithme de Dijkstra.
     * - Phase d'initialisation
     * - Phase qui analyse chaque sommet dont la distance la plus courte n'a pas été trouvée
     *
     * @return le tableau des plus courtes distances (par rapport au sommet de départ) déterminées par l'algorithme
     */
    protected double[] runAlgorithm() {
        initialization();
        analyzeUnmarkedVertices();
        return lembda;
    }

    /**
     * Méthode qui permet de retrouver la plus courte distance d'un sommet par rapport au sommet de départ
     *
     * @param endVertex le sommet dont on veut la distance
     * @return la distance du plus court chemin du sommet de départ à un sommet donné
     */
    public double getDistance(Vertex endVertex) {
        return lembda[endVertex.getId()];
    }

    /**
     * Phase d'initialisation de l'algorithme.
     */
    protected abstract void initialization();

    /**
     * Phase qui boucle sur la liste des sommets dont la distance la plus courte n'est pas encore determinée.
     */
    protected abstract void analyzeUnmarkedVertices();


    /**
     * Méthode qui permet de mettre à jour la distance des sommets de fin d'un arc donné
     *
     * @param edge l'arc dont il va falloir mettre à jour la distance de son sommet de fin
     * @param min  l'id du sommet de départ de l'arc (pour pouvoir déterminer quel est le sommet de fin de l'arc)
     */
    protected abstract void modifyDistance(Edge edge, int min);

    /**
     * Méthode qui permet d'analyser les arcs adjacents à un sommet donné.
     * Elle permet également de mettre à jour la distance des sommets de fin de chacun des arcs.
     *
     * @param min l'id du sommet non traité qui a la distance minimale avec le sommet de départ
     */
    protected void analyzeAdjacentsEdges(int min) {
        int loop = 0;
        for (Integer edgeId : G.getAdjacentList().get(min)) { // analyze adjacent edges
            // the first one is the Vertex Id
            if (loop != 0) {
                // we have edges adjacent to the vertex x
                // we get from each edge the vertex which is not x
                Edge edge = G.getEdges().get(edgeId);
                modifyDistance(edge, min);
            }
            loop++;
        }
    }
}
