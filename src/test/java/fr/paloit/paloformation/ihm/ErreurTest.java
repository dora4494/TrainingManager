package fr.paloit.paloformation.ihm;

import fr.paloit.paloformation.controller.ErreurController;
import fr.paloit.paloformation.controller.SessionController;
import fr.paloit.paloformation.service.SessionService;
import jakarta.servlet.RequestDispatcher;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest({ErreurController.class, SessionController.class})
@AutoConfigureMockMvc
public class ErreurTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SessionService sessionService;

    @Test
    public void testPageIntrouvableRetourneStatus404() throws Exception {
        final ResultActions resultActions = this.mvc.perform(get("/page_inconnue"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testPageNotFoundContientUnRetourAccueil() throws Exception {
        final ResultActions resultActions = this.mvc.perform(get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value()))
                .andExpect(status().isOk())
                .andExpect(view().name("erreur_introuvable"));

        final Document html = OutilsTestHtml.toHtmlDocument(resultActions);

        final String texteHtml = html.body().text();
        assertTrue(texteHtml.contains("Page introuvable"), "HTML: " + texteHtml);

        final Elements select = html.select("a");
        select.stream().anyMatch(element -> element.attr("href") == "/");
    }

    @Test
    public void testPageErreurContientUnRetourAccueil() throws Exception {
        final ResultActions resultActions = this.mvc.perform(get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(status().isOk())
                .andExpect(view().name("erreur_technique"));

        final Document html = OutilsTestHtml.toHtmlDocument(resultActions);

        final String texteHtml = html.body().text();
        assertTrue(texteHtml.contains("Erreur technique"), "HTML: " + texteHtml);

        final Elements select = html.select("a");
        select.stream().anyMatch(element -> element.attr("href") == "/");
    }

    @Test
    public void testPageSansStatusRenvoiVersUneErreurTechnique() throws Exception {
        final ResultActions resultActions = this.mvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("erreur_technique"));
    }

    @Test
    public void testPageException() throws Exception {
        Mockito.when(sessionService.listeSesions()).thenThrow(new RuntimeException("???"));

        final ResultActions resultActions = this.mvc.perform(get("/sessions"))
                .andExpect(status().isInternalServerError())
                .andExpect(status().is5xxServerError());

        final String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(contentAsString);
    }


}
