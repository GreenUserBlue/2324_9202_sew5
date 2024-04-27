package f_Steganography_Michi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bild_Level_4 {

    public static void main(String[] args) throws IOException {
        String secretFilePath = "./src/Geheimnachricht";
        String inFilePath = "./src/Level_4.png";
        int howManyBits = 1;

        seek(inFilePath,"./src/GeheimnachrichtSolved_2",howManyBits);
    }

    public static void seek(String inFilePath, String outFilePath, int howManyBits) throws IOException {
        BufferedImage image = ImageIO.read(new File(inFilePath));
        int width = image.getWidth();
        int height = image.getHeight();

        String lsg = "";
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                System.out.println(pixel);
                /*Tipp: Die ersten 8 Bits sind für die Farbe Blau
                        Die zweien 8 Bits sind für die Farbe Grün
                        Die dritten 8 Bits sind für die Farbe Rot
                 */
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                lsg+=(getLastBits(red, howManyBits));
                lsg+=(getLastBits(green, howManyBits));
                lsg+=(getLastBits(blue, howManyBits));
            }
        }

        byte[] secretMessageBytes = bitsToByteArray(lsg);
        java.nio.file.Files.write(java.nio.file.Paths.get(outFilePath), secretMessageBytes);
    }

    private static String getLastBits(int value, int numBits) {
        int bits = value & ((1 << numBits) - 1);
        return String.format("%" + numBits + "s", Integer.toBinaryString(bits)).replace(' ', '0');
    }


    private static byte[] bitsToByteArray(String bits) {
        int byteLength = bits.length() / 8;
        byte[] byteArray = new byte[byteLength];

        for (int i = 0; i < byteLength; i++) {
            int byteValue = Integer.parseInt(bits.substring(i * 8, (i + 1) * 8), 2);
            byteArray[i] = (byte) byteValue;
        }

        return byteArray;
    }
}