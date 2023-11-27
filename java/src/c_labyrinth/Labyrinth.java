package c_labyrinth;

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
		for (int i = 0; i < lab.length; i++) {
			System.out.println();
			for (int j = 0; j < lab[i].length; j++) {
				System.out.print(lab[i][j]);
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
        // TODO Code fehlt noch
        // nur lab[zeile][spalte] betrachten
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        char[][] labyrinth = fromStrings(maps[0]);
        printLabyrinth(labyrinth);
        System.out.println("Ausgang gefunden: " + (suchen(5, 5, labyrinth) ? "ja" : "nein"));
        // TODO: System.out.println("Anzahl Wege: " + suchenAlle(5, 5, labyrinth));
    }
}
