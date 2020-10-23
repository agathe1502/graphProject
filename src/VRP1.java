import org.jgrapht.util.FibonacciHeap;
import org.jgrapht.util.FibonacciHeapNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class VRP1 {

    private ArrayList<LinkedList<Integer>> adjacentList;
    private Graph graph;
    private FibonacciHeap<Integer> averages;

    public VRP1(){
        graph = new Graph("src/files/File_VRP1.txt");
        adjacentList = graph.getAdjacentList();
    }

    public void runAlgorithm() throws IOException {
        // Read the file to create the adjacent list
        CSVtoTXT fileCSV = new CSVtoTXT();
        fileCSV.fileConversion(0, 0, false);
        graph.createAdjacentListVRP1(fileCSV.getPopulations(), 200000);

        // Given
        ArrayList<Edge> edges = graph.getEdges();
        ArrayList<Vertex> vertices = graph.getVertices();
        averages = new FibonacciHeap<>();

        for(LinkedList<Integer> startCityList : adjacentList) {
            double sum = 0;
            int loop = 0;
            for (Integer edgeId : startCityList) {
                if(loop!= 0){
                    Edge e = edges.get(edgeId);
                    double value = graph.getValueByVertices(vertices.get(e.getInitialVertex()), vertices.get(e.getFinalVertex()));
                    if (value == -1) {
                        value = graph.getValueByVertices(vertices.get(e.getFinalVertex()), vertices.get(e.getInitialVertex()));
                    }
                    sum += value;
                }
                loop ++;
            }
            averages.insert(new FibonacciHeapNode<>(startCityList.get(0)), sum / Double.parseDouble(String.valueOf(startCityList.size()-1)));
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public void displayResults() {
        int cityId = averages.min().getData();
        ArrayList<Edge> edges = graph.getEdges();
        ArrayList<Vertex> vertices = graph.getVertices();

        System.out.println("The VRP should live in " + graph.getVertices().get(cityId).getName());
        int loop = 0;
        for (Integer edgeId: adjacentList.get(cityId)){
            if(loop!= 0){
                Edge e = edges.get(edgeId);
                int otherVertex = e.getOtherVertex(cityId);

                double value = graph.getValueByVertices(vertices.get(e.getInitialVertex()), vertices.get(e.getFinalVertex()));
                if (value == -1) {
                    value = graph.getValueByVertices(vertices.get(e.getFinalVertex()), vertices.get(e.getInitialVertex()));
                }
                System.out.println("- " +vertices.get(otherVertex).getName() + " : " + value + " km");

            }
            loop++;
        }
        System.out.println("--> Average : " + String.format("%.2f", averages.min().getKey()) + " km");

    }
}
