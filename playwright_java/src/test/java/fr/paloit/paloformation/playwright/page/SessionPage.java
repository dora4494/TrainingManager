package fr.paloit.paloformation.playwright.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;
import java.util.Optional;

public class SessionPage {
    private final Page page;
    public final Locator titre;
    public final Locator boutonAjouter;
    private final Locator zoneParticipants;
    private final Locator boutonEnvoyerEmargement;


    public SessionPage(Page page) {
        this.page = page;

        this.titre = page.locator("h2");
        this.boutonAjouter = page.locator("#bouton-sessions-submit");
        this.boutonEnvoyerEmargement = page.locator("#bouton-detail-session");  // Id probablement à revoir

        this.zoneParticipants = page.locator("#container-lst-participants");
    }

    public void ouvrir() {
        String host = Optional.ofNullable(System.getenv("APP_HOST")).orElse("localhost:8080");
        page.navigate("http://" + host + "/sessions");
    }

    public void selectionnerParticipant(String nom) {
        final List<Locator> all = this.zoneParticipants.locator("#table-participants tr").all();
        for (Locator locator : all) {
            if (locator.locator("td").first().textContent().equals(nom)) {
                locator.locator("td").last().click();
            }
        }
    }

    public void envoyerEmargement() {
        boutonEnvoyerEmargement.click();
    }
}
