package fr.paloit.paloformation;

import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.repository.FormationRepository;
import fr.paloit.paloformation.service.FormationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@WebMvcTest(FormationService.class)
public class FormationServiceTest {

    @InjectMocks
    private FormationService formationService;

    @Mock
    private FormationRepository formationRepository;

     /* @Test
  public void testCreerFormation() {
        Formation formation = new Formation();
        formation.setIntitule("test");

        when(formationRepository.save(any(Formation.class))).thenReturn(formation);

        Formation creee = formationService.creerFormation(formation);

        assertThat(creee).isNotNull();
        assertThat(creee.getIntitule()).isEqualTo(formation.getIntitule());

    }
    */

}