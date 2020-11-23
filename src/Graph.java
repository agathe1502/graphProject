import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Classe qui permet de créer un graphe à partir d'un nom de fichier.
 * Un graphe est composé de sommets et d'arcs.
 * Des méthodes sont disponibles pour créer une structure de graphe ( liste d'adjacence ) et la récupérer
 */
public class Graph {

    /**
     * Le nom du fichier du graphe
     */
    private final String name;
    /**
     * La liste des sommets du graphe
     */
    private final ArrayList<Vertex> vertices = new ArrayList<>();
    /**
     * La liste des arcs du graphe
     */
    private final ArrayList<Edge> edges = new ArrayList<>();
    /**
     * La hashmap des arcs du graphes
     * Clé : liste des 2 sommets de l'arc
     * Valeur : la valeur de l'arc
     */
    private final HashMap<List<Vertex>, Double> edges2 = new HashMap<>();
    /**
     * La structure de graphe : une liste d'adjacence.
     * C'est une liste de liste chainées.
     * Chaque liste chaînée correspond à la liste des arcs (par leur id)  de chaque sommets (par leur id)
     */
    private final ArrayList<LinkedList<Integer>> adjacentList = new ArrayList<>();

    /**
     * Constructeur qui permet de créer un graphe à partir d'un nom de fichier
     *
     * @param file le nom du fichier du graphe
     */
    public Graph(String file) {
        this.name = file;
    }


    /**
     * Méthode qui permet d'ajouter un sommet à la liste des sommets du graphe
     *
     * @param v le sommet à ajouter
     */
    public void addVertex(Vertex v) {
        vertices.add(v);
    }

    /**
     * Méthode qui permet d'ajouter un sommet à :
     * - la liste des sommets (pour pouvoir le récupérér par la suite par son id)
     * - la hashmap des arcs (pour pouvoir récupérer sa valeur à partir de ses deux sommets)
     *
     * @param e le sommet à ajouter
     */
    public void addEdge(Edge e) {
        edges.add(e);
        // voir pour créer autre méthode sans passer par classe Edge
        edges2.put(Arrays.asList(vertices.get(e.getInitialVertex()), vertices.get(e.getFinalVertex())), e.getValue());
    }

    /**
     * Méthode qui permet d'ajouter l'id d'un arc à la liste chainée d'un sommet.
     *
     * @param vertexId l'id du sommet
     * @param edgeId   l'id de l'arc
     */
    public void linkEdgeToVertex(int vertexId, int edgeId) {
        adjacentList.get(vertexId).add(edgeId);
    }

    /**
     * Permet d'ajouter une nouvelle liste chainée à la liste d'adjacence.
     * Méthode utilisée dans le cas où on ajoute un nouveau sommet à la liste d'adjacence
     *
     * @param list la liste chainée du sommet à ajouter à la liste d'adjacence
     */
    public void addLinkedList(LinkedList<Integer> list) {
        this.adjacentList.add(list);
    }

    /**
     * Retourne le nom du graphe
     *
     * @return le nom du graphe
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retourne la liste d'adjacence du graphe.
     * C'est une liste de liste chainée
     *
     * @return la liste d'adjacence du graphe
     */
    public ArrayList<LinkedList<Integer>> getAdjacentList() {
        return adjacentList;
    }

    /**
     * Retrourne la liste des sommets.
     * Le premier est celui d'id 1, le 2ème d'id 2 etc.
     *
     * @return la liste des sommets du graphe
     */
    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Retrourne la liste des arcs.
     * Le premier est celui d'id 1, le 2ème d'id 2 etc.
     *
     * @return la liste des arcs du graphe
     */
    public ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * Retourne une Hashmap avec :
     * - en clé : une liste de 2 sommets
     * - en valeur : la valeur de l'arc qui a pour sommets, ceux de la clé
     *
     * @return la Hashmap de la valeur de l'arc en fonction de ses 2 sommets
     */
    public HashMap<List<Vertex>, Double> getEdges2() {
        return edges2;
    }

    private CSVtoTXT fileCSV;


    /**
     * Permet d'obtenir la valeur d'un arc à partir de ses deux sommets
     *
     * @param initialVertex le noeud de départ
     * @param finalVertex   le noeud de fin
     * @return la valeur de l'arc (-1 si l'arc n'existe pas)
     */
    public double getValueByVertices(Vertex initialVertex, Vertex finalVertex) {
        if (edges2.containsKey(Arrays.asList(initialVertex, finalVertex))) {
            return edges2.get(Arrays.asList(initialVertex, finalVertex));
        }
        return -1;
    }

    /**
     * Permet d'afficher la liste d'adjacence du graphe
     *
     * @return la liste d'adjacence sous forme de chaine de caractères
     */
    public String displayAdjacentList() {
        // Display adjacentList
        StringBuilder s = new StringBuilder();
        for (LinkedList<Integer> list : this.getAdjacentList()) {
            for (int i : list) {
                s.append(i).append("->");
            }
            s.append("\n");
        }
        return String.valueOf(s);
    }

    /**
     * Permet de créer la liste d'agjacence du graphe.
     * 1- Lecture du fichier txt
     * 2- Création d'une liste chainée pour chaque sommet (avec son id en premier élément)
     * 3- Ajout de l'id de chaque arc à la suite la liste chainée des 2 sommets qui le compose
     */
    public void createAdjacentList() {
        System.out.println("Adjacent list is creating...");
        String line = "";
        boolean readVertices = false;


        try {
            BufferedReader br = new BufferedReader(new FileReader(this.getName()));
            br.readLine();
            br.readLine();
            //br.readLine(); // A rajouter si la ligne du nb de noeuds est présente

            while ((line = br.readLine()) != null) {
                if (line.contains("---")) {
                    // if start of file => now read vertices
                    // elseif read Vertices => now read edges
                    readVertices = !readVertices;
                } else {
                    if (readVertices) {
                        // read vertices
                        String[] arrayVertex = line.split(" ");
                        int idVertex = Integer.parseInt(arrayVertex[0]);
                        this.addVertex(new Vertex(idVertex, arrayVertex[1]));

                        // Create linkedList and add it on arrayList
                        LinkedList<Integer> linkedList = new LinkedList<>();
                        linkedList.add(idVertex);
                        this.addLinkedList(linkedList);
                    } else {
                        // Read edges
                        String[] arrayEdge = line.split(" ");
                        int edgeId = Integer.parseInt(arrayEdge[0]);
                        int initialVerticeId = Integer.parseInt(arrayEdge[1]);
                        int finalVerticeId = Integer.parseInt(arrayEdge[2]);
                        this.addEdge((new Edge(Integer.parseInt(arrayEdge[0]), initialVerticeId,
                                finalVerticeId, Double.parseDouble(arrayEdge[3].replaceAll(",", ".")))));

                        // Link edge on linkedList of the 2 vertices of the edge
                        this.linkEdgeToVertex(initialVerticeId, edgeId);
                        this.linkEdgeToVertex(finalVerticeId, edgeId);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Même principe que pour createAdjacentList, mais en prenant en compte la population des villes sommets
     * On ajoute tous les sommets dans la liste d'adjacence.
     * On lie a ces sommets seulement les ids des arcs qui ont un sommet de plus de "minPop" habitants
     *
     * @param populations une HashMap avec en clé l'id de la ville, et en valeur son nombre d'habitants
     * @param minPop      le nombre d'habitants minimum pour pouvoir ajouter l'arc à la liste d'adjacence.
     */
    public void createAdjacentListVRP1(HashMap<Integer, Integer> populations, int minPop) {
        System.out.println("Adjacent list is creating...");
        String line = "";
        boolean readVertices = false;


        try {
            BufferedReader br = new BufferedReader(new FileReader(this.getName()));
            br.readLine();
            br.readLine();
            //br.readLine(); // A rajouter si la ligne du nb de noeuds est présente

            while ((line = br.readLine()) != null) {
                if (line.contains("---")) {
                    // if start of file => now read vertices
                    // elseif read Vertices => now read edges
                    readVertices = !readVertices;
                } else {
                    if (readVertices) {
                        // read vertices
                        String[] arrayVertex = line.split(" ");
                        int idVertex = Integer.parseInt(arrayVertex[0]);
                        this.addVertex(new Vertex(idVertex, arrayVertex[1]));

                        // Create linkedList and add it on arrayList
                        LinkedList<Integer> linkedList = new LinkedList<>();
                        linkedList.add(idVertex);
                        this.addLinkedList(linkedList);
                    } else {
                        // Read edges
                        String[] arrayEdge = line.split(" ");
                        int edgeId = Integer.parseInt(arrayEdge[0]);
                        int initialVerticeId = Integer.parseInt(arrayEdge[1]);
                        int finalVerticeId = Integer.parseInt(arrayEdge[2]);
                        boolean addedEdge = false;
                        if (populations.get(initialVerticeId) > minPop) {
                            this.linkEdgeToVertex(finalVerticeId, edgeId);
                            //this.linkEdgeToVertex(finalVerticeId, initialVerticeId);
                            addedEdge = true;
                        }
                        if (populations.get(finalVerticeId) > minPop) {
                            this.linkEdgeToVertex(initialVerticeId, edgeId);
                            //this.linkEdgeToVertex(initialVerticeId, finalVerticeId);
                            addedEdge = true;
                        }
                        if (addedEdge) {
                            this.addEdge((new Edge(Integer.parseInt(arrayEdge[0]), initialVerticeId,
                                    finalVerticeId, Double.parseDouble(arrayEdge[3].replaceAll(",", ".")))));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void setFileCSV(CSVtoTXT fileCSV){
        this.fileCSV = fileCSV;
    }

    public CSVtoTXT getFileCSV() {
        return fileCSV;
    }
}
