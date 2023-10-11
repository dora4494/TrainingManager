package fr.paloit.paloformation.service;

import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.repository.FormationRepository;
import fr.paloit.paloformation.service.FormationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class FormationServiceBDTest {

    @Autowired
    private FormationService formationService;

    @Autowired
    private FormationRepository formationRepository;

    @Test
    public void testCreerFormation() {
        Formation formation = new Formation(null, "TDD", new ArrayList<>());

        formationService.creerFormation(formation);

        List<Formation> formations = StreamSupport
                .stream(formationService.listeFormations().spliterator(), false)
                .collect(Collectors.toList());
        assertEquals(1, formations.size());
        assertEquals("TDD",formations.get(0).getIntitule());
    }

    @Test
    public void testCreerFormationDejaExistant() {
        {
            // L'enregistrement positionne un ID sur la formation.
            // C'est pour cela que l'on ne réutilise pas l'objet pour la seconde insertion.
            Formation formation = new Formation(null, "TDD", new ArrayList<>());
            formationRepository.save(formation);
        }

        Formation formation = new Formation(null, "TDD", new ArrayList<>());
        formationService.creerFormation(formation);

        List<Formation> formations = StreamSupport
                .stream(formationService.listeFormations().spliterator(), false)
                .collect(Collectors.toList());
        assertEquals(1, formations.size());
        assertEquals("TDD",formations.get(0).getIntitule());
    }
}