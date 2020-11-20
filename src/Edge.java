/**
 * Classe qui permet de créer un sommet.
 */
public class Edge {

    /**
     * Id de l'arc dans le graphe
     */
    private final int id;
    /**
     * Id du sommet de départ dans le graphe
     */
    private final int initialVertex;
    /**
     * Id du sommet final dans le graphe
     */
    private final int finalVertex;
    /**
     * Valeur de l'arc
     */
    private final double value;

    /**
     * Constructeur de l'arc
     *
     * @param id            son id dans le graphe
     * @param initialVertex l'id de son sommet de départ
     * @param finalVertex   l'id de son sommet final
     * @param value         sa valeur
     */
    public Edge(int id, int initialVertex, int finalVertex, double value) {
        this.id = id;
        this.initialVertex = initialVertex;
        this.finalVertex = finalVertex;
        this.value = value;
    }

    /**
     * Retourne l'id de l'arc
     *
     * @return l'id de l'arc
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne sommet de départ de l'arc
     *
     * @return le sommet de départ de l'arc
     */
    public int getInitialVertex() {
        return initialVertex;
    }

    /**
     * Retourne le sommet de terminaison de l'arc
     *
     * @return le sommet de terminaison de l'arc
     */
    public int getFinalVertex() {
        return finalVertex;
    }

    /**
     * Retourne la valeur de l'arc
     *
     * @return la valeur de l'arc
     */
    public double getValue() {
        return value;
    }

    /**
     * Permet de récupérer :
     * - le sommet de départ de l'arc, à partir de son sommet final
     * - le sommet final de l'arc à partir de son sommet de départ
     *
     * @param vertex l'id du sommet connu
     * @return l'autre id du sommet qui compose l'arc
     */
    public int getOtherVertex(int vertex) {
        return vertex == initialVertex ? finalVertex : initialVertex;
    }
}
