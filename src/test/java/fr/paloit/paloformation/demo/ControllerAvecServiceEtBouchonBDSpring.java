package fr.paloit.paloformation.demo;

import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.repository.FormationRepository;
import fr.paloit.paloformation.service.FormationService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test avec un bouchon au niveau de la couche repository.
 * Les classes de la couche service du context spring sont réellement utilisées.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerAvecServiceEtBouchonBDSpring {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FormationService formationService;

    // Création d'un bouchon pour la base de données
    @MockBean
    FormationRepository formationRepository;

    @Test
    public void testAfficherFormations() throws Exception {
        // On spécifie les valeurs atttendues lors des appels à la base de données.
        Mockito.when(formationRepository.findAll()).thenReturn(Arrays.asList(
                new Formation(5L, "DDD", Arrays.asList()),
                new Formation(6L, "Clean code", Arrays.asList())
        ));

        final ResultActions resultActions = this.mvc.perform(get("/formations"))
                .andExpect(status().isOk())                                             // Vérification que la requête renvoi un code 200 (pas d'erreur)
                .andExpect(view().name("formations"))                                   // Nom de la vue à afficher en retour
                .andExpect(model().attributeExists("formations"))                       // Vérification de la présence de l'attribut 'formations' au niveau du model du controller
                .andExpect(model().attribute("formations", Matchers.hasSize(2)));       // Vérification du nombre d'élément dans l'attribut 'formations'


        final String body = resultActions.andReturn().getResponse().getContentAsString(); // Récupération du contenu HTML renvoyé
        assertTrue(body.contains("DDD"));
        assertTrue(body.contains("Clean code"));
    }

}
