/**
 * Classe qui permet de créer un sommet dans un graphe
 */
public class Vertex {

    /**
     * Id du sommet
     */
    private int id;
    /**
     * Nom du sommet
     */
    private String name;

    /**
     * Constructeur du sommet
     *
     * @param id   du sommet
     * @param name du sommet
     */
    public Vertex(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Retourne l'id du sommet
     *
     * @return l'id du sommet
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le nom du sommet
     *
     * @return le nom du sommet
     */
    public String getName() {
        return name;
    }

}
