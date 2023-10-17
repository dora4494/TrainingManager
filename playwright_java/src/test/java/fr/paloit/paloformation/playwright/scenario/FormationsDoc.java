package fr.paloit.paloformation.playwright.scenario;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.options.AriaRole;
import fr.paloit.paloformation.playwright.outil.PlaywrightExtension;
import fr.paloit.paloformation.playwright.page.DetailFormationPage;
import fr.paloit.paloformation.playwright.page.FormationPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormationsDoc {

    @RegisterExtension
    public static PlaywrightExtension playwright = new PlaywrightExtension();
    FormationPage formationPage;
    String testName;
    @BeforeEach
    public void debut(TestInfo info) {
        testName = info.getTestMethod().map(m ->m.getDeclaringClass().getSimpleName() + "." + m.getName()).get();
        playwright.setContext(new Browser.NewContextOptions()
                .setViewportSize(700, 400));
        formationPage = new FormationPage(playwright.page());
        formationPage.ouvrir();
    }

    int index = 0;
    StringBuffer buffer = new StringBuffer();

    final Path docs = Paths.get("build", "docs");

    @Test
    void creation_d_une_formation(TestInfo info) {
        buffer.append("ifndef::ROOT_PATH[:ROOT_PATH: .]\n");
        buffer.append("= Création et suppression d'une formation\n\n");
        describeStep("Page d'accueil", docs);

        String nom_formation = "Tutorial";

        playwright.page().getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ajouter une formation")).click();
        playwright.page().locator("#intitule").fill(nom_formation);
        describeStep("Ajout d'une formation `" + nom_formation +"`", docs);


        playwright.page().locator("button", new Page.LocatorOptions().setHasText("Enregistrer")).click();

        describeStep("Liste des formations après l'ajout", docs);

        formationPage.allerSurFormation(nom_formation);
        describeStep("Affchage du détail de la formation", docs);

        DetailFormationPage detailPage = new DetailFormationPage(playwright.page());
        detailPage.supprimerFormation();

        describeStep("Liste des formations après suppression", docs);

        try (FileWriter fileWriter = new FileWriter(docs.resolve(testName + ".adoc").toString())) {
            buffer.append("\n" + style());
            fileWriter.write(buffer.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String style() {
        return "\n" + String.join("\n",
                "++++",
                "<style>",
                "  img {",
                "    border: grey solid 1px;",
                "    box-shadow: 5px 5px 5px grey;",
                "    margin: 1em;",
                "    margin-left: 5em;",
                "  }",
                "</style>",
                "++++"
        );
    }

    private void describeStep(String description, Path docs) {
        index++;
        final String imagePath = testName + "_" + index + ".jpg";
        playwright.page().screenshot(new Page.ScreenshotOptions().setPath(docs.resolve(imagePath)));
        buffer.append(description);
        buffer.append("\n\n");
        buffer.append("image::{ROOT_PATH}/"+imagePath+"[]\n");
    }

}