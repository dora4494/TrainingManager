package fr.paloit.paloformation.playwright.scenario;

import com.microsoft.playwright.Browser;
import fr.paloit.paloformation.playwright.outil.DocExtension;
import fr.paloit.paloformation.playwright.outil.PlaywrightExtension;
import fr.paloit.paloformation.playwright.outil.Site;
import fr.paloit.paloformation.playwright.page.AjoutSessionPage;
import fr.paloit.paloformation.playwright.page.MenuPage;
import fr.paloit.paloformation.playwright.page.SessionPage;
import fr.paloit.paloformation.playwright.page.SessionsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;
import java.util.stream.Collectors;

public class SessionDoc {

    @RegisterExtension
    public static PlaywrightExtension playwright = new PlaywrightExtension();

    @RegisterExtension
    public static DocExtension doc = new DocExtension();

    MenuPage menuPage;
    SessionPage sessionPage;
    SessionsPage sessionsPage;
    AjoutSessionPage ajoutSessionPage;


    @BeforeEach
    public void debut(TestInfo info) {
        playwright.setContext(new Browser.NewContextOptions()
                .setViewportSize(900, 600));

        sessionPage.ouvrir();
    }

    @Test
    void creation_d_une_session(TestInfo info) {
        doc.writeln("= Création d'une Session\n");

        describeStep("Depuis la pages des sessions");

        final String titreFormation = "TDD";

        doc.cliquerSur(sessionPage.boutonAjouter);
        ajoutSessionPage.champFormation.selectOption(titreFormation);
        ajoutSessionPage.champClient.fill("Palo");

        ajoutSessionPage.champDates.fill("2023-10-10");

        final List<String> formateurs = ajoutSessionPage.champFormateur.locator("option").allTextContents();
        ajoutSessionPage.champFormateur.selectOption(formateurs.get(1));

        System.out.println("SessionDoc.creation_d_une_session " + ajoutSessionPage.champParticipants.textContent());
        final List<String> participants = ajoutSessionPage.champParticipants.locator("option").allTextContents();
        ajoutSessionPage.champParticipants.selectOption(participants.subList(1, 4).toArray(new String[0]));

        describeStep("Renseigner les informations concernant la session");
        doc.cliquerSur(ajoutSessionPage.boutonEnregister);

        describeStep("Après l'enregistrement, on revient à l'écran des sessions qui affiche celle qui vient d'être saisie.");

        sessionsPage.ouvrirDerniereSession(titreFormation);
        describeStep("En cliquant sur son nom `" + titreFormation + ", on voit le détail de la session.");

        final List<String> participantsSelectionnes = participants.subList(1,3);
        for (String participant : participantsSelectionnes) {
            sessionPage.selectionnerParticipant(participant);
        }

        describeStep("On peut alors sélectionner des participants et leurs envoyer la feuille d'émargement");
        Site.viderLogSpring();
        sessionPage.envoyerEmargement();

        doc.writeln("Ici, après avoir sélectionné "
                        + participantsSelectionnes.stream().map(nom -> "`" + nom + "`").collect(Collectors.joining(", "))
                        + " et envoyé la demande d'émargement, on peut lire dans les logs:",
                "");

        // Pour lire dans les logs
        final String logsDocuSign = Site.extraireLogSpring("DocuSignService");
        doc.writeln("----",
                logsDocuSign,
                "----");
    }

    private void describeStep(String description) {
        doc.describeStep(description, playwright.page());
    }

}