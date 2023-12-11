package c_labyrinth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Zwickelstorfer Felix
 */
public class Labyrinth {

    public static String[][] maps = {{
            "############",
            "#  #     # #",
            "## # ### # #",
            "#  # # # # #",
            "## ### # # #",
            "#        # #",
            "## ####### #",
            "#          #",
            "# ######## #",
            "# #   #    #",
            "#   #   # ##",
            "######A#####"
    }, {
            "################################",
            "#                              #",
            "# ############################ #",
            "# # ###       ##  #          # #",
            "# #     ##### ### # ########## #",
            "# #   ##### #     # #      ### #",
            "# # ##### #   ###   # # ## # # #",
            "# # ### # ## ######## # ##   # #",
            "# ##### #  # #   #    #    ### #",
            "# # ### ## # # # # ####### # # #",
            "# #        # #   #     #     # #",
            "# ######## # ######### # ### # #",
            "# ####     #  # #   #  # ##### #",
            "# # #### #### # # # # ## # ### #",
            "#                      # #     #",
            "###########################A####"
    }, {
            "###########################A####",
            "#   #      ## # # ###  #     # #",
            "# ###### #### # # #### ##### # #",
            "# # ###  ## # # # #          # #",
            "# # ### ### # # # # # #### # # #",
            "# #     ### # # # # # ## # # # #",
            "# # # # ### # # # # ######## # #",
            "# # # #     #          #     # #",
            "# ### ################ # # # # #",
            "# #   #             ## # #   # #",
            "# # #### ############# # #   # #",
            "# #                    #     # #",
            "# # #################### # # # #",
            "# # #### #           ###     # #",
            "# # ## # ### ### ### ### # ### #",
            "# #    #     ##  ##  # ###   # #",
            "# ####   ###### #### # ###  ## #",
            "###########################A####"
    }, {
            "#############",
            "#           #",
            "#           #",
            "#           #",
            "###########A#"
    }};

    /**
     * Wandelt (unveränderliche) Strings in Char-Arrays
     *
     * @param map der Plan, ein String je Zeile
     * @return char[][] des Plans
     */
    public static char[][] fromStrings(String[] map) {
        char res[][] = new char[map.length][];
        for (int i = 0; i < map.length; i++) {
            res[i] = map[i].toCharArray();
        }
        return res;
    }


    /**
     * Ausgabe des Layrinths
     *
     * @param lab
     */
    public static void printLabyrinth(char[][] lab) {
        for (char[] chars : lab) {
            System.out.println();
            for (char aChar : chars) {
                System.out.print(aChar);
            }
        }
        System.out.println();
    }

    /**
     * Suche den Weg
     *
     * @param zeile  aktuelle Position
     * @param spalte aktuelle Position
     * @param lab
     * @throws InterruptedException für die verlangsamte Ausgabe mit sleep()
     */
    public static boolean suchen(int zeile, int spalte, char[][] lab) throws InterruptedException {
        if (zeile < 0 && spalte < 0 || zeile >= lab.length || spalte >= lab[0].length) return false;
        if (lab[zeile][spalte] == 'A') return true;
        if (lab[zeile][spalte] == '#' || lab[zeile][spalte] == 'X') return false;
        char old = lab[zeile][spalte];

        lab[zeile][spalte] = 'X';

        if (suchen(zeile + 1, spalte, lab)) return true;
        if (suchen(zeile, spalte + 1, lab)) return true;
        if (suchen(zeile - 1, spalte, lab)) return true;
        if (suchen(zeile, spalte - 1, lab)) return true;

        lab[zeile][spalte] = old;
        return false;
    }


    /**
     * Suche alle möglichen Wege
     *
     * @param zeile  aktuelle Position
     * @param spalte aktuelle Position
     * @param lab das aktuelle labyrinth
     * @throws InterruptedException für die verlangsamte Ausgabe mit sleep()
     */
    public static int suchenAlle(int zeile, int spalte, char[][] lab) throws InterruptedException {
        if (zeile < 0 && spalte < 0 || zeile >= lab.length || spalte >= lab[0].length) return 0;
        if (lab[zeile][spalte] == 'A') return 1;
        if (lab[zeile][spalte] == '#' || lab[zeile][spalte] == 'X') return 0;
        char old = lab[zeile][spalte];
        lab[zeile][spalte] = 'X';
        int amount = 0;
        amount += suchenAlle(zeile + 1, spalte, lab);
        amount += suchenAlle(zeile, spalte + 1, lab);
        amount += suchenAlle(zeile - 1, spalte, lab);
        amount += suchenAlle(zeile, spalte - 1, lab);
        lab[zeile][spalte] = old;
        return amount;
    }

    /**
     * a method that reads a file and makes it to a char[][] array
     * @param path the file path
     * @throws IOException if the file doesn't exist
     */
    public static char[][] fromFile(Path path) throws IOException {
        return fromStrings(Files.readAllLines(path).toArray(new String[0]));
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        var map=3;
        char[][] labyrinth = fromStrings(maps[map]);
        printLabyrinth(labyrinth);
//        System.out.println("Ausgang gefunden: " + (suchen(5, 5, labyrinth) ? "ja" : "nein"));
//        printLabyrinth(labyrinth);

        labyrinth = fromStrings(maps[map]);
        System.out.println("Anzahl Wege: " + suchenAlle(5, 5, labyrinth));


//        printLabyrinth(fromFile(Paths.get("res/c_labyrinth/l2.txt")));
//        System.out.println((suchenAlle(5,5,fromFile(Paths.get("res/c_labyrinth/l3.txt")))));
    }
}
