package c_labyrinth;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.zip.ZipEntry;

/**
 * @author Zwickelstorfer Felix
 * <p>
 * implementiert eine labyrinthlogik statisch
 */
public class Labyrinth2 {

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

    static class MyPoint {
        int x;
        int y;

        public MyPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyPoint myPoint = (MyPoint) o;
            return x == myPoint.x && y == myPoint.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    //    1. es verändern sich zeile, spalte, und die besuchten Punkte, und für die unbesuchten brauche ich zusätzlich das labyrinth selbst
    public static boolean suchen(int zeile, int spalte, char[][] lab, ArrayList<MyPoint> visitedPoints) throws InterruptedException {
//       2. Abbruchkriterien
        if (lab[zeile][spalte] == 'A') return true;
        if (lab[zeile][spalte] == '#') return false;
        if (visitedPoints.contains(new MyPoint(zeile, spalte))) return false;

        visitedPoints.add(new MyPoint(zeile, spalte));
//       3. was ich eigenltich machen muss (in jede Richtung gehen)
//       4. (durch das .clone() spare ich mir das zurücksetzten)
        if (suchen(zeile + 1, spalte, lab, (ArrayList<MyPoint>) visitedPoints.clone())) return true;
        if (suchen(zeile - 1, spalte, lab, (ArrayList<MyPoint>) visitedPoints.clone())) return true;
        if (suchen(zeile, spalte + 1, lab, (ArrayList<MyPoint>) visitedPoints.clone())) return true;
        if (suchen(zeile, spalte - 1, lab, (ArrayList<MyPoint>) visitedPoints.clone())) return true;
        return false;
    }

////5. Optimierung 1. visitedPoints als Parameter wegbekommen
//    public static boolean suchen(int zeile, int spalte, char[][] lab) throws InterruptedException {
//        if (lab[zeile][spalte] == 'A') return true;
//        if (lab[zeile][spalte] == '#') return false;
//
//        lab[zeile][spalte] = '#';
//        if (suchen(zeile + 1, spalte, lab)) return true;
//        if (suchen(zeile - 1, spalte, lab)) return true;
//        if (suchen(zeile, spalte + 1, lab)) return true;
//        if (suchen(zeile, spalte - 1, lab)) return true;
////        4. zurücksetzten, da ich es nach meinem durchlauf verändert habe
//        lab[zeile][spalte] = ' ';
//        return false;
//    }

    //5. Optimierung 2. Den Weg sichtbar machen
    public static boolean suchen(int zeile, int spalte, char[][] lab) throws InterruptedException {
        if (lab[zeile][spalte] == 'A') return true;
        if (lab[zeile][spalte] == '#') return false;
        if (lab[zeile][spalte] == 'X') return false;

        lab[zeile][spalte] = 'X';
        if (suchen(zeile + 1, spalte, lab)) return true;
        if (suchen(zeile - 1, spalte, lab)) return true;
        if (suchen(zeile, spalte + 1, lab)) return true;
        if (suchen(zeile, spalte - 1, lab)) return true;
        lab[zeile][spalte] = ' ';
        return false;
    }

    /**
     * Suche alle möglichen Wege
     *
     * @param zeile  aktuelle Position
     * @param spalte aktuelle Position
     * @param lab    das aktuelle labyrinth
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
     *
     * @param path the file path
     * @throws IOException if the file doesn't exist
     */
    public static char[][] fromFile(Path path) throws IOException {
        return fromStrings(Files.readAllLines(path).toArray(new String[0]));
    }

    public static void main(String[] args) throws InterruptedException, IOException {

//        var map = 2;
//        char[][] labyrinth = fromStrings(maps[map]);
//        System.out.println("Ausgang gefunden: " + (suchen(5, 5, labyrinth) ? "ja" : "nein"));
//        printLabyrinth(labyrinth);
//        printLabyrinth(labyrinth);

//        labyrinth = fromStrings(maps[map]);
//        System.out.println("Anzahl Wege: " + suchenAlle(5, 5, labyrinth));


//        printLabyrinth(fromFile(Paths.get("res/c_labyrinth/l2.txt")));
//        System.out.println((suchenAlle(5,5,fromFile(Paths.get("res/c_labyrinth/l1.txt")))));
//        System.out.println(possibile_coins(5, 0));
//        System.out.println(myeuler(new ArrayList<>(Arrays.asList(200,100,50,20,10,5,2,1)), 200,200));

        String[] split = angabe3.split("\n");
        int[][] a = new int[split.length][];

        for (int i = 0; i < a.length; i++) {
            String[] curNbrs = split[i].split(" ");
            a[i] = new int[curNbrs.length];
            for (int j = 0; j < curNbrs.length; j++) {
                a[i][j] = Integer.parseInt(curNbrs[j]);
            }
        }

        System.out.printf("%s", getMax(0, 0, a));
    }

    private static int getMax(int zeile, int spalte, int[][] input) {
        if (zeile == input.length) {
            return 0;
        }
        int a = getMax(zeile + 1, spalte, input);
        int b = getMax(zeile + 1, spalte + 1, input);

        if (a > b) {
            return a + input[zeile][spalte];
        } else {
            return b + input[zeile][spalte];
        }
    }

    public static int[] geld = new int[]{200, 100, 50, 20, 10, 5, 2, 1};

    private static int possibile_coins(int remainingValue, int curIndex) {
        int[] coins = new int[]{200, 100, 50, 20, 10, 5, 2, 1};
        int counter = 0;
        for (int i = curIndex; i < coins.length; i++) {
            if (coins[i] == remainingValue) {
                counter++; //hier könnte man recursiv machen, und dann abbruchkriterium, aber das ineffizient, weil extra methodenaufruf
                continue;
            }
            if (coins[i] < remainingValue) {
                counter += possibile_coins(remainingValue - coins[i], i);
            }
        }
        return counter;
    }

    public static int eulerCoin2(int rest, int max) {
        if (rest == 0) {
            return 1;
        }
        int ct = 0;
        for (int i = 0; i < geld.length; i++) {
            int coin = geld[i];
            if (rest - coin >= 0 && coin <= max) {
                ct += eulerCoin2(rest - coin, coin);
            }
        }
        return ct;
    }

    public static int myeuler(ArrayList<Integer> curCoins, int target, int firstCoin) {
        if (target == 0) {
            return 1;
        }
        int ct = 0;
        for (Integer coin : curCoins) {
            if (coin <= target) {
                if (coin <= firstCoin) {
                    ct += myeuler(curCoins, target - coin, coin);
                }
            }
        }
        return ct;
    }


    static String angabe = """
            75
            95 64
            17 47 82
            18 35 87 10
            20 04 82 47 65
            19 01 23 75 03 34
            88 02 77 73 07 63 67
            99 65 04 28 06 16 70 92
            41 41 26 56 83 40 80 70 33
            41 48 72 33 47 32 37 16 94 29
            53 71 44 65 25 43 91 52 97 51 14
            70 11 33 28 77 73 17 78 39 68 17 57
            91 71 52 38 17 14 91 43 58 50 27 29 48
            63 66 04 68 89 53 67 30 73 16 69 87 40 31
            04 62 98 27 23 09 70 98 73 93 38 53 60 04 23
            """;

    static String angabe2 = """
            3
            7 4
            2 4 6
            8 5 9 3
            """;

    static String angabe3 = """
            2
            8 5
            """;
}
