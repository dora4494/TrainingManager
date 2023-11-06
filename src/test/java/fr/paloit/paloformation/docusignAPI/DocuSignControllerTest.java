package fr.paloit.paloformation.docusignAPI;

import fr.paloit.paloformation.controller.ControllerGlobalAdvice;
import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.SessionBuilder;
import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.EmargementService;
import fr.paloit.paloformation.service.SessionService;
import fr.paloit.paloformation.service.UtilisateurService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DocuSignController.class)
@AutoConfigureMockMvc
public class DocuSignControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DocuSignService docuSignService;
    @MockBean
    private SessionService sessionService;
    @MockBean
    private UtilisateurService utilisateurService;
    @MockBean
    ControllerGlobalAdvice controllerGlobalAdvice;

    @Test
    public void testDemandeDeSignature() throws Exception {
        Session session = SessionBuilder.uneSession("TDD")
                .setFormateur(new Utilisateur(1L, "John", "Doe", "jdoe@palo-it.com"))
                .get();

        final DocuSignFeuilleEmargement feuilleEmargement = new DocuSignFeuilleEmargement();

        final List<Utilisateur> utilisateurs = List.of(
                new Utilisateur(32L, "John", "Doe", "jdoe@palo-it.com"),
                new Utilisateur(36L, "Jean", "Petit", "jpetit@palo-it.com")
        );

        Mockito.when(sessionService.trouverSessionById(session.getId())).thenReturn(session);
        Mockito.when(utilisateurService.listeUtilisateursById(utilisateurs.stream().map(Utilisateur::getId).toList())).thenReturn(utilisateurs);
        Mockito.when(docuSignService.getFeuilleEmargement(Mockito.any(Session.class))).thenReturn(feuilleEmargement);

        final ResultActions resultActions = this.mvc.perform(post("/docusign")
                        .param("idSession", session.getId().toString())
                        .param("ids", utilisateurs.stream().map(u -> u.getId().toString()).toArray(String[]::new)))
                .andExpect(status().is3xxRedirection());


        ArgumentCaptor<List<Utilisateur>> utilisateursCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<EmargementService.FeuilleEmargement> feuilleEmargementCaptor = ArgumentCaptor.forClass(EmargementService.FeuilleEmargement.class);
        Mockito.verify(docuSignService).envoyerDemandeSignature(utilisateursCaptor.capture(), feuilleEmargementCaptor.capture());

        final List<Utilisateur> utilisateurParametre = utilisateursCaptor.getValue();
        assertEquals(2, utilisateurParametre.size());
        assertEquals("jdoe@palo-it.com", utilisateurParametre.get(0).getMail());
        assertEquals("jpetit@palo-it.com", utilisateurParametre.get(1).getMail());
    }
}
