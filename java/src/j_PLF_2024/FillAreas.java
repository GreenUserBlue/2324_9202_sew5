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
        if (x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight()) return;
        if (!colorEquals(img.getRGB(x, y), colorToFill)) return;
        img.setRGB(x, y, fillColor);
        fillArea(img, x + 1, y, colorToFill, fillColor);
        fillArea(img, x - 1, y, colorToFill, fillColor);
        fillArea(img, x, y + 1, colorToFill, fillColor);
        fillArea(img, x, y - 1, colorToFill, fillColor);
    }

    public static boolean colorEquals(int rgb, int color) {
        return (rgb & 0xFF_FF_FF) == (color | (color << 8) | (color << 16));
    }

    public static int countAndFillAreas(BufferedImage img, int colorToFill) {
        int counter = 0;
        int startValue = (int) (Math.random() * PALLET.length);
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                if (colorEquals(img.getRGB(i, j), colorToFill)) {
//                    fillArea(img, i, j, colorToFill, PALLET[(int) (Math.random() * PALLET.length)]);
                    fillArea(img, i, j, colorToFill, PALLET[(counter + startValue) % PALLET.length]);
                    counter++;
                }
            }
        }

        return counter;
    }

    public static void main(String[] args) throws IOException {

        BufferedImage img = readImageFile("res/j_PLF_2024/areas_003.png");
        fillArea(img, 45, 100, COLOR_TO_FILL, PALLET[1]);
        writeImageFile(img, "res/j_PLF_2024/areas_003_first.png");


        img = readImageFile("res/j_PLF_2024/areas_003.png");
        System.out.println(countAndFillAreas(img, COLOR_TO_FILL));
        writeImageFile(img, "res/j_PLF_2024/areas_003_second.png");


        createAreasFilledFile("res/j_PLF_2024/areas_003");
        createAreasFilledFile("res/j_PLF_2024/areas_006");
        createAreasFilledFile("res/j_PLF_2024/europakarte");
    }

    private static void createAreasFilledFile(String fileNameWithoutExtension) throws IOException {
        BufferedImage img = readImageFile(fileNameWithoutExtension + ".png");
        int filledAreas = countAndFillAreas(img, COLOR_TO_FILL);
        writeImageFile(img, fileNameWithoutExtension + ".colored.png");
        System.out.printf("file %s.png filled with %d areas\n", fileNameWithoutExtension, filledAreas);
    }

}
