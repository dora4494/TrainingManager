package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.client.ApiException;
import fr.paloit.paloformation.controller.ControllerGlobalAdvice;
import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.SessionBuilder;
import fr.paloit.paloformation.model.Utilisateur;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(DocuSignService.class)
@AutoConfigureMockMvc
public class DocuSignServiceTest {

    @SpyBean
    DocuSignService docuSignService;

    @MockBean
    DocuSignReel docuSign;

    @MockBean
    ControllerGlobalAdvice controllerGlobalAdvice;


    @Test
    public void testEnvoyerFeuilleEmargementEnvoiUneEnveloppePourTousLesUtilisateurs() throws IOException, ApiException {
        Session session = SessionBuilder.uneSession("TDD")
                .setFormateur(new Utilisateur(1L, "John", "Doe", "jdoe@palo-it.com"))
                .get();

        final DocuSignFeuilleEmargement feuilleEmargement = (DocuSignFeuilleEmargement) docuSignService.getFeuilleEmargementTexte(session);
        feuilleEmargement.setNomFichier("Emargement.txt");
        feuilleEmargement.setTexteDocument("Contenu de la feuille d'émargement");

        List<Utilisateur> utilisateurs = List.of(
                new Utilisateur(32L, "John", "Doe", "jdoe@palo-it.com"),
                new Utilisateur(36L, "Jean", "Petit", "jpetit@palo-it.com")
        );
        docuSignService.envoyerDemandeSignature(utilisateurs, feuilleEmargement);

        ArgumentCaptor<EnveloppeDocuSign> enveloppeCaptor = ArgumentCaptor.forClass(EnveloppeDocuSign.class);
        Mockito.verify(docuSignService, Mockito.times(1)).envoyerEnveloppe(enveloppeCaptor.capture());
        final List<EnveloppeDocuSign> enveloppes = enveloppeCaptor.getAllValues();
        final EnveloppeDocuSign enveloppe = enveloppes.get(0);
        assertEquals("Emargement.txt", enveloppe.getNomDocument());
        assertArrayEquals("Contenu de la feuille d'émargement".getBytes(), enveloppe.getDocumentBytes());

        assertEquals(utilisateurs.size(), enveloppe.getUtilisateurs().size());
        for (int i = 0; i < utilisateurs.size(); i++) {
            Utilisateur utilisateur = utilisateurs.get(i);

            assertEquals(utilisateur.getNom(), enveloppe.getUtilisateurs().get(i).getNom());
            assertEquals(utilisateur.getMail(), enveloppe.getUtilisateurs().get(i).getMail());
        }
    }

    @Test
    public void testGetFeuilleEmargement() {
        final Session session = SessionBuilder.uneSession("TDD")
                .setFormateur(new Utilisateur(32L, "John", "Glenn", "jgleen@palo-it.com"))
                .setParticipants(Set.of(
                        new Utilisateur(35L, "Paul", "Martin", "pmartin@palo-it.com"),
                        new Utilisateur(36L, "Bob", "Ray", "bray@palo-it.com")
                        )
                ).get();
        final DocuSignFeuilleEmargement feuilleEmargement = (DocuSignFeuilleEmargement)docuSignService.getFeuilleEmargementTexte(session);
        assertEquals("emargement_TDD.txt", feuilleEmargement.getNomFichier());
        assertTrue(feuilleEmargement.getTexteDocument().contains("Formation: TDD"));
        assertTrue(feuilleEmargement.getTexteDocument().contains("Formateur: John Glenn"));
        assertTrue(feuilleEmargement.getTexteDocument().contains("Paul Martin"));
        assertTrue(feuilleEmargement.getTexteDocument().contains("Bob Ray"));
    }
}