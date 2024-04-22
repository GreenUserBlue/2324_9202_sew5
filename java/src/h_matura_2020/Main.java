package h_matura_2020;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Testet die Project-Klasse indem sie die Datei "resources/NeueWebsiteHTL3r.project" in das XML-File
 * "resources/NeueWebsiteHTL3r.xml" konvertiert.
 *
 * Hier muss nichts geändert oder hinzugefügt werden.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        new Project(Files.readAllLines(Paths.get("res/h_matura_2020/NeueWebsiteHTL3r.project"), StandardCharsets.UTF_8))
                .saveToXMLFile("res/h_matura_2020/NeueWebsiteHTL3r.xml");

        System.out.println("Datei erfolgreich geschrieben");
    }
}
