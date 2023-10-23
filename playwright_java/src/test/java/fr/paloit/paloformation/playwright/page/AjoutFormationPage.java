package fr.paloit.paloformation.playwright.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AjoutFormationPage {
    private final Page page;
    public final Locator boutonEnregister;
    public final Locator champFormation;

    public AjoutFormationPage(Page page) {
        this.page = page;
        this.champFormation = page.locator("#intitule");
        this.boutonEnregister = page.locator("button", new Page.LocatorOptions().setHasText("Enregistrer"));
    }

    public void remplirTitre(String titreFormation) {
        this.champFormation.fill(titreFormation);
    }

    public void ajouterFormation(String titreFormation) {
        remplirTitre(titreFormation);
        boutonEnregister.click();
    }

}
