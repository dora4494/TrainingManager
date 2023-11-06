package fr.paloit.paloformation.controller;

import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.FormationService;
import fr.paloit.paloformation.service.SessionService;
import fr.paloit.paloformation.service.ToDoService;
import fr.paloit.paloformation.service.UtilisateurService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.*;

@WebMvcTest(SessionController.class)
@AutoConfigureMockMvc
public class SessionControllerTestBouchonService {

    @Autowired
    MockMvc mvc;

    // Bouchon Service

    @MockBean
    SessionService sessionService;

    @MockBean
    ControllerGlobalAdvice controllerGlobalAdvice;


    private Formation getUneFormation() {
        return new Formation(null, "formation", null);
    }

    private Utilisateur getUnFormateur() {
        return new Utilisateur(null, "Jean", "Bodier", Arrays.asList().toString());
    }

    @Test
    public void afficherSession() throws Exception {

        Mockito.when(sessionService.listeSesions()).thenReturn(Arrays.asList(
                new Session(null, getUneFormation(), "client", new HashSet<>(), new ArrayList<>(), getUnFormateur(),
                        2, 4L, "modalites", 1, Set.of(LocalDate.of(2023, 11, 21)), null)
        ));

        final ResultActions resultActions = this.mvc.perform(get("/sessions"))
                .andExpect(status().isOk())
                .andExpect(view().name("sessions"))
                .andExpect(model().attributeExists("sessions"))
                .andExpect(model().attribute("sessions", Matchers.hasSize(1)));
    }

    @Test
    public void creerSession() throws Exception {
        Session session = new Session(null, getUneFormation(), "client", new HashSet<>(), new ArrayList<>(), getUnFormateur(),
                2, 4L, "modalites", 1, Set.of(LocalDate.of(2023, 11, 21)), null);

        final ResultActions resultat_creation = this.mvc.perform(post("/session-creee") // Appel de l'url de création d'une session
                        .flashAttr("session", session)) // Ajout de l'attribut session qui est passé en paramètre de la méthode de création d'une session
                .andExpect(status().is3xxRedirection())  // Indique que l'on a fait une redirection
                .andExpect(view().name("redirect:/sessions")); // Vérification de la page de redirection

        Mockito.verify(sessionService).creerSession(Mockito.any()); // Vérification que la méthode creerSession est bien appelée

    }


}
