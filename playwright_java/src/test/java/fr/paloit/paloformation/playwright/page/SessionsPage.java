package fr.paloit.paloformation.playwright.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.Optional;

public class SessionsPage {
    private final Page page;
    private final Locator tableauSession;


    public SessionsPage(Page page) {
        this.page = page;
        this.tableauSession = page.locator("#lst-container-sessions");
    }

    public void ouvrirDerniereSession(String nomFormation) {
        final Locator sessions = tableauSession.locator("td a").filter(new Locator.FilterOptions().setHasText(nomFormation));
        sessions.last().click();
    }
}
