package w_ff_selection_tests;

public class Vector2 {
   public double x;
    public double y;

    public Vector2(Number x, Number y) {
        this.x = x.doubleValue();
        this.y = y.doubleValue();
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
