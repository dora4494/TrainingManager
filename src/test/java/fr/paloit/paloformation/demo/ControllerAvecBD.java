package fr.paloit.paloformation.demo;

import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.repository.FormationRepository;
import fr.paloit.paloformation.service.FormationService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

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
public class ControllerAvecBD {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FormationService formationService;

    @Autowired
    FormationRepository formationRepository;


    @Test
    public void testCreerFormations() throws Exception {
        // On créé une formation en passant par le controller.
        // On aurait aussi pu la créer en appelant directement le service ou le repository si on ne cherche pas à tester le controller.
        final ResultActions resultat_creation = this.mvc.perform(post("/formation-cree") // Appel de création d'une formation
                .flashAttr("formation", new Formation(null, "XXX", new ArrayList<>()))) // Ajout des attributs nécessaires
                .andExpect(status().is3xxRedirection())                                 // Indique que l'on a fait une redirection
                .andExpect(view().name("redirect:/formations"));                        // Vérification de la page de redirection

        final ResultActions resultActions = this.mvc.perform(get("/formations"))
                .andExpect(status().isOk())                                             // Vérification que la requête renvoi un code 200 (pas d'erreur)
                .andExpect(view().name("formations"))                                   // Nom de la vue à afficher en retour
                .andExpect(model().attributeExists("formations"))                       // Vérification de la présence de l'attribut 'formations' au niveau du model du controller
                .andExpect(model().attribute("formations", Matchers.hasSize(1)));       // Vérification du nombre d'élément dans l'attribut 'formations'


        final String body = resultActions.andReturn().getResponse().getContentAsString(); // Récupération du contenu HTML renvoyé
        assertTrue(body.contains("XXX"));
    }

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
}
