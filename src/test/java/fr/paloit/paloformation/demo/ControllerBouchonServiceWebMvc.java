package fr.paloit.paloformation.demo;

import fr.paloit.paloformation.controller.ControllerGlobalAdvice;
import fr.paloit.paloformation.controller.FormationController;
import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.repository.FormationRepository;
import fr.paloit.paloformation.service.FormationService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test avec un bouchon au niveau de la couche service.
 */
@WebMvcTest(FormationController.class)
@AutoConfigureMockMvc
public class ControllerBouchonServiceWebMvc {

    @Autowired
    private MockMvc mvc;

    // Création d'un bouchon pour le service
    @MockBean
    private FormationService formationService;

    @MockBean
    ControllerGlobalAdvice controllerGlobalAdvice;

    @Test
    public void testAfficherFormations() throws Exception {
        // On spécifie les valeurs atttendues lors des appels au service.
        Mockito.when(formationService.listeFormations()).thenReturn(Arrays.asList(
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
