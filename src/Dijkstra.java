/**
 * Classe qui permet de lancer l'algorithme de Dijkstra sur un graphe (à partir d'un sommet de référence)
 */
public class Dijkstra extends DijkstraAlgorithm {

    /**
     * Constructeur de l'algorithme
     *
     * @param G           le graphe auquel il va être appliqué
     * @param startVertex le sommet de référence
     */
    public Dijkstra(Graph G, Vertex startVertex) {
        super(G, startVertex);
    }


    /**
     * Phase d'initialisation de l'algorithme.
     * - initialiser 0 comme distance entre le point de référence et lui même.
     * - initialise +INFINI pour les autres sommets
     * - Indique le sommet de départ comme sommet parent pour chacun des autres sommets du graphe
     */
    @Override
    protected void initialization() {
        lembda[startVertex.getId()] = 0;
        Z.remove(startVertex);
        isUsedTab[startVertex.getId()] = true;

        for (Vertex i : Z) {
            if (!isUsedTab[i.getId()]) {
                double value = G.getValueByVertices(startVertex, i);
                // graph non orienté
                if (value == -1) {
                    value = G.getValueByVertices(i, startVertex);
                }
                lembda[i.getId()] = value != -1 ? value : Double.POSITIVE_INFINITY;
                pathArray[i.getId()] = startVertex;
            }
        }

    }

    /**
     * Phase qui boucle sur la liste des sommets dont la distance la plus court n'est pas encore determinée.
     * Pour chaque itération elle va :
     * - rechercher le sommet qui a la distance minimale, parmi ceux non traités
     * - analyser ses arcs adjacents (pour mettre à jour la valeur des sommets finaux)
     */
    @Override
    protected void analyzeUnmarkedVertices() {
        while (Z.size() > 0) { // analyse unmarked vertices
            int min = getIndexOfMin();
            isUsedTab[min] = true;
            Vertex x = vertices.get(min);
            Z.remove(x);
            analyzeAdjacentsEdges(min);
        }
    }


    /**
     * Méthode qui permet de mettre à jour la distance des sommets de fin d'un arc donné
     *
     * @param edge l'arc dont il va falloir mettre à jour la distance de son sommet de fin
     * @param min  l'id du sommet de départ de l'arc (pour pouvoir déterminer quel est le sommet de fin de l'arc)
     */
    @Override
    public void modifyDistance(Edge edge, int min) {
        Vertex x = vertices.get(min);
        int otherVertexId = edge.getOtherVertex(min);
        Vertex otherVertex = vertices.get(otherVertexId);
        if (!isUsedTab[otherVertexId]) { // modify distance
            double value = G.getValueByVertices(x, otherVertex);
            if (value == -1) {
                value = G.getValueByVertices(otherVertex, x);
            }
            if (value != -1) {
                if (lembda[min] + value < lembda[otherVertexId]) {
                    lembda[otherVertexId] = lembda[min] + value;
                    pathArray[otherVertexId] = x;
                }
            }

        }
    }

    /**
     * Méthode qui permet de déterminer l'id du sommet non traité qui a la plus petite distance avec le sommet de référence
     *
     * @return l'id du sommet non traité qui a la plus petite distance avec le sommet de référence.
     */
    protected int getIndexOfMin() {
        double min = Double.POSITIVE_INFINITY;
        int index = -1;
        int firstInfinity = -1;
        for (int i = 0; i < lembda.length; i++) {
            if (!isUsedTab[i]) {
                if (lembda[i] < min) {
                    min = lembda[i];
                    index = i;
                } else if (firstInfinity == -1 && lembda[i] == Double.POSITIVE_INFINITY) {
                    firstInfinity = i;
                }
            }
        }
        if (index == -1) {
            index = firstInfinity;
        }

        return index;
    }

}
