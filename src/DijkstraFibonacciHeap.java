import org.jgrapht.util.FibonacciHeap;
import org.jgrapht.util.FibonacciHeapNode;

public class DijkstraFibonacciHeap extends DijkstraAlgorithm {


    /**
     * Le tas de Fibonacci composé de la manière suivante :
     * - en clé : la valeur du sommet
     * - en valeur : le sommet du graphe
     */
    private static FibonacciHeap<Vertex> fibonacciHeap;
    /**
     * La liste des noeuds utilisés dans le tas de Fibonacci
     */
    private static FibonacciHeapNode[] fiboHeadNodeTab;

    /**
     * Constructeur de l'algorithme
     *
     * @param G           le graphe auquel il va être appliqué
     * @param startVertex le sommet de référence
     */
    public DijkstraFibonacciHeap(Graph G, Vertex startVertex) {
        super(G, startVertex);
        fibonacciHeap = new FibonacciHeap<>();
        fiboHeadNodeTab = new FibonacciHeapNode[G.getVertices().size()];
    }

    /**
     * Phase d'initialisation de l'algorithme.
     * - initialiser 0 comme distance entre le point de référence et lui même.
     * - initialise +INFINI pour les autres sommets
     * - Indique le sommet de départ comme sommet parent pour chacun des autres sommets du graphe
     * - Initialise +INFINI dans le tas de fibonacci à chacun des sommets
     * - Initialise la liste des noeuds du tas de fibonacci
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
                FibonacciHeapNode<Vertex> fhn = new FibonacciHeapNode<>(i);
                fiboHeadNodeTab[i.getId()] = fhn;
                fibonacciHeap.insert(fhn, lembda[i.getId()]);
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
            int min = fibonacciHeap.min().getData().getId();
            fibonacciHeap.removeMin();
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
    protected void modifyDistance(Edge edge, int min) {
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
                    fibonacciHeap.decreaseKey(fiboHeadNodeTab[otherVertexId], lembda[min] + value);
                }
            }
        }
    }
}
