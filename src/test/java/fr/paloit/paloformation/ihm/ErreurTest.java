package fr.paloit.paloformation.ihm;

import fr.paloit.paloformation.controller.ErreurController;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ErreurController.class)
@AutoConfigureMockMvc
public class ErreurTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testPageIntrouvable() throws Exception {
        final ResultActions resultActions = this.mvc.perform(get("/page_inconnue"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testPageErreur() throws Exception {
        final ResultActions resultActions = this.mvc.perform(get("/error"))
                .andExpect(status().isOk());

        final Document html = OutilsTestHtml.toHtmlDocument(resultActions);

        assertTrue(html.body().text().contains("Page introuvable"));

        final Elements select = html.select("a");
        select.stream().anyMatch(element -> element.attr("href") == "/");
    }


}
