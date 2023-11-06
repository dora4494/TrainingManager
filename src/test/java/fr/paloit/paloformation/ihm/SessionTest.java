package fr.paloit.paloformation.ihm;

import fr.paloit.paloformation.controller.ControllerGlobalAdvice;
import fr.paloit.paloformation.controller.SessionController;
import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.ToDo;
import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.SessionService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@AutoConfigureMockMvc
public class SessionTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    ControllerGlobalAdvice controllerGlobalAdvice;

    @Test
    public void testContenuDuTableauDesSessions() throws Exception {
        Formation formation = new Formation(5L, "DDD", Arrays.asList());
        Set<Utilisateur> participants = new HashSet<>();
        Set<LocalDate> dates = new HashSet<>() {
        };
        dates.add(LocalDate.of(2023, 10, 22));
        List<ToDo> todos = new ArrayList<>();
        Utilisateur formateur = new Utilisateur(36L, "Jean", "Petit", "jpetit@palo-it.com");
        int etat = 0;
        Mockito.when(sessionService.listeSesions()).thenReturn(Arrays.asList(
                new Session(23L, formation, "client", participants, todos, formateur, 2, 230L, "modalites", etat, dates, null)
        ));


        final ResultActions resultActions = this.mvc.perform(get("/sessions"))
                .andExpect(status().isOk());

        final Document html = OutilsTestHtml.toHtmlDocument(resultActions);

        final Elements sessions = html.select("#lst-container-sessions");
        final List<String> texteEnTeteColonnes = extraireEnTeteColonnes(sessions);

        final List<List<String>> tableSessions = extraireLignes(sessions);

        final List<String> session = tableSessions.get(0);
        assertEquals("DDD", session.get(texteEnTeteColonnes.indexOf("Formation")));
        assertEquals("client", session.get(texteEnTeteColonnes.indexOf("Client")));
        assertEquals("Jean Petit", session.get(texteEnTeteColonnes.indexOf("Formateur")));
    }

    private static List<List<String>> extraireLignes(Elements sessions) {
        final Elements lignes = sessions.select("tbody").first().select("tr");
        final List<List<String>> tableSessions = lignes.stream().map(
                ligne -> ligne.select("td").stream().map(e -> e.text()).collect(Collectors.toList())
        ).collect(Collectors.toList());
        return tableSessions;
    }

    private static List<String> extraireEnTeteColonnes(Elements sessions) {
        final Element elementEnTete = sessions.select("thead").first();
        final List<String> texteEnTeteColonnes = elementEnTete.select("th").stream().map(e -> e.text()).collect(Collectors.toList());
        System.out.println("SessionTest.testSessions: " + texteEnTeteColonnes);
        return texteEnTeteColonnes;
    }


    @Test
    public void testafficherDetailsSession() throws Exception {
        Formation formation = new Formation(5L, "DDD", Arrays.asList());
        Set<Utilisateur> participants = new HashSet<>();
        Set<LocalDate> dates = new HashSet<>() {
        };
        dates.add(LocalDate.of(2023, 10, 22));
        List<ToDo> todos = new ArrayList<>();
        Utilisateur formateur = new Utilisateur(36L, "Jean", "Petit", "jpetit@palo-it.com");
        int etat = 0;
        Mockito.when(sessionService.trouverSessionById(23L)).thenReturn(
                new Session(23L, formation, "client", participants, todos, formateur, 2, 230L, "modalites", etat, dates, null)
        );


        final ResultActions resultActions = this.mvc.perform(get("/session/23"))
                .andExpect(status().isOk());

        final String body = resultActions.andReturn().getResponse().getContentAsString();
        final Document html = Jsoup.parse(body);

        final Elements detailsSession = html.select("#items-detail-session");

        final Map<String, String> inputs = extrairetexteInput(detailsSession);

        assertEquals("DDD", inputs.get("Formation:"));
        assertEquals("client", inputs.get("Client:"));
        assertEquals("Jean Petit", inputs.get("Formateur:"));
    }

    private static Map<String, String> extrairetexteInput(Elements detailsSession) {
        final Elements elementInputs = detailsSession.select(".pure-u-1-3");
//        Map<String, String> listeChamps = new HashMap<>();
//        for (Element elementInput : elementInputs) {
//            String span = elementInput.selectFirst("span").text();
//            String p = elementInput.selectFirst("p").text();
//            System.out.println(span + " " + p);
//            listeChamps.put(p, span);
//        }
//
//        return listeChamps;

        return elementInputs.stream().collect(Collectors.toMap(
                element -> element.selectFirst("p").text(),
                element -> element.selectFirst("span").text()));

    }

}






