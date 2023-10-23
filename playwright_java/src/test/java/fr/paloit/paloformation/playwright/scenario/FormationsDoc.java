package fr.paloit.paloformation.playwright.scenario;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import fr.paloit.paloformation.playwright.outil.DocExtension;
import fr.paloit.paloformation.playwright.outil.PlaywrightExtension;
import fr.paloit.paloformation.playwright.page.DetailFormationPage;
import fr.paloit.paloformation.playwright.page.FormationPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.file.Path;

public class FormationsDoc {

    @RegisterExtension
    public static PlaywrightExtension playwright = new PlaywrightExtension();

    @RegisterExtension
    public static DocExtension doc = new DocExtension();

    FormationPage formationPage;

    @BeforeEach
    public void debut(TestInfo info) {
        playwright.setContext(new Browser.NewContextOptions()
                .setViewportSize(700, 400));
        formationPage = new FormationPage(playwright.page());
        formationPage.ouvrir();
    }

    int index = 0;

    @Test
    void creation_d_une_formation(TestInfo info) {
        Path docs = doc.getPath();
        doc.writeln("ifndef::ROOT_PATH[:ROOT_PATH: .]");
        doc.writeln("= Création et suppression d'une formation\n");
        describeStep("Page d'accueil");

        String nom_formation = "Tutorial";

        playwright.page().getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ajouter une formation")).click();
        playwright.page().locator("#intitule").fill(nom_formation);
        describeStep("Ajout d'une formation `" + nom_formation + "`");

        playwright.page().locator("button", new Page.LocatorOptions().setHasText("Enregistrer")).click();

        describeStep("Liste des formations après l'ajout");

        formationPage.allerSurFormation(nom_formation);
        describeStep("Affchage du détail de la formation");

        DetailFormationPage detailPage = new DetailFormationPage(playwright.page());
        detailPage.supprimerFormation();

        describeStep("Liste des formations après suppression");

    }

    private void describeStep(String description) {
        index++;
        final String imageFilename = doc.getTestName() + "_" + index + ".jpg";
        final Path imagePath = doc.getPath().resolve(imageFilename);
        playwright.page().screenshot(new Page.ScreenshotOptions().setPath(imagePath));
        doc.writeln(description,
                "",
                "image::{ROOT_PATH}/" + imageFilename + "[]");
    }

}