package fr.paloit.paloformation.playwright.outil;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlaywrightExtension implements AfterEachCallback, AfterAllCallback, BeforeEachCallback, BeforeAllCallback, TestWatcher {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    private Object testInstance;

    // Permet d'activer les traces utilisées par le TraceViewer.
    static boolean TRACE_ACTIVE = true;
    // En mode headless, le navigateur ne s'ouvre pas pendant l'execution
    static boolean HEADLESS = true;
    private String pageContentBeforeClose;

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
        testInstance = extensionContext.getTestInstance().get();
        setContext();
    }

    public void autoInitPageFields(Object instanceObject) {
       final List<Field> pageFields = Arrays.stream(instanceObject.getClass().getDeclaredFields())
               .filter(field -> isPageObject(field))
               .collect(Collectors.toList());

        this.page().setDefaultTimeout(3000);
        for (Field pageField : pageFields) {
            try {
                pageField.setAccessible(true);
                final Constructor<?> constructor = pageField.getType().getConstructor(Page.class);
                pageField.set(instanceObject, constructor.newInstance(this.page()));
            } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isPageObject(Field field) {
        field.setAccessible(true);
        return Arrays.stream(field.getType().getConstructors())
                .anyMatch(c -> c.getParameterTypes().length == 1 && c.getParameterTypes()[0] == Page.class);
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
        autoInitPageFields(testInstance);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        if (TRACE_ACTIVE) {
            final Path outputPath = Paths.get("build", "trace", "trace.zip");
            context.tracing().stop(
                    new Tracing.StopOptions().setPath(outputPath));
        }
        pageContentBeforeClose = page.content();
        context.close();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println("Content before test failed:\n" + pageContentBeforeClose);
    }

    public void allerAccueil() {
        page().navigate(Site.getUrlAccueil());
    }

    public Page page() {
        return page;
    }
}
