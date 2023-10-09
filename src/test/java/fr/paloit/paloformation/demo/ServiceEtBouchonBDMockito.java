package fr.paloit.paloformation.demo;

import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.repository.FormationRepository;
import fr.paloit.paloformation.service.FormationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test du service avec un bouchon au niveau de la couche repository.
 * On utilise uniquement Mockito sans utiliser le context Spring.
 * La méthode de test est identique à celle avec Spring.
 * On change juste la création des objets en passant par les annotations Mockito plutôt que Spring.
 */
@ExtendWith(MockitoExtension.class)
public class ServiceEtBouchonBDMockito {

    @InjectMocks
    private FormationService formationService;

    // Création d'un bouchon pour la base de données
    @Mock
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
