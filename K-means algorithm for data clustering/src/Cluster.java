import java.util.ArrayList;

public class Cluster {
    private final ArrayList<Point> points;
    private Point centroid;

    Cluster() {
        points = new ArrayList<>();
    }

    public void addPoint(Point p) { points.add(p); }

    public ArrayList<Point> getPoints() { return points; }

    public void clearPoints() { points.removeAll(points); }

    public Point getCentroid() { return centroid; }

    public void setCentroid(double x, double y) { centroid = new Point(x, y); }

    public Point computeCentroid() {
        double x = 0;
        double y = 0;
        for (Point p : points) {
            x += p.getX();
            y += p.getY();
        }
        centroid = new Point(x/points.size(), y/points.size());
        return centroid;
    }
}
