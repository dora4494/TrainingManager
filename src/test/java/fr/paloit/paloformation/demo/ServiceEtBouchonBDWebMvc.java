package fr.paloit.paloformation.demo;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test du service avec un bouchon au niveau de la couche repository.
 */
@WebMvcTest(FormationService.class)
@AutoConfigureMockMvc
public class ServiceEtBouchonBDWebMvc {

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

        // Récupération de la liste des formations
        List<Formation> formations = StreamSupport
                .stream(formationService.listeFormations().spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(2, formations.size());
        final List<String> intitules = formations.stream()
                .map(Formation::getIntitule)
                .collect(Collectors.toList());
        assertTrue(intitules.contains("DDD"));
        assertTrue(intitules.contains("Clean code"));

    }

}
