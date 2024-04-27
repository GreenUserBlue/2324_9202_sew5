package z_FlappyBird;

import java.awt.*;
import java.util.Random;

public class Pipe {

    private double pos = Flappy.scale * 11;

    private static double WIDTH = 2 * Flappy.scale;

    private static int GAP = 4 * Flappy.scale;

    private double height;

    private static Random r = new Random();

    public Pipe() {
        height = ((int) (r.nextDouble() * 9) + 1.5) * Flappy.scale;
    }

    private static final double speed = 1 / 30.;

    public void update(double mult) {
        pos -= Flappy.scale * speed * mult;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 86, 0));
        g.fillRect((int) pos, 0, (int) WIDTH, (int) height);
        g.fillRect((int) pos, (int) (height + GAP), (int) WIDTH, (int) Flappy.scale * 16);
        g.setColor(new Color(0, 126, 0));
        g.fillRect((int) (pos - WIDTH * 0.15), (int) (height + GAP), (int) (WIDTH * 1.3), Flappy.scale);
        g.fillRect((int) (pos - WIDTH * 0.15), (int) (height - Flappy.scale), (int) (WIDTH * 1.3), Flappy.scale);
    }

    public double getPos() {
        return pos;
    }

    public boolean collides(int posY) {
        if (pos > Flappy.scale * 0.4 + WIDTH || pos < Flappy.scale * 1.4 - WIDTH) {
            return false;
        }
        if (posY < height || posY > height + GAP - Flappy.scale) {
            return true;
        }
        return false;
    }
}
