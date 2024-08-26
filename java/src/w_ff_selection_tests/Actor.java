package w_ff_selection_tests;

public class Actor {
    public double x;
    public double y;

    public double width=5;
    public double height=5;


    public Actor(Number x, Number y) {
        this.x = (double) x;
        this.y = (double) y;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
