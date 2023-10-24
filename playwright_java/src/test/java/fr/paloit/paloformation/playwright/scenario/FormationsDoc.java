package fr.paloit.paloformation.playwright.scenario;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import fr.paloit.paloformation.playwright.outil.DocExtension;
import fr.paloit.paloformation.playwright.outil.PlaywrightExtension;
import fr.paloit.paloformation.playwright.page.AjoutFormationPage;
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
    AjoutFormationPage ajoutFormationPage;
    DetailFormationPage detailPage;

    @BeforeEach
    public void debut(TestInfo info) {
        playwright.setContext(new Browser.NewContextOptions()
                .setViewportSize(700, 400));

        formationPage.ouvrir();
    }

    @Test
    void creation_d_une_formation(TestInfo info) {

        doc.writeln("= Création et suppression d'une formation\n");

        describeStep("Page d'accueil");

        String titreFormation = "Tutorial";

        formationPage.boutonAjouter.click();

        ajoutFormationPage.remplirTitre(titreFormation);
        describeStep("Ajout d'une formation `" + titreFormation + "`");
        ajoutFormationPage.boutonEnregister.click();

        describeStep("Liste des formations après l'ajout");

        formationPage.allerSurFormation(titreFormation);
        describeStep("Affchage du détail de la formation");

        detailPage.supprimerFormation();

        describeStep("Liste des formations après suppression");

    }

    private void describeStep(String description) {
        doc.describeStep(description, playwright.page());
    }

}