public class Edge {

    private final int id;
    private final int initialVertex;
    private final int finalVertex;
    private final double value;

    public Edge(int id, int initialVertex, int finalVertex, double value) {
        this.id = id;
        this.initialVertex = initialVertex;
        this.finalVertex = finalVertex;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getInitialVertex() {
        return initialVertex;
    }

    public int getFinalVertex() {
        return finalVertex;
    }

    public double getValue() {
        return value;
    }

    public int getOtherVertex(int vertex) {
        return vertex == initialVertex ? finalVertex : initialVertex;
    }
}
