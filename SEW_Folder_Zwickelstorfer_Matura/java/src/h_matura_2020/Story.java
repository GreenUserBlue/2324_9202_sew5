package h_matura_2020;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse kapselt eine eizelne Story und ermöglicht die Story in eine XML-Darstellung zu bringen.
 *
 * @author Zwickelstorfer Felix
 */
public class Story implements Comparable<Story> {

    /**
     * Der Name der Story.
     */
    private String storyName = "no name";

    /**
     * Der Name des States (Bsp.: In Progress).
     */
    private String stateName = "no name";

    /**
     * Wie viele Prozent der Stunden der Story schon erledigt sind.
     */
    private int progressReadyPercent;

    /**
     * Die Anzahl der Stunden die die Story (geschätzt) braucht.
     */
    private int progressLengthHours;

    /**
     * Name des Story-Owners
     */
    private String ownerName = "no name";

    /**
     * Beschreibung der Story
     */
    private String description = "no description";

    /**
     * Der Konstruktor baut aus den Story-Zeilen die Story.
     * <p>
     * Beispiel für die Story-Zeilen:
     * ## Content-Texte erstellen
     * state: In Progress
     * ready: 20%
     * length: 15h
     * owner: noch unbekannt
     * description: Berichte über die HTL erstellen bzw. sammeln (Veranstaltungen, Preise, ...)
     *
     * @param story die Story-Zeilen
     */
    public Story(List<String> story) {
        story.forEach(a -> {
            String[] s = a.trim().split(":");
            if (s.length == 1 && a.startsWith("##")) {
                storyName = a.substring(3).trim();
            } else if (s.length == 2) {
                switch (s[0]) {
                    case "state" -> stateName = s[1].trim();
                    case "length" -> progressLengthHours = Integer.parseInt(s[1].replaceAll("\\D", ""));
                    case "ready" -> progressReadyPercent = Integer.parseInt(s[1].replaceAll("\\D", ""));
                    case "owner" -> ownerName = s[1].trim();
                    case "description" -> description = s[1].trim();
                    default -> throw new IllegalArgumentException("unknown key: " + s[0]);
                }
            } else if (!a.isBlank()) {
                throw new IllegalArgumentException("Wrong line format: " + a);
            }
        });
    }


    @Override
    public int compareTo(Story other) {
        int stateComp = other.stateName.compareTo(this.stateName);
        return stateComp == 0 ? other.storyName.compareTo(this.storyName) : stateComp;
    }


    @Override
    public String toString() {
        return "storyName: " + storyName + "\n" +
                "stateName: " + stateName + '\n' +
                "progressReadyPercent:" + progressReadyPercent + '\n' +
                "progressLengthHours: " + progressLengthHours + '\n' +
                "ownerName: " + ownerName + '\n' +
                "description: " + description + "\n";
    }

    /**
     * Erzeugt eine XML-Darstellung der Story
     *
     * @return die XML-Darstellung
     */
    public List<String> toXML() {
        List<String> xml = new ArrayList<>();
        xml.add("<story name=\"" + storyName + "\">");
        xml.add("   <state name=\"" + stateName + "\"/>");
        xml.add("   <story name=\"" + storyName + "\">");
        xml.add("   <progress>");
        xml.add("       <ready percent=\"" + progressReadyPercent + "\"/>");
        xml.add("       <length hours=\"" + progressLengthHours + "\"/>");
        xml.add("   </progress>");
        xml.add("   <owner name=\"" + ownerName + "\"/>");
        xml.add("   <description>");
        xml.add("       " + description);
        xml.add("   </description>");
        xml.add("</story>");
        return xml;
    }

    public String getStoryName() {
        return storyName;
    }

    public String getStateName() {
        return stateName;
    }

    public double getProgressReadyPercent() {
        return progressReadyPercent;
    }

    public int getProgressLengthHours() {
        return progressLengthHours;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Ermittelt wie viele Stunden der Story schon erledigt sind.
     */
    public double getProgressReadyHours() {
        return progressReadyPercent * progressLengthHours / 100.0;
    }

    /**
     * Dient nur zum einfachen Testen der Klasse
     * <p>
     * Demo-Story zum Debuggen:
     * ## Content-Texte erstellen
     * state: ToDo
     * ready: 20%
     * length: 15h
     * owner: noch unbekannt
     * description: Berichte über die HTL erstellen bzw. sammeln (Veranstaltungen, Preise, ...)
     *
     * @param args unused
     */
    public static void main(String[] args) {
        String story[] = ("\n" +
                "## Content-Texte erstellen\n" +
                "state: ToDo\n" +
                "ready: 20%\n" +
                "length: 15h\n" +
                "owner: noch unbekannt\n" +
                "description: Berichte über die HTL erstellen bzw. sammeln (Veranstaltungen, Preise, ...)").split("\n");

        System.out.println("String-Darstellung:");
        System.out.println(new Story(new ArrayList<>(List.of(story))));

        System.out.println("getProgressReadyHours: " + new Story(new ArrayList<>(List.of(story))).getProgressReadyHours());

        System.out.println("\nXML-Darstellung:");
        new Story(new ArrayList<>(List.of(story))).toXML().forEach(System.out::println);
    }
}