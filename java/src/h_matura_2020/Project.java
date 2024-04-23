package h_matura_2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Die Klasse kapselt ein gesamtes Projekt und ermöglicht das Projekt in eine XML-Darstellung zu bringen.
 *
 * @author Zwickelstorfer Felix
 */
public class Project {

    /**
     * Projektname
     */
    private String name = "no name";

    /**
     * die Map speichert zu jedem State (In Progress, Done, ...) eine Liste mit den dazugehörigen Storys.
     */
    private Map<String, TreeSet<Story>> states = new LinkedHashMap<>();

    /**
     * Der Konstruktor baut aus den Projekt-Zeilen das Projekt.
     * <p>
     * Beispiel für die Projekt-Zeilen:
     * # Neue Webseite für HTL3R
     * <p>
     * ## Content-Keyword-Strategie planen
     * state: In Progress
     * ready: 10%
     * length: 5h
     * owner: John Doe
     * description: Wie soll Google die Webseite finden
     * <p>
     * ## Content-Texte erstellen
     * state: Done
     * ready: 100%
     * length: 15h
     * owner: noch unbekannt
     * description: Berichte über die HTL erstellen bzw. sammeln (Veranstaltungen, Preise, ...)
     * <p>
     * ## Content-Bilder erstellen
     * state: In Progress
     * ready: 40%
     * length: 8h
     * owner: Mimi Grafitti
     * description: Bilder für die Hauptseite erstellen, die einen Eindruck der HTL geben
     *
     * @param project die Projektzeilen
     */
    public Project(List<String> project) {
        List<List<String>> storys = splitToStorys(project);

        // Die erste Story ist in Wirklichkeit der Projekt-Name
        name = storys.remove(0).get(0).substring(1).trim();

        storys.forEach(story -> addStory(new Story(story)));
    }

    /**
     * Erzeugt den Projekt-Zeilen eine List, in der alle Storys (und als erstes auch die Projekbezeichnug)
     * in einzelnen Listen stehen.
     *
     * @param project die gesamten Projektzeilen
     * @return das gesplittete Projekt
     */
    private static List<List<String>> splitToStorys(List<String> project) {
        List<List<String>> storys = new ArrayList<>();
        project.removeIf(String::isBlank);
        int curStart = 0;
        for (int i = 0; i < project.size(); i++) {
            if (project.get(i).startsWith("##")) {
                storys.add(new ArrayList<>(project.subList(curStart, i)));
                curStart = i;
            } else if (project.get(i).startsWith("#")) {
                curStart = i;
            }
        }
        storys.add(new ArrayList<>(project.subList(curStart, project.size())));
        return storys;
    }

    /**
     * Fügt eine Story in die states-Map ein.
     *
     * @param story die neue Story
     */
    private void addStory(Story story) {
        states.putIfAbsent(story.getStateName(), new TreeSet<>());
        states.get(story.getStateName()).add(story);
    }


    /**
     * Summiert für alle Storys des States "state" die progressReadyHours.
     *
     * @param state der gewünschte State
     * @return die Summe
     */
    private double sumProgressReadyHours(String state) {
        return states.get(state).stream().mapToDouble(Story::getProgressReadyHours).sum();
    }

    /**
     * Summiert für alle Storys des States "state" die progressLengthHours.
     *
     * @param state der gewünschte State
     * @return die Summe
     */
    private int sumProgressLengthHours(String state) {
        return states.get(state).stream().mapToInt(Story::getProgressLengthHours).sum();
    }

    /**
     * Summiert für alle Storys des gesamten Projekts die progressReadyHours
     *
     * @return die Summe
     */
    private double sumProgressReadyHours() {
        return states.values().stream().flatMapToDouble(it -> it.stream().mapToDouble(Story::getProgressReadyHours)).sum();
    }

    /**
     * Summiert für alle Storys des gesamten Projekts die progressLengthHours
     *
     * @return die Summe
     */
    private int sumProgressLengthHours() {
        return states.values().stream().mapToInt(it -> it.stream().mapToInt(Story::getProgressLengthHours).sum()).sum();

    }

    /**
     * Erzeugt für das Projekt eine XML-Darstellung
     *
     * @return die XML-Darstellung
     */
    public List<String> toXML() {
        String indent = "    ";
        List<String> xml = new ArrayList<>();
        xml.add("<kanbanBoard name=\"" + name + "\">");
        xml.add(indent.repeat(1) + "<total>");
        xml.add(indent.repeat(2) + "<ready percent=\"" + (int)(sumProgressReadyHours()/sumProgressLengthHours()*100) + "\"/>");
        xml.add(indent.repeat(2) + "<length hours=\"" + sumProgressLengthHours() + "\"/>");
        xml.add(indent.repeat(1) + "</total>");
        xml.add(indent.repeat(1) + "");
        for (Map.Entry<String, TreeSet<Story>> e : states.entrySet()) {
            xml.add(indent.repeat(1) + "<state name=\"" + e.getKey() + "\">");
            xml.add(indent.repeat(2) + "<total>");
            xml.add(indent.repeat(3) + "<ready percent=\"" +(int)(sumProgressReadyHours(e.getKey())/sumProgressLengthHours(e.getKey())*100) + "\"/>");
            xml.add(indent.repeat(3) + "<length hours=\"" + sumProgressLengthHours(e.getKey()) + "\"/>");
            xml.add(indent.repeat(2) + "</total>");
            xml.add(indent.repeat(2) + "");
            for (Story s : e.getValue()) {
                xml.add(indent.repeat(2) + "<story name=\"" + s.getStoryName() + "\">");
                xml.add(indent.repeat(3) + "<state name=\"" + s.getStateName() + "\"/>");
                xml.add(indent.repeat(3) + "<progress>");
                xml.add(indent.repeat(4) + "<ready percent=\"" + (int) s.getProgressReadyPercent() + "\"/>");
                xml.add(indent.repeat(4) + "<length hours=\"" + s.getProgressLengthHours() + "\"/>");
                xml.add(indent.repeat(3) + "</progress>");
                xml.add(indent.repeat(3) + "<description>");
                xml.add(indent.repeat(4) + s.getDescription());
                xml.add(indent.repeat(3) + "</description>");
                xml.add(indent.repeat(2) + "</story>");

            }
            xml.add(indent.repeat(1) + "</state>");
        }
            xml.add(indent.repeat(0) + "</kanbanBoard>");
//            xml.add(indent.repeat(4) + "");
        return xml;
    }

    /**
     * Speichert das Projekt als XML-File (UTF-8).
     *
     * @param fileName der Name der XML-Datei
     */
    public void saveToXMLFile(String fileName) throws IOException {
        Files.write(Path.of(fileName), toXML());
    }

    @Override
    public String toString() {
        return "Project: " + name + "\n-------------------------------------------------------\n\n" +
                states.entrySet().stream()
                        .map(entry -> entry.getValue())
                        .map(storys -> storys.stream().map(Story::toString).collect(Collectors.joining("\n")))
                        .collect(Collectors.joining("-------------------------------------------------------\n"));
    }

    /**
     * Dient nur zum einfachen Testen der Klasseanhand
     * <p>
     * Demo-Projekt zum Debuggen:
     * # Neue Webseite für HTL3R
     * <p>
     * ## Content-Keyword-Strategie planen
     * state: In Progress
     * ready: 10%
     * length: 5h
     * owner: John Doe
     * description: Wie soll Google die Webseite finden
     * <p>
     * ## Content-Texte erstellen
     * state: ToDo
     * ready: 0%
     * length: 15h
     * owner: noch unbekannt
     * description: Berichte über die HTL erstellen bzw. sammeln (Veranstaltungen, Preise, ...)
     * <p>
     * ## Content-Bilder erstellen
     * state: In Progress
     * ready: 40%
     * length: 8h
     * owner: Mimi Grafitti
     * description: Bilder für die Hauptseite erstellen, die einen Eindruck der HTL geben
     *
     * @param args unused
     */
    public static void main(String[] args) {
        String project[] = ("# Neue Webseite für HTL3R\n" +
                "\n" +
                "## Content-Keyword-Strategie planen\n" +
                "state: In Progress\n" +
                "ready: 10%\n" +
                "length: 5h\n" +
                "owner: John Doe\n" +
                "description: Wie soll Google die Webseite finden\n" +
                "\n" +
                "## Content-Texte erstellen\n" +
                "state: ToDo\n" +
                "ready: 0%\n" +
                "length: 15h\n" +
                "owner: noch unbekannt\n" +
                "description: Berichte über die HTL erstellen bzw. sammeln (Veranstaltungen, Preise, ...)\n" +
                "\n" +
                "## Content-Bilder erstellen\n" +
                "state: In Progress\n" +
                "ready: 40%\n" +
                "length: 8h\n" +
                "owner: Mimi Grafitti\n" +
                "description: Bilder für die Hauptseite erstellen, die einen Eindruck der HTL geben").split("\n");

        System.out.println("String-Darstellung:");
        System.out.println(new Project(new ArrayList<>(List.of(project))));

        System.out.println("XML-Darstellung:");
        new Project(new ArrayList<>(List.of(project))).toXML().forEach(System.out::println);
    }
}
