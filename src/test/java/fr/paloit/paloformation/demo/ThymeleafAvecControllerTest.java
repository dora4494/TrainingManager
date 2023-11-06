package fr.paloit.paloformation.demo;

import fr.paloit.paloformation.controller.ControllerGlobalAdvice;
import fr.paloit.paloformation.controller.FormationController;
import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.service.FormationService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test du contenu HTML généré par Thymeleaf suite à l'appel du controller.
 * Le service est bouchonné pour controller les valeurs à retourner.
 */
@WebMvcTest(FormationController.class)
@AutoConfigureMockMvc
public class ThymeleafAvecControllerTest {

    @Autowired
    private MockMvc mvc;

    // Création d'un bouchon pour le service
    @MockBean
    private FormationService formationService;

    @MockBean
    ControllerGlobalAdvice controllerGlobalAdvice;

    @Test
    public void testFormations() throws Exception {
        // Bouchon des valeurs retournées par la service
        Mockito.when(formationService.listeFormations()).thenReturn(Arrays.asList(
                new Formation(5L, "DDD", Arrays.asList()),
                new Formation(6L, "Clean code", Arrays.asList())
        ));

        final ResultActions resultActions = this.mvc.perform(get("/formations"))
                .andExpect(status().isOk());

        final String body = resultActions.andReturn().getResponse().getContentAsString(); // Récupération du contenu HTML renvoyé
        final Document html = Jsoup.parse(body); // Parsing du html via jsoup pour avoir un objet manipulable
        final Element link = html.select("a").stream().filter(elt -> elt.text().equals("DDD")).findFirst().get();
        assertEquals("formation/5", link.attr("href"));

    }


}
