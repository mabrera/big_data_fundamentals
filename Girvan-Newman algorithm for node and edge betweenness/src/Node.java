import java.util.HashSet;

public class Node {
    private String name;
    private HashSet<String> neighbors;

    public Node(String name) {
        this.name = name;
        this.neighbors = new HashSet<>();
    }

    public String getName() { return name; }

    public HashSet<String> getNeighbors() { return neighbors; }

    @Override
    public String toString() { return name; }

}
