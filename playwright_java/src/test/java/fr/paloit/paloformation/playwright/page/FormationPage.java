package fr.paloit.paloformation.playwright.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import fr.paloit.paloformation.playwright.outil.Site;

import java.util.Optional;

public class FormationPage {
    private final Page page;
    public final Locator boutonEnregister;
    public final Locator titre;
    public final Locator itemsFormation;
    public final Locator boutonAjouter;

    public FormationPage(Page page) {
        this.page = page;

        this.titre = page.locator("h2");
        this.itemsFormation = page.locator("a");
        this.boutonAjouter = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ajouter une formation"));

        this.boutonEnregister = page.locator("button", new Page.LocatorOptions().setHasText("Enregistrer"));
    }

    public void ouvrir() {
        page.navigate(Site.getUrlAccueil() + "/formations");
    }

    public void allerSurFormation(String nomFormation) {
        page.locator("a:has-text(\""+nomFormation+"\")").click();
    }
}
