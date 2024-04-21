package f_Steganography_Michi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Steganography {

    public static void main(String[] args) {
        try {
            BufferedImage originalImage = ImageIO.read(new File("res/f_Stegano/Donut.jpg")); // Load image
            String secretMessage = "This Secret Message is: \"Geröt\"";
            int channels = 1;
            int bitsToSet = 8;
            BufferedImage encodedImage = encodeText(originalImage, secretMessage, bitsToSet, channels);
            File outputFile = new File("res/f_Stegano/Donut_enc.png");
            ImageIO.write(encodedImage, "png", outputFile); // Save encoded image

            String decodedMessage = decodeText(encodedImage, bitsToSet, channels);
            System.out.println("Decoded Message: " + decodedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage encodeText(BufferedImage image, String text, int bitsToSet, int channels) {
        int[] rgbArray = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        boolean[] message = getBits(text);
        int bitCounter = 0;
        int[] bitIndicesToReplace = getBitIndicesToReplace(bitsToSet, channels);
        int pixelCounter = Math.ceilDiv(32, bitsToSet * channels);
        if (message.length / channels / bitsToSet + pixelCounter > image.getWidth() * image.getHeight()) {
            throw new IllegalArgumentException("Image not large enough to hold message.");
        }
        boolean[] b = intToBooleanArray(message.length / 8);
        for (int i = 0; i < pixelCounter; i++) {
            for (int index : bitIndicesToReplace) {
                rgbArray[i] = replaceAt(rgbArray[i], index, b[bitCounter++] ? 1 : 0);
                if (bitCounter >= b.length) {
                    break;
                }
            }
        }

        bitCounter = 0;
        while (bitCounter < message.length) {
            for (int index : bitIndicesToReplace) {
                rgbArray[pixelCounter] = replaceAt(rgbArray[pixelCounter], index, message[bitCounter++] ? 1 : 0);
                if (bitCounter >= message.length) {
                    break;
                }
            }
            pixelCounter++;
        }
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        newImage.setRGB(0, 0, image.getWidth(), image.getHeight(), rgbArray, 0, image.getWidth());
        return newImage;
    }

    private static boolean[] intToBooleanArray(int src) {
        boolean[] res = new boolean[32];
        for (int i = 0; i < 32; i++) {
            if (((src >> i) & 1) == 1) {  // WICHTIG
                res[i] = true;
            }
        }
        return res;
    }

    private static int replaceAt(int src, int index, int var) {
        src = (src & (~(1 << index))); // WICHTIG
        src |= (var << index);  // WICHTIG
        return src;
    }

    private static int[] getBitIndicesToReplace(int bitsToSet, int channels) {
        int[] res = new int[bitsToSet * channels];
        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < bitsToSet; j++) {
                res[i * bitsToSet + j] = i * 8 + j;
            }
        }
        return res;
    }

    private static boolean[] getBits(String text) {
        byte[] bytes = text.getBytes();
        boolean[] res = new boolean[bytes.length * 8];
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < 8; j++) {
                res[i * 8 + j] = ((bytes[i] >> j) & 1) == 1;
            }
        }
        return res;
    }

    private static String decodeText(BufferedImage image, int bitsToSet, int channels) {
        int[] rgbArray = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        int pixelCounter = Math.ceilDiv(32, bitsToSet * channels);
        int[] bitIndicesToReplace = getBitIndicesToReplace(bitsToSet, channels);
        int length = 0;
        int bitCounter = 0;
        int messageCounter = 0;
        for (int i = 0; i < pixelCounter; i++) {
            for (int index : bitIndicesToReplace) {
                length |= (((rgbArray[i] >> index) & 1) << bitCounter++);
                if (bitCounter >= 32) {
                    break;
                }
            }
        }
        byte[] message = new byte[length];

        bitCounter = 0;
        while (bitCounter < message.length * 8) {
            for (int index : bitIndicesToReplace) {
                message[messageCounter] |= (((rgbArray[pixelCounter] >> index) & 1) << (bitCounter++ % 8));
                if (bitCounter % 8 == 0) messageCounter++;
                if (bitCounter >= message.length * 8) {
                    break;
                }
            }
            pixelCounter++;
        }
        return new String(message);
    }
}