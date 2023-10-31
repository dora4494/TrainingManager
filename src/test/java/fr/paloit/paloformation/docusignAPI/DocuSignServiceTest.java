package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.Signer;
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
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@WebMvcTest(DocuSignService.class)
@AutoConfigureMockMvc
public class DocuSignServiceTest {

    @SpyBean
    DocuSignService docuSignService;

    @MockBean
    DocuSign docuSign;

    @Test
    public void testEnvoyerFeuilleEmargementEnvoiUnEnveloppeParUtilisateur() throws IOException, ApiException {
        Session session = SessionBuilder.uneSession("TDD")
                .setFormateur(new Utilisateur(1L, "John", "Doe", "jdoe@palo-it.com"))
                .get();

        final DocuSignFeuilleEmargement feuilleEmargement = (DocuSignFeuilleEmargement)docuSignService.getFeuilleEmargement(session);
        feuilleEmargement.setNomFichier("Emargement.txt");
        feuilleEmargement.setTexteDocument("Contenu de la feuille d'émargement");

        List<Utilisateur> utilisateurs = List.of(
                new Utilisateur(32L, "John", "Doe", "jdoe@palo-it.com"),
                new Utilisateur(36L, "Jean", "Petit", "jpetit@palo-it.com")
        );
        docuSignService.envoyerDemandeSignature(utilisateurs, feuilleEmargement);

        ArgumentCaptor<EnveloppeDocuSign> enveloppeCaptor = ArgumentCaptor.forClass(EnveloppeDocuSign.class);
        Mockito.verify(docuSignService, Mockito.times(utilisateurs.size())).envoyerEnveloppe(enveloppeCaptor.capture());
        final List<EnveloppeDocuSign> enveloppes = enveloppeCaptor.getAllValues();

        for (int i = 0; i < utilisateurs.size(); i++) {
            Utilisateur utilisateur = utilisateurs.get(i);
            final EnveloppeDocuSign enveloppe = enveloppes.get(i);

            assertEquals(1, enveloppe.getUtilisateurs().size());
            assertEquals(utilisateur.getNom(), enveloppe.getUtilisateurs().get(0).getNom());
            assertEquals(utilisateur.getMail(), enveloppe.getUtilisateurs().get(0).getMail());

            assertEquals("Emargement.txt", enveloppe.getNomDocument());
            assertEquals("Contenu de la feuille d'émargement", enveloppe.getTexteDocument());
        }
    }

    @Test
    public void testCreerEnveloppe() {

        final Utilisateur utilisateur = new Utilisateur(1L, "John", "Doe", "john.doe.test@palo-it.com");
        EnvelopeDefinition enveloppe = DocuSignService.creerEnveloppe(utilisateur).generer();

        final Recipients recipients = enveloppe.getRecipients();

        assertNull(recipients.getCarbonCopies());

        final List<Signer> participants = recipients.getSigners();
        assertEquals(1, participants.size());
        {
            final Signer signer = participants.get(0);
            assertEquals("John", signer.getFirstName());
            assertEquals("Doe", signer.getLastName());
            // TODO Faut il renseigner le name et le fullName?
            assertEquals("John Doe", signer.getName());
            assertEquals(null, signer.getFullName());
            assertEquals("john.doe.test@palo-it.com", signer.getEmail());
        }
    }
}
