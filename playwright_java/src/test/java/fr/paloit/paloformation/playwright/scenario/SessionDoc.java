package fr.paloit.paloformation.playwright.scenario;

import com.microsoft.playwright.Browser;
import fr.paloit.paloformation.playwright.outil.DocExtension;
import fr.paloit.paloformation.playwright.outil.PlaywrightExtension;
import fr.paloit.paloformation.playwright.page.AjoutSessionPage;
import fr.paloit.paloformation.playwright.page.MenuPage;
import fr.paloit.paloformation.playwright.page.SessionPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.RegisterExtension;

public class SessionDoc {

    @RegisterExtension
    public static PlaywrightExtension playwright = new PlaywrightExtension();

    @RegisterExtension
    public static DocExtension doc = new DocExtension();

    MenuPage menuPage;
    SessionPage sessionPage;
    AjoutSessionPage ajoutSessionPage;


    @BeforeEach
    public void debut(TestInfo info) {
        playwright.setContext(new Browser.NewContextOptions()
                .setViewportSize(800, 400));

        sessionPage.ouvrir();
    }

    @Test
    void creation_d_une_session(TestInfo info) {
        doc.writeln("= Création d'une Session\n");

        menuPage.planning.click();
        describeStep("Planning");
        menuPage.clients.click();
        describeStep("clients");
        menuPage.formateurs.click();
        describeStep("formateurs");
        menuPage.parametres.click();
        describeStep("parametres");

        describeStep("Depuis la pages des sessions");

        final String titreFormation = "TDD";

        doc.cliquerSur(sessionPage.boutonAjouter);
        ajoutSessionPage.champFormation.selectOption(titreFormation);
        ajoutSessionPage.champClient.fill("Palo");

        ajoutSessionPage.champDates.fill("2023-10-10");

        ajoutSessionPage.champFormateur.selectOption("Petit");
        describeStep("Renseigner les informations concernant la session");
        doc.cliquerSur(ajoutSessionPage.boutonEnregister);

        describeStep("Après l'enregistrement, on revient à l'écran des sessions qui affiche celle qui vient d'être saisie.");
    }

    private void describeStep(String description) {
        doc.describeStep(description, playwright.page());
    }

}