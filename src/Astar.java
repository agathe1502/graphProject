import java.util.ArrayList;
import java.util.HashMap;

public class Astar {

    private Graph graph;
    private Vertex startVertex;
    private Vertex endVertex;
    private ArrayList<Vertex> vertices;
    private HashMap<Integer, double[]> coordinates;
    private double[] f; // f = g+h
    private double[] g;
    private double[] h;
    private ArrayList<Vertex> openList;
    private boolean[] isUsedTab;
    private Vertex[] pathArray;
    private boolean success;


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
        this.pathArray = new Vertex[vertices.size()];
        this.success = false;
    }

    public void runAlgorithm() {
        initialization();
        analyzeUnmarkedVertices();
    }

    private void initialization() {
        openList.add(startVertex);
        isUsedTab[startVertex.getId()] = true;

        for (Vertex v : vertices) {
           // if (!isUsedTab[v.getId()]) {
                g[v.getId()] = getValueDistance(startVertex, v);
                h[v.getId()] = distance(v, endVertex);
                f[v.getId()] = g[v.getId()] + h[v.getId()];
            //}
        }
        displayTab(f);
    }

    private void  analyzeUnmarkedVertices() {
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

    private double distance(Vertex vertex1, Vertex vertex2) {
        double long1 = Math.toRadians(coordinates.get(vertex1.getId())[0]);
        double lat1 = Math.toRadians(coordinates.get(vertex1.getId())[1]);
        double long2 = Math.toRadians(coordinates.get(vertex2.getId())[0]);
        double lat2 = Math.toRadians(coordinates.get(vertex2.getId())[1]);

        double distance = Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(long2 - long1);
        return Math.acos(distance) * 6378.137;
    }

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

    private boolean isSuccess(Vertex v) {
        return v.equals(endVertex);
    }

    private void analyzeAdjacentsEdges(int min) {
        int loop = 0;
        for (Integer edgeId : graph.getAdjacentList().get(min)) {
            if (loop != 0) {
                Edge edge = graph.getEdges().get(edgeId);
                int otherVertexId = edge.getOtherVertex(min);
                if (!isUsedTab[otherVertexId] && !openList.contains(vertices.get(otherVertexId))) {
                    openList.add(vertices.get(otherVertexId));
                    pathArray[otherVertexId] = vertices.get(min);
                    //Edge edge = graph.getEdges().get(edgeId);
                    modifyDistance(min, edge);
                }
                else {
                    //Edge edge = graph.getEdges().get(edgeId);
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

    private double getValueDistance(Vertex vertex1, Vertex vertex2) {
        double value = graph.getValueByVertices(vertex1, vertex2);
        if (value == -1) {
            value = graph.getValueByVertices(vertex2, vertex1);
        }
        return value;
    }

    private void modifyDistance(int min, Edge edge) {
        Vertex x = vertices.get(min);
        int vertexSuccessorId = edge.getOtherVertex(min);
        Vertex vertexSuccessor = vertices.get(vertexSuccessorId);

        double value = getValueDistance(x,vertexSuccessor);
        if (value != -1) {
            g[vertexSuccessorId] = g[min] + value;
            f[vertexSuccessorId] = g[vertexSuccessorId] + h[vertexSuccessorId];
            displayTab(f);
        }
    }

    private void displayTab (double[] tab) {
        for (int i = 0; i<tab.length; i++){
            System.out.print(tab[i] + " ; ");
        }
    }
}
