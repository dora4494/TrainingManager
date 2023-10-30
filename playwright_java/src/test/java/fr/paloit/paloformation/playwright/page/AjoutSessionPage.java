package fr.paloit.paloformation.playwright.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AjoutSessionPage {
    private final Page page;
    public final Locator boutonEnregister;
    public final Locator champFormation;
    public final Locator champClient;
    public final Locator champDates;
    public final Locator champFormateur;
    public final Locator champParticipants;

    public AjoutSessionPage(Page page) {
        this.page = page;
        this.champFormation = page.locator("#select-intitule-formation");
        this.champClient = page.locator("#aligned-client");
        this.champDates = page.locator("#aligned-dates");
        this.champFormateur = page.locator("#aligned-formateur");
        this.champParticipants = page.locator("#aligned-participant");

        this.boutonEnregister = page.locator("button", new Page.LocatorOptions().setHasText("Enregistrer"));
    }

    public void ajouterSession(String titreFormation) {
        boutonEnregister.click();
    }

}
