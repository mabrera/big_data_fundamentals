import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Doer {
    private final ArrayList<Point> points;
    private final ArrayList<Point> centroids;
    private final ArrayList<Cluster> clusters;
    private int numPoints, numCentroids;

    Doer(){
        points = new ArrayList<>();
        centroids = new ArrayList<>();
        clusters = new ArrayList<>();
    }

    private void loadData(String filename) throws FileNotFoundException {
        File f = new File(filename);
        Scanner sc = new Scanner(f);
        String[] firstLineData = sc.nextLine().split("   ");
        numCentroids = Integer.parseInt(firstLineData[0]);
        numPoints = Integer.parseInt(firstLineData[1]);
        while (sc.hasNextLine()) {
            String[] lineData = sc.nextLine().split("\t");
            points.add(new Point(Double.parseDouble(lineData[0]),Double.parseDouble(lineData[1])));
        }
        assert points.size() == numPoints : "Didn't generate correct number of points";
    }

    private void selectInitialCentroids() {
        Collections.shuffle(points);
        for (int i=0; i<numCentroids; i++) {
            centroids.add(points.get(i));
            clusters.add(new Cluster());
        }
        System.out.println("initial centroids:");
        for (int i=0; i<numCentroids; i++) System.out.println(centroids.get(i));
    }

    private void recalibrateClusters() {
        for (Point p : points) {
            int clusterIndex = -1;
            double minDistance = Double.MAX_VALUE;
            for (int i=0; i<numCentroids; i++) {
                double centroidDistance = Math.pow((p.getX()-centroids.get(i).getX()),2) + Math.pow((p.getY()-centroids.get(i).getY()),2);
                if (centroidDistance < minDistance) {
                    minDistance = centroidDistance;
                    clusterIndex = i;
                }
            }
            assert clusterIndex != -1 : "Failed to find appropriate cluster";
            clusters.get(clusterIndex).addPoint(p);
        }
        System.out.println("recalibrated clusters");
        System.out.println("cluster sizes now:");
        for (int i=0; i<numCentroids; i++) System.out.println(clusters.get(i).getPoints().size());
    }

    private void recalibrateCentroids() {
        centroids.removeAll(centroids);
        for (int i=0; i<numCentroids; i++) {
            centroids.add(clusters.get(i).computeCentroid());
            clusters.get(i).clearPoints();
        }
        System.out.println("new centroids:");
        for (int i=0; i<numCentroids; i++) System.out.println(centroids.get(i));
    }

    private ArrayList<Point> sortByX(ArrayList<Point> points) {
        ArrayList<Point> copy = (ArrayList<Point>) points.clone();
        ArrayList<Point> sorted = new ArrayList<>();
        while (copy.size() > 0) {
            Point smallest = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
            for (Point p : copy) {
                if (p.getX() < smallest.getX()) smallest = p;
            }
            sorted.add(smallest);
            copy.remove(smallest);
        }
        return sorted;
    }

    private boolean areArraysEqual(ArrayList<Point> l1, ArrayList<Point> l2) {
        assert l1.size() == l2.size() : "Arrays are of different size";
        for (int i=0; i<l1.size(); i++) {
            if (!l1.get(i).equals(l2.get(i))) return false;
        }
        return true;
    }

    private void k_means() {
        selectInitialCentroids();
        ArrayList<Point> oldCentroids = new ArrayList<>();
        for (int i=0; i<numCentroids; i++) oldCentroids.add(new Point(0, 0));
        ArrayList<Point> newCentroids = (ArrayList<Point>) sortByX(centroids).clone();
        while (!areArraysEqual(oldCentroids, newCentroids)) {
            oldCentroids = (ArrayList<Point>) newCentroids.clone();
            recalibrateClusters();
            recalibrateCentroids();
            newCentroids = (ArrayList<Point>) sortByX(centroids).clone();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Doer d = new Doer();
        d.loadData("Test-case-3.txt");
        d.k_means();
    }

}
