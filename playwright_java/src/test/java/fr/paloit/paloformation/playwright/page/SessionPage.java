package fr.paloit.paloformation.playwright.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.Optional;

public class SessionPage {
    private final Page page;
    public final Locator titre;
    public final Locator boutonAjouter;

    public SessionPage(Page page) {
        this.page = page;

        this.titre = page.locator("h2");
        this.boutonAjouter = page.locator("#bouton-sessions-submit");
    }

    public void ouvrir() {
        String host = Optional.ofNullable(System.getenv("APP_HOST")).orElse("localhost:8080");
        page.navigate("http://" + host + "/sessions");
    }

}
