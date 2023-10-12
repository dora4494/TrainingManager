package fr.paloit.paloformation.demo;

import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.repository.FormationRepository;
import fr.paloit.paloformation.service.FormationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test du service avec une vraie base de données
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ServiceEtBDWebMvc {

    @Autowired
    private FormationService formationService;

    // Utilisation d'une vraie base de données.
    @Autowired
    FormationRepository formationRepository;

    @Test
    public void testAfficherFormations() throws Exception {
        formationService.creerFormation(new Formation(null, "Test", Arrays.asList()));
        final Iterable<Formation> formations = formationService.listeFormations();
        final long count = StreamSupport.stream(formations.spliterator(), false).count();
        assertEquals(1, count);

    }

}
