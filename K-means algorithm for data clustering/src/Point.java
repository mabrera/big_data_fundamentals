public class Point {
    private final double x, y;

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }

    public double getY() { return y; }

    public String toString(){ return "(" + x + ", " + y + ")"; }

    public boolean equals(Point p2) { return (x == p2.getX()) && (y == p2.getY()); }

}
