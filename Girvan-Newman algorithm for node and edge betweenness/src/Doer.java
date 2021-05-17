import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Doer {
    Graph graph;

    public Doer() {
        graph = new Graph();
    }

    public void loadData(String filename) throws FileNotFoundException {
        File f = new File(filename);
        try {
            // Create and add nodes
            Scanner scn = new Scanner(f);
            scn.nextLine();
            while (scn.hasNextLine()) {
                String line = scn.nextLine();
                String[] lineData = line.split(" ");
                if (!graph.hasNode(lineData[1])) graph.addNode(lineData[1]);
                if (!graph.hasNode(lineData[2])) graph.addNode(lineData[2]);
            }
            // Create and add edges
            Scanner sce = new Scanner(f);
            sce.nextLine();
            while (sce.hasNextLine()) {
                String line = sce.nextLine();
                String[] lineData = line.split(" ");
                graph.addEdge(lineData[1], lineData[2]);
            }
        }
        catch (Exception e) {
            System.out.println("File " + f + " not found");
        }
    }

    private void checkNodesAndEdges() {
        System.out.println("nodes: " + graph.getNumNodes() + ", edges: " + graph.getNumEdges());
    }

    private void tests1() {
        System.out.println(graph.hasEdge("A", "B"));
        System.out.println(graph.hasEdge("A", "C"));
        System.out.println(graph.hasEdge("A", "D"));
        System.out.println(graph.hasEdge("A", "E"));
        System.out.println(graph.hasEdge("B", "D"));
        System.out.println(graph.hasEdge("C", "E"));
        System.out.println(graph.hasEdge("D", "F"));
        System.out.println(graph.hasEdge("F", "G"));
        System.out.println(graph.hasEdge("F", "H"));
        System.out.println(graph.hasEdge("A", "A"));
        System.out.println(graph.hasEdge("A", "F"));
        System.out.println(graph.hasEdge("A", "G"));
        System.out.println(graph.hasEdge("A", "H"));
        System.out.println(graph.getDistances().keySet().size());
        System.out.println(graph.getDistances().get("A"));
        System.out.println(graph.getDistances().get("B"));
        System.out.println(graph.getDistances().get("C"));
        System.out.println(graph.getDistances().get("D"));
        System.out.println(graph.getDistances().get("E"));
        System.out.println(graph.getDistances().get("F"));
        System.out.println(graph.getDistances().get("G"));
        System.out.println(graph.getDistances().get("H"));
    }

    private void tests2() {
        System.out.println(graph.hasEdge("131","1"));
        System.out.println(graph.hasEdge("132","1"));
        System.out.println(graph.hasEdge("227","23"));
        System.out.println(graph.hasEdge("228","23"));
        System.out.println(graph.hasEdge("500","23"));
        System.out.println(graph.hasEdge("42","41"));
    }

    public static void main(String[] args) throws FileNotFoundException {
        Doer d = new Doer();
        d.loadData("Test.txt");
        d.checkNodesAndEdges();
        d.graph.findDistances();
//        d.tests1();
//        d.tests2();
//        System.out.println(d.graph.findPaths("H", "A"));
//        System.out.println(d.graph.nodeBetweenness("A"));
        System.out.println(d.graph.edgeBetweenness("D", "F"));
    }

}
