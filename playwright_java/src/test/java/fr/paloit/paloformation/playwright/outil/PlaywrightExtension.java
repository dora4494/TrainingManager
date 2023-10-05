package fr.paloit.paloformation.playwright.outil;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.extension.*;

public class PlaywrightExtension implements AfterEachCallback, AfterAllCallback, BeforeEachCallback, BeforeAllCallback {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        playwright.close();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        context = browser.newContext();
        page = context.newPage();
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        context.close();
    }

    public Page page() {
        return page;
    }
}
