import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {

    private final String name;
    private final ArrayList<Vertex> vertices = new ArrayList<>();
    private final ArrayList<Edge> edges = new ArrayList<>();
    private final HashMap<List<Vertex>, Double> edges2 = new HashMap<>();
    private final ArrayList<LinkedList<Integer>> adjacentList = new ArrayList<>();

    public Graph(String file) {
        this.name = file;
    }


    public void addVertex(Vertex v) {
        vertices.add(v);
    }

    public void addEdge(Edge e) {
        edges.add(e);
        // voir pour créer autre méthode sans passer par classe Edge
        edges2.put(Arrays.asList(vertices.get(e.getInitialVertex()), vertices.get(e.getFinalVertex())), e.getValue());
    }

    public void linkEdgeToVertex(int vertexId, int edgeId) {
        adjacentList.get(vertexId).add(edgeId);
    }

    public void addLinkedList(LinkedList<Integer> list) {
        this.adjacentList.add(list);
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<LinkedList<Integer>> getAdjacentList() {
        return adjacentList;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public HashMap<List<Vertex>, Double> getEdges2() {
        return edges2;
    }

    public double getValueByVertices(Vertex initialVertex, Vertex finalVertex) {
        if (edges2.containsKey(Arrays.asList(initialVertex, finalVertex))) {
            return edges2.get(Arrays.asList(initialVertex, finalVertex));
        }
        return -1;
    }

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

    public void createAdjacentList() throws IOException {
        System.out.println("Adjacent list is creating...");
        String line = "";
        boolean readVertices = false;


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
    }

    public void createAdjacentListVRP1(HashMap<Integer, Integer> populations, int minPop) throws IOException {
        System.out.println("Adjacent list is creating...");
        String line = "";
        boolean readVertices = false;


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
    }
}
