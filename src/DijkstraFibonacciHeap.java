import org.jgrapht.util.FibonacciHeap;
import org.jgrapht.util.FibonacciHeapNode;

import java.util.ArrayList;
import java.util.Collections;

public class DijkstraFibonacciHeap {

    private Graph G;
    private Vertex startVertex;
    private double[] lembda;
    private ArrayList<Vertex> vertices;
    private ArrayList<Vertex> Z;
    private boolean[] isUsedTab;
    private FibonacciHeap<Vertex> fibonacciHeap;
    private FibonacciHeapNode[] fiboHeadNodeTab;
    private Vertex[] pathArray;

    public DijkstraFibonacciHeap(Graph G, Vertex startVertex){
        this.G = G;
        this.startVertex = startVertex;
        this.lembda = new double[G.getVertices().size()];
        this.vertices = G.getVertices();
        this.Z = (ArrayList<Vertex>) vertices.clone();
        this.isUsedTab = new boolean[G.getVertices().size()];
        this.fibonacciHeap = new FibonacciHeap<>();
        this.fiboHeadNodeTab = new FibonacciHeapNode[G.getVertices().size()];
        this.pathArray = new Vertex[vertices.size()];
    }

    public double[] runAlgorithm(){
        initialization();
        analyzeUnmarkedVertices();
        return lembda;
    }

    public ArrayList<String> getPath(Vertex endVertex) {
        ArrayList<String> path = new ArrayList<>();
        Vertex v = endVertex;
        path.add(endVertex.getName());
        while(pathArray[v.getId()] != null){
            v = pathArray[v.getId()];
            path.add(v.getName());
        }
        Collections.reverse(path);
        return path;
    }


    public double getDistance(Vertex endVertex){
        return lembda[endVertex.getId()];
    }

    private void initialization(){
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

    private void analyzeUnmarkedVertices(){
        while (Z.size() > 0) { // analyse unmarked vertices
            int min = fibonacciHeap.min().getData().getId();
            fibonacciHeap.removeMin();
            isUsedTab[min] = true;
            Vertex x = vertices.get(min);
            Z.remove(x);
            analyzeAdjacentsEdges(min);
        }
    }

    private void analyzeAdjacentsEdges(int min){
        int loop = 0;
        for (Integer edgeId : G.getAdjacentList().get(min)) { // analyze adjacent edges
            // the first one is the Vertex Id
            if (loop != 0) {
                // we have edges adjacent to the vertex x
                // we get from each edge the vertex which is not x
                Edge edge = G.getEdges().get(edgeId);
                modifyDistance(edge, min);
            }
            loop++;
        }
    }

    public void modifyDistance(Edge edge, int min){
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
