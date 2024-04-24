package g_steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PngSteganographie4pupil {

    /**
     * Wandelt ein byte in einen Bit-String um.
     * Bsp. toBinString((byte)0xaf): 1010.1111
     *
     * @param n ist das Byte, das umgewandelt wird
     * @return der Bit-String ist das umgewandelte Byte
     */
    public static String toBinString(byte n) {
        // Hier ist kein Code zu verändern
        String bits = "00000000" + Integer.toBinaryString(n);
        bits = bits.substring(bits.length() - 8);
        return bits.substring(0, 4) + "." + bits.substring(4, 8);
    }

    /**
     * Wandelt einen int-Wert in einen Bit-String um.
     * Bsp toBinString((0xabcd_ef98): 1100.1101  1100.1101  1110.1111  1001.1000
     *
     * @param n ist das int, das umgewandelt wird
     * @return der Bit-String ist das umgewandelte int
     */
    public static String toBinString(int n) {
        // Hier ist kein Code zu verändern
        String bits = "0".repeat(32) + Integer.toBinaryString(n);
        bits = bits.substring(bits.length() - 32);

        return bits.substring(0, 4) + "." + bits.substring(4, 8) + "  " +
                bits.substring(8, 12) + "." + bits.substring(12, 16) + "  " +
                bits.substring(16, 20) + "." + bits.substring(20, 24) + "  " +
                bits.substring(24, 28) + "." + bits.substring(28, 32);
    }

    /**
     * Wandelt Pixel aus einem Image in einen Bit-String um. Jedes Pixel besteht
     * aus 4 Bytes, die einzelnen Pixel werden durch einen senkrechten Strich
     * voneinander getrennt.
     * Beispiel: 1111.1111  1101.0010  1101.0010  1101.0010 | 1111.1111  1101.0010  1101.0010  1101.0010
     *
     * @param img    das Bild, aus dem die Pixel ausgelesen werden.
     * @param offset die Pixel-Positon des 1. Pixels (die Pixel werden von links oben
     *               nach rechts unten durchnummeriert)
     * @param length die Anzahl der Pixel, die ausgelesen werden sollen.
     * @return der Bit-String
     */
    public static String toBinString(BufferedImage img, int offset, int length) {
        // Hier ist kein Code zu verändern
        if (length <= 0) {
            return "";
        }

        StringBuilder b = new StringBuilder();
        int width = img.getWidth();

        for (int pos = offset; pos < offset + length; pos++) {
            b.append(" | ").append(toBinString(img.getRGB(pos % width, pos / width)));
        }
        return b.substring(3);
    }

    /**
     * Liest ein PNG-File ein.
     *
     * @param filename der Pfad + Dateiname des PNG-Files
     * @return das PNG-Bild
     */
    public static BufferedImage readPngFile(String filename) throws IOException {
        return ImageIO.read(new File(filename));
    }

    /**
     * Schreibt ein PNG-Bild in eine Datei.
     *
     * @param filename der Pfad + Dateiname des PNG-Files
     * @param img      das PNG-Bild
     */
    public static void writePngFile(String filename, BufferedImage img) throws IOException {
        ImageIO.write(img, "png", new File(filename));
    }

    /**
     * Setzt ein Nibble (= 4 Bit = HalfByte) ins Pixel an der Stelle x,y.
     * Es wird jeweils ein Bit vom Nibble in das niederwertigste Bit von Alpha, r, g, oder b gesetzt.
     * <p>
     * Ist z.B. das Pixel (argb) 0xAABBCCDD = binär: 1010.1010 1011.1011 1100.1100 1101.1101
     * und das Nibble 0x08 = binär. 1000, dann ist der neue Pixelwert:
     * binär: 1010.1011 1011.1010 1100.1100 1101.1100
     * neu gesetzt:   ^         ^         ^         ^
     *
     * @param img      das Bild, in dem das Nibble versteckt wird
     * @param pixelPos die Pixel-Position (die Pixel werden von links oben
     *                 nach rechts unten durchnummeriert)
     * @param nibble   das nibble, das versteckt werden soll.
     */
    public static void setNibble(BufferedImage img, int pixelPos, byte nibble) {
        int imageData = img.getRGB(pixelPos % img.getWidth(), pixelPos / img.getWidth());
        for (int j = 0; j < 4; j++) {
            imageData = replaceAt(imageData, ((nibble >> j) & 1), j * 8);
        }
        img.setRGB(pixelPos % img.getWidth(), pixelPos / img.getWidth(), imageData);
    }

    public static int replaceAt(int data, int replaceable, int position) {
        int mask = (~(1 << position));
        data = (data & mask);
        data |= (replaceable << position);
        return data;

//        it is the same as above, however this is in one line
//        return (data & (~(1 << position))) | (replaceable << position);
    }

    /**
     * Liest aus dem PNG-Bild aus dem Pixel x, y ein Nibble aus.
     * Genaue Erklärung, siehe {@link #setNibble(BufferedImage, int, byte)}
     *
     * @param img      das Bild, in dem das Nibble versteckt wird
     * @param pixelPos die Pixel-Position (die Pixel werden von links oben
     *                 nach rechts unten durchnummeriert)
     * @return das ausgelesene Nibble
     */
    public static byte getNibble(BufferedImage img, int pixelPos) {
        int images = img.getRGB(pixelPos % img.getWidth(), pixelPos / img.getWidth());
        byte nibble = 0;
        for (int j = 0; j < 4; j++) {
            nibble |=(((images >> (j * 8)) & 1) << j);
        }
        return nibble;
    }

    /**
     * Speichert die beiden Nibble des Bytes b in das Bild, dazu werden 2 Pixel benötigt.
     *
     * @param img      das Bild, in dem das Byte b versteckt wird
     * @param pixelPos die Pixel-Position für das höherwertige Nibble von b
     *                 (die Pixel werden von links oben nach rechts unten durchnummeriert)
     * @param b        das Byte, das versteckt werden soll
     */
    public static void setByte(BufferedImage img, int pixelPos, byte b) {
        setNibble(img, pixelPos, (byte) ((b >> 4) & 0xf));
        setNibble(img, pixelPos + 1, (byte) (b & 0xf));
    }


    /**
     * Liest ein verstecktes Byte (= 2 Nibbles) aus einem Bild.
     *
     * @param img      das Bild, in dem das Byte versteckt ist
     * @param pixelPos die Pixel-Position für das höherwertige Nibble von b
     *                 (die Pixel werden von links oben nach rechts unten durchnummeriert)
     * @return das versteckte Byte
     */
    public static byte getByte(BufferedImage img, int pixelPos) {
        return (byte) ((getNibble(img, pixelPos) << 4) | getNibble(img, pixelPos + 1));
    }


    /**
     * Versteckt ein ganzes Byte-Array in einem Bild.
     * Zuerst wird die Länge des Byte-Arrays info als int-Zahl gespeichert,
     * danach das eigentliche Byte-Array info, mit dem Byte mit dem Index 0 beginnend.
     * Tipp: verwende die schon vorhandenen Methoden.
     *
     * @param img  das Bild, in dem alles versteckt wird
     * @param info das Byte-Array, das versteckt wird
     */
    public static void setByteArray(BufferedImage img, byte info[]) {
        //32 bit => 4 pro byte saven => 8 byte benötigt
        for (int i = 0; i < 4; i++) {
            setByte(img, i * 2, (byte) ((info.length >> (i * 8)) & 0xff));
        }

        for (int i = 0; i < info.length; i++) {
            setByte(img, 8 + i * 2, info[i]);
        }
    }


    /**
     * Extrahiert das versteckte Byte-Array aus dem Bild.
     * <p>
     * Zuerst ist die Länge des Byte-Arrays info als int-Zahl gespeichert,
     * danach das eigentliche Byte-Array info, mit dem Byte mit dem Index 0 beginnend.
     * Tipp: verwende die schon vorhandenen Methoden.
     *
     * @param img das Bild mit dem versteckten Byte-Array
     * @return das versteckte Byte-Array
     */
    public static byte[] getByteArray(BufferedImage img) {
        int len = 0;
        //32 bit => 4 pro byte saven => 8 byte benötigt
        for (int i = 0; i < 4; i++) {
            len |= (getByte(img, i * 2) << (i * 8));
        }
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = getByte(img, 8 + i * 2);
        }
        return bytes;
    }

    /**
     * Extrahiert aus einem PNG-Bild das versteckte Byte-Array
     *
     * @param filename der Dateiname samt Pfad zum Bild
     * @return das versteckte Byte-Array
     * @throws IOException bei Lesefehlern
     */
    public static byte[] readSteganographieBytesFromPngFile(String filename) throws IOException {
        return getByteArray(readPngFile(filename));
    }

    /**
     * Versteckt ein Byte-Array in einer PNG-Datei und speichert sie
     *
     * @param filename Dateiname inkl. Pfad zur Ausgabedatei
     * @param img      das Bild, in dem das Byte-Array versteckt wird
     * @param info     das Byte-Array, das versteckt werden soll
     * @throws IOException bei Schreibfehlern
     */
    public static void writeSteganographieBytesToPngFile(String filename, BufferedImage img, byte info[]) throws IOException {
        setByteArray(img, info);
        writePngFile(filename, img);
    }

    public static void main(String[] args) throws IOException {
        // Testcode von BRE, ergänze/verändere ihn, wie du willst
        BufferedImage img = readPngFile("res/g_steganography/img.png");

        System.out.println(toBinString(img, 0, 2));
        setByte(img, 1, (byte) 255);
        System.out.println(getByte(img, 1) & 255);
        System.out.println(toBinString(img, 1, 2));

        setByteArray(img, "abcd".getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(getByteArray(img), StandardCharsets.UTF_8));
        writeSteganographieBytesToPngFile("res/g_steganography/img.geheim.png", img,
                "Viel Erfolg bei der SEW-Matura!".getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(readSteganographieBytesFromPngFile("res/g_steganography/img.geheim.png"), StandardCharsets.UTF_8));
    }
}
