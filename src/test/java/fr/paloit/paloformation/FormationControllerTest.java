package fr.paloit.paloformation;

import fr.paloit.paloformation.controller.FormationController;
import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.service.FormationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;



import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@WebMvcTest(FormationController.class)
public class FormationControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private FormationService formationService;


    @Test
    public void testAfficherFormations() throws Exception {

        List<Formation> formations = new ArrayList<>();
        Formation formation1 =  new Formation();
        formation1.setId(1L);
        formation1.setIntitule("intitule1");

        formations.add(formation1);

        when(formationService.listeFormations())
                .thenReturn(formations);

mvc.perform(MockMvcRequestBuilders.get("/formations"))
        .andExpect(status().isOk())
        .andExpect(view().name("formations"))
        .andExpect(model().attributeExists("formations"));


    }









}
