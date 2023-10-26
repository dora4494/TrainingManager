package fr.paloit.paloformation.ihm;

import fr.paloit.paloformation.controller.IndexController;
import fr.paloit.paloformation.controller.SessionController;
import fr.paloit.paloformation.model.*;
import fr.paloit.paloformation.service.SessionService;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {IndexController.class, SessionController.class})
@AutoConfigureMockMvc
public class IndexTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    SessionService sessionService;

    @Test
    public void testIndex() throws Exception {

        Mockito.when(sessionService.listeSesions())
                .thenReturn(List.of(SessionBuilder.uneSession("DDD").get()));

        final String redirectedUrl = "/sessions";

        this.mvc.perform(get(""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectedUrl));

        final ResultActions resultActions = this.mvc.perform(get(redirectedUrl))
                .andExpect(status().isOk());

        final Document html = OutilsTestHtml.toHtmlDocument(resultActions);
        final String htmlText = html.body().text();
        assertTrue(htmlText.contains("DDD"), htmlText);
    }

}
