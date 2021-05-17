import java.util.*;

public class Graph {
    private int numNodes, numEdges;
    private HashMap<String, Node> nodes;
    private HashMap<String, HashMap<String, Integer>> distances;

    public Graph() {
        numNodes = 0;
        numEdges = 0;
        nodes = new HashMap<>();
        distances = new HashMap<>();
    }

    public int getNumNodes() { return numNodes; }

    public int getNumEdges() { return numEdges; }

    public HashMap<String, Node> getNodes() { return nodes; }

    public HashMap<String, HashMap<String, Integer>> getDistances() { return distances; }

    public void addNode(String name) {
        nodes.put(name, new Node(name));
        distances.put(name, new HashMap<>());
        for (String name2 : nodes.keySet()) {
            distances.get(name).put(name2, Integer.MAX_VALUE);
            if (name.equals(name2)) distances.get(name2).put(name, 0);
            else distances.get(name2).put(name, Integer.MAX_VALUE);
        }
        numNodes++;
    }

    public void removeNode(String name) {
        try {
            for (Node node : nodes.values()) node.getNeighbors().remove(name);
            nodes.remove(name);
            distances.remove(name);
            for (String name2 : nodes.keySet()) distances.get(name2).remove(name);
            numNodes--;
        }
        catch (Exception e) {
            System.out.println("Node " + name + " does not exist in graph");
        }
    }

    public Node getNode(String name) { return nodes.get(name); }

    public boolean hasNode(String name) { return nodes.containsKey(name); }

    public void addEdge(String name1, String name2) {
        try {
            getNode(name1).getNeighbors().add(name2);
            getNode(name2).getNeighbors().add(name1);
            numEdges++;
        }
        catch (Exception e) {
            System.out.println("One or both nodes do not exist in graph");
        }
    }

    public void removeEdge(String name1, String name2) {
        try {
            getNode(name1).getNeighbors().remove(name2);
            getNode(name2).getNeighbors().remove(name1);
            numEdges--;
        }
        catch (Exception e) {
            System.out.println("One or both nodes do not exist in graph");
        }
    }

    public boolean hasEdge(String name1, String name2) {
        return (hasNode(name1) && getNode(name1).getNeighbors().contains(name2));
    }

    public void BFS(String start) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            String name1 = queue.remove();
            for (String name2 : getNode(name1).getNeighbors()) {
                if (!visited.contains(name2)) {
                    visited.add(name2);
                    queue.add(name2);
                    distances.get(start).put(name2, distances.get(start).get(name1)+1);
                }
            }
        }
    }

    public void findDistances() {
        for (String name : nodes.keySet()) BFS(name);
    }

    public ArrayList<LinkedList<String>> findPaths(String start, String end) {
        return findPathsHelper(new ArrayList<>(), start, end);
    }

    private ArrayList<LinkedList<String>> findPathsHelper(ArrayList<LinkedList<String>> pathsSoFar, String start, String node) {
        if (pathsSoFar.size() == 0) {
            LinkedList<String> newPath = new LinkedList<>();
            newPath.add(node);
            pathsSoFar.add(newPath);
        }
        else {
            for (LinkedList<String> path : pathsSoFar) path.add(node);
            if (node.equals(start)) return pathsSoFar;
        }

        ArrayList<LinkedList<String>> pathsWithNode = new ArrayList<>();
        for (String neighbor : getNode(node).getNeighbors()) {
            if (distances.get(start).get(neighbor) == distances.get(start).get(node)-1) {
                ArrayList<LinkedList<String>> paths = new ArrayList<>();
                for (LinkedList<String> path : pathsSoFar) paths.add((LinkedList<String>)path.clone());
                ArrayList<LinkedList<String>> pathsWithNeighbor = findPathsHelper(paths, start, neighbor);
                pathsWithNode.addAll(pathsWithNeighbor);
            }
        }

        return pathsWithNode;
    }

    public double nodeBetweenness(String name) {
        double nodeBetweenness = 0;
        for (String node1 : nodes.keySet()) {
            for (String node2 : nodes.keySet()) {
                if (!node1.equals(name) && !node2.equals(name) && !node1.equals(node2)) {
                    System.out.println(node1 + "-" + node2 + ": ");
                    ArrayList<LinkedList<String>> shortestPaths = findPaths(node1, node2);
                    System.out.println(shortestPaths);
                    double numShortestPaths = (double)shortestPaths.size();
                    System.out.println("numShortestPaths: " + numShortestPaths);
                    double numShortestPathsThroughNode = 0;
                    for (LinkedList<String> path : shortestPaths) {
                        if (path.contains(name)) numShortestPathsThroughNode++;
                    }
                    System.out.println("numShortestPathsThroughNode: " + numShortestPathsThroughNode);
                    nodeBetweenness += numShortestPathsThroughNode/numShortestPaths;
                    System.out.println("nodeBetweenness: " + nodeBetweenness);
                }
            }
        }
        return nodeBetweenness/2;
    }

    public double edgeBetweenness(String name1, String name2) {
        double edgeBetweenness = 0;
        for (String node1 : nodes.keySet()) {
            for (String node2 : nodes.keySet()) {
                if (!node1.equals(node2)) {
                    System.out.println(node1 + "-" + node2 + ": ");
                    ArrayList<LinkedList<String>> shortestPaths = findPaths(node1, node2);
                    System.out.println(shortestPaths);
                    double numShortestPaths = (double)shortestPaths.size();
                    System.out.println("numShortestPaths: " + numShortestPaths);
                    double numShortestPathsThroughEdge = 0;
                    for (LinkedList<String> path : shortestPaths) {
                        if (path.contains(name1) && path.contains(name2)) numShortestPathsThroughEdge++;
                    }
                    System.out.println("numShortestPathsThroughEdge: " + numShortestPathsThroughEdge);
                    edgeBetweenness += numShortestPathsThroughEdge/numShortestPaths;
                    System.out.println("edgeBetweenness: " + edgeBetweenness);
                }
            }
        }
        return edgeBetweenness/2;
    }

}
