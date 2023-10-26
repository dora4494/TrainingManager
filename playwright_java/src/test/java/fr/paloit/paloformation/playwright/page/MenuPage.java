package fr.paloit.paloformation.playwright.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MenuPage {
    private final Page page;
    public final Locator accueil;
    public final Locator planning;
    public final Locator formations;
    public final Locator profils;
    public final Locator parametres;

    public MenuPage(Page page) {
        this.page = page;
        this.accueil = page.locator("a", new Page.LocatorOptions().setHasText("Accueil"));
        this.planning = page.locator("a", new Page.LocatorOptions().setHasText("Planning"));
        this.formations = page.locator("a", new Page.LocatorOptions().setHasText("Formations"));
        this.profils = page.locator("a", new Page.LocatorOptions().setHasText("Profils"));
        this.parametres = page.locator("a", new Page.LocatorOptions().setHasText("Paramètres"));

    }


}
