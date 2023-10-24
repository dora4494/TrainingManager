package fr.paloit.paloformation.playwright.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.Optional;

public class MenuPage {
    private final Page page;
    public final Locator accueil;
    public final Locator planning;
    public final Locator clients;
    public final Locator formateurs;
    public final Locator parametres;

    public MenuPage(Page page) {
        this.page = page;
        this.accueil = page.locator("a", new Page.LocatorOptions().setHasText("Accueil"));
        this.planning = page.locator("a", new Page.LocatorOptions().setHasText("Planning"));
        this.clients = page.locator("a", new Page.LocatorOptions().setHasText("Clients"));
        this.formateurs = page.locator("a", new Page.LocatorOptions().setHasText("Formateurs"));
        this.parametres = page.locator("a", new Page.LocatorOptions().setHasText("Paramètres"));

    }


}
