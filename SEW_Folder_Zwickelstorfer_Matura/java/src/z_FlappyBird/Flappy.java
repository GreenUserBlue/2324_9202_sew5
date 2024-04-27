package z_FlappyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.HashSet;

public class Flappy extends JPanel implements MouseListener {

    private GameState gameState = GameState.HOME;

    private final boolean[] pressed = new boolean[2];

    private Pipe[] pipes = new Pipe[10];

    private int pipeCounter = 0;

    private int timeTillNextPipe = 0;

    public static final int scale = 30;

    private int posY = scale * 8;

    private double velocity = 0;

    private int highScore = 0;

    public Flappy() {
        addMouseListener(this);
    }

    private HashSet<Pipe> passedPipes = new HashSet<>();

    public static void main(String[] args) {
        Flappy flappy = new Flappy();
        JFrame frame = new JFrame();
        frame.getContentPane().add(flappy);
        frame.setTitle("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setSize(9 * scale + 14, 16 * scale + 35);
        frame.setVisible(true);
        flappy.start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        pressed[0] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void paint(Graphics g) { // paint() method
        super.paint(g);
        g.setColor(new Color(0, 165, 255));
        g.fillRect(0, 0, scale * 9, scale * 16);
        g.setColor(new Color(0, 255, 0));
        g.fillRect(0, scale * 15, scale * 9, scale);
        switch (gameState) {
            case HOME -> updateHome(g);
            case PLAYING -> updateGame(g);
            case DEAD -> updateDead(g);
        }
    }

    private void updateDead(Graphics g) {
        velocity = 0;
        for (Pipe p : pipes) if (p != null) p.draw(g);
        draw(g, posY);
        drawScore(g);

        if (pressed[0] && !pressed[1]) {
            highScore = Math.max(passedPipes.size(), highScore);
            gameState = GameState.HOME;
        }
        pressed[1] = pressed[0];
        pressed[0] = false;
    }

    private void updateHome(Graphics g) {
        boolean isPressed = pressed[0];
        pressed[1] = false;
        pressed[0] = posY > scale * 9 || isPressed;
        updateVel();
        pressed[0] = false;
        posY += velocity;
        if (isPressed) restartGame();
        draw(g, posY);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Monospace", Font.LAYOUT_NO_LIMIT_CONTEXT, scale * 2));
        g.drawString("Home", (int) (scale * 1.75), scale * 4);
        drawScore(g);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Monospace", Font.LAYOUT_NO_LIMIT_CONTEXT, scale));
        g.drawString(highScore + "", (int) (scale * 0.5), (int) (scale * 2.5));
    }

    private void restartGame() {
        gameState = GameState.PLAYING;
        passedPipes.clear();
        pipeCounter = 0;
        timeTillNextPipe = 0;
        Arrays.fill(pipes, null);
    }

    private void draw(Graphics g, int posY) {
        g.setColor(Color.YELLOW);
        g.fillRect(scale, posY, (int) (scale * 1.4), scale);
    }

    private void updateGame(Graphics g) {
        updateVel();
        pressed[1] = pressed[0];
        pressed[0] = false;
        posY += velocity;
        if (posY < 0) {
            posY = 0;
            velocity = Math.min(0.25 * scale / 60, velocity);
        }
        if (posY >= scale * 14) {
            gameState = GameState.DEAD;
            pressed[1] = true;
        }
        drawAndUpdatePipes(g);
        draw(g, posY);
        drawScore(g);
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Monospace", Font.LAYOUT_NO_LIMIT_CONTEXT, scale));
        g.drawString(passedPipes.size() + "", (int) (scale * 0.5), (int) (scale * 1.25));
    }

    private void drawAndUpdatePipes(Graphics g) {
        timeTillNextPipe--;
        for (int i = 0; i < pipes.length; i++) {
            Pipe pipe = pipes[i];
            if (timeTillNextPipe <= 0) {
                timeTillNextPipe = 200 + (int) (Math.random() * 50);
                pipes[pipeCounter % pipes.length] = new Pipe();
                pipeCounter++;
            }
            if (pipe != null) {
                pipe.update(1);
                pipe.draw(g);
                if (pipe.collides(posY)) {
                    gameState = GameState.DEAD;
                    pressed[1] = true;
                }
                if (pipe.getPos() < scale) {
                    passedPipes.add(pipe);
                }
            }
        }
    }

    private void updateVel() {
        velocity += 0.35 * scale / 30;
        if (pressed[0] && !pressed[1]) {
            velocity = -0.20 * scale;
        }
    }

    public void start() {
        int frames = 1000 / 60;
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(frames);
                } catch (InterruptedException ignored) {
                }
                repaint();
            }
        }).start();
    }


    enum GameState {
        PLAYING,
        HOME,
        DEAD
    }
}
