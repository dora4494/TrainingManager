package fr.paloit.paloformation.playwright.scenario;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import fr.paloit.paloformation.playwright.outil.DocExtension;
import fr.paloit.paloformation.playwright.outil.PlaywrightExtension;
import fr.paloit.paloformation.playwright.page.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.file.Path;

public class SessionDoc {

    @RegisterExtension
    public static PlaywrightExtension playwright = new PlaywrightExtension();

    @RegisterExtension
    public static DocExtension doc = new DocExtension();

    SessionPage sessionPage;
    AjoutSessionPage ajoutSessionPage;


    @BeforeEach
    public void debut(TestInfo info) {
        playwright.setContext(new Browser.NewContextOptions()
                .setViewportSize(700, 400));

        sessionPage.ouvrir();
    }

    int index = 0;

    @Test
    void creation_d_une_session(TestInfo info) throws InterruptedException {

        doc.writeln("ifndef::ROOT_PATH[:ROOT_PATH: .]");
        doc.writeln("= Création et suppression d'une formation\n");
        describeStep("Page d'accueil");

        final String titreFormation = "TDD";

        sessionPage.boutonAjouter.click();
        ajoutSessionPage.champFormation.selectOption(titreFormation);
        ajoutSessionPage.champClient.fill("Palo");

        ajoutSessionPage.champDates.fill("2023-10-10");

        ajoutSessionPage.champFormateur.selectOption("Petit");
        describeStep("Ajout d'une session pour la formation `" + titreFormation + "`");
        ajoutSessionPage.boutonEnregister.click();

        describeStep("Liste des sessions après l'ajout");
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