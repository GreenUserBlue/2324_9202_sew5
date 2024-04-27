package j_PLF_2024;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FillAreas {
    public static final int[] PALLET = new int[]{
            Color.RED.getRGB(),
            Color.GREEN.getRGB(),
            Color.BLUE.getRGB(),
            Color.YELLOW.getRGB(),
            Color.CYAN.getRGB(),
            Color.MAGENTA.getRGB(),
            Color.ORANGE.getRGB(),
            Color.PINK.getRGB(),
    };
    public static final int COLOR_TO_FILL = 210;


    public static void writeImageFile(BufferedImage file, String path) throws IOException {
        ImageIO.write(file, "png", new File(path));
    }

    public static BufferedImage readImageFile(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    public static void fillArea(BufferedImage img, int x, int y, int colorToFill, int fillColor) {
        if (x < 0 || x >= img.getWidth() || y < 0 || y > img.getHeight()) return;
        if ((img.getRGB(x, y) & 0xFF_FF_FF) != (colorToFill | (colorToFill << 8) | (colorToFill << 16))) return;
        img.setRGB(x, y, fillColor);
        fillArea(img, x + 1, y, colorToFill, fillColor);
        fillArea(img, x - 1, y, colorToFill, fillColor);
        fillArea(img, x, y + 1, colorToFill, fillColor);
        fillArea(img, x, y - 1, colorToFill, fillColor);
    }

    public static void main(String[] args) throws IOException {
        BufferedImage img = readImageFile("res/j_PLF_2024/areas_003.png");
        fillArea(img, 45, 100, COLOR_TO_FILL, PALLET[1]);
        writeImageFile(img, "res/j_PLF_2024/areas_003_new.png");
    }
}
