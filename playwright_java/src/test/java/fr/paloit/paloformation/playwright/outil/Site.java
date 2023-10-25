package fr.paloit.paloformation.playwright.outil;

import java.util.Optional;

public class Site {

    public static String getUrlAccueil() {
        String host = Optional.ofNullable(System.getenv("APP_HOST")).orElse("localhost:8080");
        return "http://" + host;
    }
}
