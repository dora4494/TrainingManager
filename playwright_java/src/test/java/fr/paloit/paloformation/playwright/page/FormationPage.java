package fr.paloit.paloformation.playwright.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.Optional;

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
        String host = Optional.ofNullable(System.getenv("APP_HOST")).orElse("localhost:8080");
        page.navigate("http://" + host + "/formations");
    }

    public void ajouterFormation(String titre) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ajouter une formation")).click();
        page.locator("#intitule").fill(titre);
        // page.getByPlaceholder("ex: TDD, Architecture hexagonale... ").fill(titre);
        boutonEnregister.click();
    }

    public void allerSurFormation(String nomFormation) {
        page.locator("a:has-text(\""+nomFormation+"\")").click();
    }
}
