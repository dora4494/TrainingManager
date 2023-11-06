package fr.paloit.paloformation.controller;

import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.Tache;
import fr.paloit.paloformation.model.ToDo;
import fr.paloit.paloformation.repository.FormationRepository;
import fr.paloit.paloformation.repository.SessionRepository;
import fr.paloit.paloformation.service.FormationService;
import fr.paloit.paloformation.service.SessionService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test du controller en utilisant toutes les couches jusqu'à la base de données.
 * On utiliser le @Autowired pour injecter les classes qui seront utilisées en production.
 */
@SpringBootTest // Ne fonctionne pas avec WebMvcTest car il ne charge pas tous les objets dans le context spring.
@AutoConfigureMockMvc
@Transactional // Pour faire un rollback de base à chaque test
public class SessionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private FormationService formationService;



/*
    @Test
    public void testCreerSessions() throws Exception {
        // On créé une session en passant par le controller.
        // On aurait aussi pu la créer en appelant directement le service ou le repository si on ne cherche pas à tester le controller.


        Formation formation = new Formation(null, "test", Collections.emptyList());
        formationService.creerFormation(formation);
        Tache tache = new Tache(null, "tache", Collections.emptyList());
        Session session = new Session(null, formation, "client", Collections.emptySet(), Collections.emptyList(), null, 1, LocalDate.now(), LocalDate.now(), null, null, 2);
        ToDo todo = new ToDo(null, tache, LocalDate.now(), 2, session);

        final ResultActions resultat_creation = this.mvc.perform(post("/session-creee") // Appel de création d'une formation
                        .flashAttr("session", session)) // Ajout des attributs nécessaires
                .andExpect(status().is3xxRedirection())                                 // Indique que l'on a fait une redirection
                .andExpect(view().name("redirect:/sessions"));                        // Vérification de la page de redirection

        final ResultActions resultActions = this.mvc.perform(get("/sessions"))
                .andExpect(status().isOk())                                             // Vérification que la requête renvoi un code 200 (pas d'erreur)
                .andExpect(view().name("sessions"))                                   // Nom de la vue à afficher en retour
                .andExpect(model().attributeExists("sessions"))                       // Vérification de la présence de l'attribut 'formations' au niveau du model du controller
                .andExpect(model().attribute("sessions", Matchers.hasSize(1)));       // Vérification du nombre d'élément dans l'attribut 'formations'

    }
*/
    /*
    @Test
    public void testAfficherFormationsAvecCreationViaLeService() throws Exception {
        // On créé une formation en passant par le service.
        // Peut importe la manière de créer les données dans le phase d'initialisation du test.
        formationService.creerFormation(new Formation(null, "XXX", new ArrayList<>()));

        final ResultActions resultActions = this.mvc.perform(get("/formations"))
                .andExpect(status().isOk())                                             // Vérification que la requête renvoi un code 200 (pas d'erreur)
                .andExpect(view().name("formations"))                                   // Nom de la vue à afficher en retour
                .andExpect(model().attributeExists("formations"))                       // Vérification de la présence de l'attribut 'formations' au niveau du model du controller
                .andExpect(model().attribute("formations", Matchers.hasSize(1)));       // Vérification du nombre d'élément dans l'attribut 'formations'


        final String body = resultActions.andReturn().getResponse().getContentAsString(); // Récupération du contenu HTML renvoyé
        assertTrue(body.contains("XXX"));
    }
    */

}