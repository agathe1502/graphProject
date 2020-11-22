import java.util.ArrayList;

public class VRP2 {

    private final Graph graph;
    private final boolean[] isUsed;
    private final Vertex startVertex;
    private final ArrayList<Vertex> cycle;
    private double distance;

    public VRP2(Graph g, Vertex startVertex) {
        this.graph = g;
        this.isUsed = new boolean[graph.getVertices().size()];
        this.startVertex = startVertex;
        this.cycle = new ArrayList<>();
        this.distance = 0;
    }

    public void runAlgorithm() {
        cycle.add(startVertex);
        int idVertex = startVertex.getId();
        while(cycle.size() < graph.getVertices().size()) {
            idVertex = getIndexOfMin(idVertex);
            if (idVertex != -1){
                isUsed[idVertex] = true;
                cycle.add(graph.getVertices().get(idVertex));
            }
        }
        distance += graph.getValueByVertices(graph.getVertices().get(idVertex), startVertex);
        cycle.add(startVertex);
        System.out.println("Distance : " + distance + " km");
        displayCycle();
    }

    private int getIndexOfMin(int idVertex){
        int loop = 0;
        double min = Double.POSITIVE_INFINITY;
        int index = -1;
        double tmpDistance = 0;
        for (Integer edgeId : graph.getAdjacentList().get(idVertex)) {
            if (loop != 0) {
                Edge edge = graph.getEdges().get(edgeId);
                double value = edge.getValue();
                int idSuccessorVertex = edge.getOtherVertex(idVertex);
                if (!isUsed[idSuccessorVertex] && idSuccessorVertex != startVertex.getId()) {
                    if (value < min) {
                        min = value;
                        index = idSuccessorVertex;
                        tmpDistance = value;
                    }
                }
            }
            loop++;
        }
        if (index != -1) {
            distance += tmpDistance;
        }
        return index;
    }

    private void displayCycle() {
        System.out.print("[ ");
        for (Vertex vertex : cycle) {
            System.out.print(vertex.getName() + " ");
        }
        System.out.print("]");
    }

}
