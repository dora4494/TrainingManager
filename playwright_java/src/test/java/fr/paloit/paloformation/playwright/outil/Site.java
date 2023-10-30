package fr.paloit.paloformation.playwright.outil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Site {

    public static String getUrlAccueil() {
        String host = Optional.ofNullable(System.getenv("APP_HOST")).orElse("localhost:8080");
        return "http://" + host;
    }

    public static void viderLogSpring() {
        try {
            PrintWriter pw = new PrintWriter("../log/spring.log");
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String extraireLogSpring(String logAExtraire) {
        try (Scanner sc = new Scanner(Paths.get("../log/spring.log").toFile())) {
            final String logsDocuSign = sc.findAll(".*" + logAExtraire + "\s*:(.*)")
                    .map(m -> m.group(1))
                    .collect(Collectors.joining("\n"));
            return logsDocuSign;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
