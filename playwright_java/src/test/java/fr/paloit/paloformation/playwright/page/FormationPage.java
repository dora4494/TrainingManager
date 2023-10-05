package fr.paloit.paloformation.playwright.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class FormationPage {
    private final Page page;
    public final Locator boutonEnregister;
    public final Locator titre;
    public final Locator items_formation;

    public FormationPage(Page page) {
        this.page = page;
        this.boutonEnregister = page.locator("button", new Page.LocatorOptions().setHasText("Enregistrer"));
        this.titre = page.locator("h2");
        this.items_formation = page.locator("a");
    }

    public void ouvrir() {
        final String appHost = System.getenv("APP_HOST");
        //const host = process.env.APP_HOST as string ?? "localhost:8080" ;
        String host = "localhost:8080";
        page.navigate("http://" + host + "/formations");
    }

    public void ajouter_formation(String titre) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ajouter une formation")).click();
        page.getByPlaceholder("Intitule").fill(titre);
        boutonEnregister.click();
    }
}
