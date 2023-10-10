package fr.paloit.paloformation.playwright.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class DetailFormationPage {
    private final Page page;
    public final Locator boutonModifier;
    public final Locator boutonSupprimer;
    public final Locator titre;

    public DetailFormationPage(Page page) {
        this.page = page;
        this.boutonModifier = page.locator("button", new Page.LocatorOptions().setHasText("Modifier formation"));
        this.boutonSupprimer = page.locator("button", new Page.LocatorOptions().setHasText("Supprimer Formation"));
        this.titre = page.locator(".details-container > div:nth-child(1)");
    }

    public String getTitre() {
        return titre.textContent();
    }

    public void supprimerFormation() {
        boutonSupprimer.click();
    }
}
