package fr.paloit.paloformation.playwright.scenario;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import fr.paloit.paloformation.playwright.outil.DocExtension;
import fr.paloit.paloformation.playwright.outil.PlaywrightExtension;
import fr.paloit.paloformation.playwright.page.MenuPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Optional;

public class EcranDoc {
    @RegisterExtension
    public static PlaywrightExtension playwright = new PlaywrightExtension();

    @RegisterExtension
    public static DocExtension doc = new DocExtension();

    MenuPage menuPage;

    @BeforeEach
    public void debut(TestInfo info) {
        playwright.setContext(new Browser.NewContextOptions()
                .setViewportSize(800, 400));

        String host = Optional.ofNullable(System.getenv("APP_HOST")).orElse("localhost:8080");
        playwright.page().navigate("http://" + host);
    }

    @Test
    void creation_d_une_session(TestInfo info) throws InterruptedException {
        doc.writeln("= Ecrans de l'application\n");

        documenterPage(menuPage.planning);
        documenterPage(menuPage.clients);
        documenterPage(menuPage.formateurs);
        documenterPage(menuPage.parametres);
    }

    public void documenterPage(Locator elementCliquable) {
        elementCliquable.click();
        doc.describeStep("== " + elementCliquable.textContent().trim(), playwright.page());
    }
}
