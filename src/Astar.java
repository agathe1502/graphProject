import java.util.ArrayList;
import java.util.HashMap;

public class Astar extends ShortestPathAlgorithm {

    private final Graph graph;
    private final Vertex startVertex;
    private final Vertex endVertex;
    private final ArrayList<Vertex> vertices;
    private final HashMap<Integer, double[]> coordinates;
    private final double[] f; // f = g+h
    private final double[] g;
    private final double[] h;
    private final ArrayList<Vertex> openList;
    private final boolean[] isUsedTab;
    private final Vertex[] pathArray;
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

    public double getDistance(Vertex endVertex) {
        return f[endVertex.getId()];
    }

    private void initialization() {
        openList.add(startVertex);
        isUsedTab[startVertex.getId()] = true;

        for (Vertex v : vertices) {
            g[v.getId()] = getValueDistance(startVertex, v);
            h[v.getId()] = distance(v, endVertex);
            //h[v.getId()] = graph.getValueByVertices(v, endVertex);
            f[v.getId()] = g[v.getId()] + h[v.getId()];
        }
    }

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

        double value = getValueDistance(x, vertexSuccessor);
        if (value != -1) {
            g[vertexSuccessorId] = g[min] + value;
            f[vertexSuccessorId] = g[vertexSuccessorId] + h[vertexSuccessorId];
        }
    }
}
