import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe abstraite qui est utilisée pour les algorithmes de plus court chemin
 */
public abstract class ShortestPathAlgorithm {

    /**
     * Le tableau qui recense les parents de chacun des sommets
     */
    protected static Vertex[] pathArray;

    /**
     * Méthode qui permet d'avoir le plus court chemin allant du sommet de référence à un sommet donné.
     *
     * @param endVertex le sommet dont on veut connaître le plus court chemin à partir du sommet de référence
     * @return la liste des villes par lesquelles il faut passer
     */
    protected ArrayList<String> getPath(Vertex endVertex) {
        ArrayList<String> path = new ArrayList<>();
        Vertex v = endVertex;
        path.add(endVertex.getName());
        while (pathArray[v.getId()] != null) {
            v = pathArray[v.getId()];
            path.add(v.getName());
        }
        Collections.reverse(path);
        return path;
    }
}
