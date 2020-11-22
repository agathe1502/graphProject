import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe qui permet de lancer l'algorithme A* sur un graphe (avec un sommet initial et un sommet final)
 */
public class Astar extends ShortestPathAlgorithm {

    /**
     * Le graphe sur lequel va s'appliquer l'algorithme
     */
    private final Graph graph;
    /**
     * Le sommet initial
     */
    private final Vertex startVertex;
    /**
     * Le sommet final
     */
    private final Vertex endVertex;
    /**
     * Les sommets du graphe
     */
    private final ArrayList<Vertex> vertices;
    /**
     * Contient les coordonnées (longitude, latitude) des sommets
     */
    private final HashMap<Integer, double[]> coordinates;
    /**
     * Tableau contenant le coût total : coût depuis la source (G) + coût vers la destination (H)
     * Aussi appelé la fonction d'évaluation d'un sommet
     */
    private final double[] f; // f = g+h
    /**
     * Tableau contenant les distances depuis le sommet initial au sommet courant (index du tableau)
     * Aussi appelé le coût depuis la source
     */
    private final double[] g;
    /**
     * Tableau contenant les distances à vol d'oiseau entre le sommet courant (index du tableau) et le sommet final
     * Aussi appelé le coût vers la destination (heuristique)
     */
    private final double[] h;
    /**
     * Liste des sommets à analyser (liste ouverte)
     */
    private final ArrayList<Vertex> openList;
    /**
     * Tableau pour savoir si le sommet est traité ou non
     */
    private final boolean[] isUsedTab;
    /**
     * Boolean permettant de savoir si le sommet final est atteint
     */
    private boolean success;

    /**
     * Constructeur de l'algorithme
     *
     * @param graph graphe sur lequel il va s'appliquer
     * @param startVertex sommet initial
     * @param endVertex sommet final
     * @param coordinates tableau des coordonées de chaque sommet
     */
    public Astar(Graph graph, Vertex startVertex, Vertex endVertex, HashMap<Integer, double[]> coordinates) {
        this.graph = graph;
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.vertices = graph.getVertices();
        this.coordinates = coordinates;
        this.f = new double[graph.getVertices().size()];
        this.g = new double[graph.getVertices().size()];
        this.h = new double[graph.getVertices().size()];
        this.isUsedTab = new boolean[graph.getVertices().size()];
        this.openList = new ArrayList<>();
        pathArray = new Vertex[vertices.size()];
        this.success = false;
    }

    /**
     * Méthode qui permet de lancer l'algorithme A*
     * - Phase d'initialisation
     * - Phase qui analyse chaque sommet présent dans la liste ouverte
     */
    public void runAlgorithm() {
        initialization();
        analyzeUnmarkedVertices();
    }

    /**
     * Méthode qui permet de récupérer la plus courte distance entre le sommet initial et le sommet final
     * @param endVertex sommet final
     * @return la distance du plus court chemin entre le sommet initial et le sommet final
     */
    public double getDistance(Vertex endVertex) {
        return f[endVertex.getId()];
    }

    /**
     * Méthode qui permet l'initialisation de l'algorithme
     * - le sommet de départ est ajoputé à la liste ouverte et marqué comme traiter
     * - pour chaque sommet, on remplit les tableaux G, H et F
     *
     * G : on récupère la valeur de l'arc entre le sommet initial et le sommet v
     * H : on calcule la distance à vol d'oiseau entre le sommet v et le sommet final (constant)
     * F : Somme de G et H
     */
    private void initialization() {
        openList.add(startVertex);
        isUsedTab[startVertex.getId()] = true;

        for (Vertex v : vertices) {
            g[v.getId()] = getValueDistance(startVertex, v);
            h[v.getId()] = distance(v, endVertex);
            f[v.getId()] = g[v.getId()] + h[v.getId()];
        }
    }

    /**
     * Méthode qui boucle sur la liste ouverte qui contient les sommets non traités et si le sommet final n'est pas atteint
     * Pour chaque itération, elle va :
     * - récupérer le sommet ayant la distance (F) minimale parmi les sommets de la liste ouverte
     * - analyser ses arcs adjacents
     */
    private void analyzeUnmarkedVertices() {
        while (openList.size() > 0 && !success) {
            int min = getIndexOfMin(f, openList);
            Vertex x = vertices.get(min);
            if (isSuccess(x)) {
                success = true;
            } else {
                openList.remove(x);
                isUsedTab[x.getId()] = true;
                analyzeAdjacentsEdges(min);
            }
        }
    }

    /**
     * Méthode qui calcule la distance à vol d'oiseau entre 2 sommets à l'aide des coordonnées de deux villes
     * @param vertex1 premier sommet
     * @param vertex2 deuxième sommet
     * @return la distance entre ces deux sommets
     */
    private double distance(Vertex vertex1, Vertex vertex2) {
        double long1 = Math.toRadians(coordinates.get(vertex1.getId())[0]);
        double lat1 = Math.toRadians(coordinates.get(vertex1.getId())[1]);
        double long2 = Math.toRadians(coordinates.get(vertex2.getId())[0]);
        double lat2 = Math.toRadians(coordinates.get(vertex2.getId())[1]);

        double distance = Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(long2 - long1);
        return Math.acos(distance) * 6378.137;
    }

    /**
     * Méthode qui récupère l'id du sommet non traité ayant la distance la plus courte suivant la valeur de F
     * @param f tableau des distances estimées
     * @param openList liste des sommets à traiter
     * @return l'id du sommet ayant la distance la plus courte
     */
    private int getIndexOfMin(double[] f, ArrayList<Vertex> openList) {
        double min = Double.POSITIVE_INFINITY;
        int index = -1;
        for (Vertex v : openList) {
            if (f[v.getId()] < min) {
                min = f[v.getId()];
                index = v.getId();
            }
        }
        return index;
    }

    /**
     * Méthode qui détermine si un sommet est égal au sommet final
     * @param v sommet à tester
     * @return true si le sommet est égal, sinon false
     */
    private boolean isSuccess(Vertex v) {
        return v.equals(endVertex);
    }

    /**
     * Méthode qui permet d'analyser les arcs adjacents à un sommet donné.
     * Elle permet aussi de mettre à jour la distance G grâce aux valeurs des arcs suiavnt le chemin partant du sommet initial au sommet courant
     * @param min l'id du sommet non traité qui a la valeur de la fonction d'évaluation la plus faible
     */
    private void analyzeAdjacentsEdges(int min) {
        int loop = 0;
        for (Integer edgeId : graph.getAdjacentList().get(min)) {
            if (loop != 0) {
                Edge edge = graph.getEdges().get(edgeId);
                int otherVertexId = edge.getOtherVertex(min);
                if (!isUsedTab[otherVertexId] && !openList.contains(vertices.get(otherVertexId))) {
                    openList.add(vertices.get(otherVertexId));
                    pathArray[otherVertexId] = vertices.get(min);
                    modifyDistance(min, edge);
                } else {
                    if (g[otherVertexId] > g[min] + getValueDistance(vertices.get(min), vertices.get(otherVertexId))) {
                        pathArray[otherVertexId] = vertices.get(min);
                        modifyDistance(min, edge);
                        if (isUsedTab[otherVertexId]) {
                            isUsedTab[otherVertexId] = !isUsedTab[otherVertexId];
                            openList.add(vertices.get(otherVertexId));
                        }
                    }
                }
            }
            loop++;
        }
    }

    /**
     * Méthode qui permet de récupérer la valeur d'un arc
     * @param vertex1 premier sommet
     * @param vertex2 deuxième sommet
     * @return la valeur de la distance entre les deux sommets
     */
    private double getValueDistance(Vertex vertex1, Vertex vertex2) {
        double value = graph.getValueByVertices(vertex1, vertex2);
        if (value == -1) {
            value = graph.getValueByVertices(vertex2, vertex1);
        }
        return value;
    }

    /**
     * Méthode qui permet de mettre à jour la distance la distance du sommet initial au sommet courant (valeur du tableau G)
     * Au sommet courant, on prend la valeur G du sommet de départ de l'arc (c'est-à dire le chemin allant de sommet initial jusqu'à celui-ci)
     * et on ajoute la valeur de l'arc
     * @param min l'id du sommet de départ de l'arc (pour pouvoir déterminer quel est le sommet de fin de l'arc, ici considéré comme le sommet courant)
     * @param edge l'arc dont il va falloir mettre à jour la distance G
     */
    private void modifyDistance(int min, Edge edge) {
        Vertex x = vertices.get(min);
        int vertexSuccessorId = edge.getOtherVertex(min);
        Vertex vertexSuccessor = vertices.get(vertexSuccessorId);

        double value = getValueDistance(x, vertexSuccessor);
        if (value != -1) {
            g[vertexSuccessorId] = g[min] + value;
            f[vertexSuccessorId] = g[vertexSuccessorId] + h[vertexSuccessorId];
        }
    }
}
