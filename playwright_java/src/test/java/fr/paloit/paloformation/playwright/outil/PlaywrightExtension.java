package fr.paloit.paloformation.playwright.outil;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.extension.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PlaywrightExtension implements AfterEachCallback, AfterAllCallback, BeforeEachCallback, BeforeAllCallback {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    // Permet d'activer les traces utilisées par le TraceViewer.
    static boolean TRACE_ACTIVE = true;
    // En mode headless, le navigateur ne s'ouvre pas pendant l'execution
    static boolean HEADLESS = true;

    @Override
    public void beforeAll(ExtensionContext context) {
        playwright = Playwright.create();
        // Ouverture du navigateur pendant l'execution des tests avec une temporisation pour voir ce qui se passe.
        if (HEADLESS) {
            browser = playwright.chromium().launch();
        } else {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        playwright.close();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        setContext();
    }

    public void setContext() {
        setContext(new Browser.NewContextOptions());
    }

    public void setContext(Browser.NewContextOptions options) {
        if (context != null) {
            context.close();
        }
        context = browser.newContext(options);
        if (TRACE_ACTIVE) {
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
        }
        page = context.newPage();
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        if (TRACE_ACTIVE) {
            final Path outputPath = Paths.get("build", "trace", "trace.zip");
            context.tracing().stop(
                    new Tracing.StopOptions().setPath(outputPath));
        }

        context.close();
    }

    public Page page() {
        return page;
    }
}
