package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.model.*;
import fr.paloit.paloformation.model.Utilisateur;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnveloppeDocuSignTest {
    final Utilisateur utilisateur = new Utilisateur(1L, "John", "Doe", "john.doe.test@palo-it.com");

    @Test
    public void testAjoutDUnSignataire() {
        final EnveloppeDocuSign enveloppeDocuSign = new EnveloppeDocuSign();
        enveloppeDocuSign.ajouterSignataire(new Utilisateur(1L, "John", "Doe", "john.doe.test@palo-it.com"));
        enveloppeDocuSign.ajouterSignataire(new Utilisateur(2L, "Franck", "Hawk", "franck.hawk.test@palo-it.com"));

        EnvelopeDefinition enveloppe = enveloppeDocuSign.generer();

        final Recipients recipients = enveloppe.getRecipients();
        assertNull(recipients.getCarbonCopies());

        final List<Signer> participants = recipients.getSigners();
        assertEquals(2, participants.size());
        {
            final Signer signer = participants.get(0);
            assertEquals("John", signer.getFirstName());
            assertEquals("Doe", signer.getLastName());
            // TODO Faut il renseigner le name et le fullName?
            assertEquals("John Doe", signer.getName());
            assertEquals(null, signer.getFullName());
            assertEquals("john.doe.test@palo-it.com", signer.getEmail());
        }
        {
            final Signer signer = participants.get(1);
            assertEquals("Franck", signer.getFirstName());
            assertEquals("Hawk", signer.getLastName());
            assertEquals("Franck Hawk", signer.getName());
            assertEquals(null, signer.getFullName());
            assertEquals("franck.hawk.test@palo-it.com", signer.getEmail());
        }

        assertNull(enveloppe.getTemplateRoles());
    }

    @Test
    public void testEnveloppeAvecUnTemplate() {
        final EnveloppeDocuSign enveloppeDocuSign = new EnveloppeDocuSign();
        assertEquals(null, enveloppeDocuSign.generer().getTemplateId());

        enveloppeDocuSign.setTemplateId("123456");
        assertEquals("123456", enveloppeDocuSign.generer().getTemplateId());
    }

    @Test
    public void testAjoutDUnSignataireSurTemplate() {
        final EnveloppeDocuSign enveloppeDocuSign = new EnveloppeDocuSign();
        enveloppeDocuSign.ajouterSignataire(new Utilisateur(1L, "John", "Doe", "john.doe.test@palo-it.com"));
        enveloppeDocuSign.ajouterSignataire(new Utilisateur(2L, "Franck", "Hawk", "franck.hawk.test@palo-it.com"));
        enveloppeDocuSign.setTemplateId("123456");

        EnvelopeDefinition enveloppe = enveloppeDocuSign.generer();

        assertEquals("123456", enveloppe.getTemplateId());

        final Recipients recipients = enveloppe.getRecipients();
        assertNull(recipients);

        final List<TemplateRole> participants = enveloppe.getTemplateRoles();
        assertEquals(2, participants.size());
        {
            final TemplateRole signer = participants.get(0);
            assertEquals("John Doe", signer.getName());
            assertEquals("john.doe.test@palo-it.com", signer.getEmail());
            assertNotNull(signer.getRoleName()); // Faut il vérifier une valeur spécifique ?
        }
        {
            final TemplateRole signer = participants.get(1);
            assertEquals("Franck Hawk", signer.getName());
            assertEquals("franck.hawk.test@palo-it.com", signer.getEmail());
            assertNotNull(signer.getRoleName());
        }
    }

    @Test
    public void testAjoutDesSignatureSurTemplate() {
        List<Utilisateur> utilisateurs = List.of(
                new Utilisateur(1L, "John", "Doe", "john.doe.test@palo-it.com"),
                new Utilisateur(2L, "Franck", "Hawk", "franck.hawk.test@palo-it.com")
        );

        final EnveloppeDocuSign enveloppeDocuSign = new EnveloppeDocuSign();
        for (Utilisateur utilisateur : utilisateurs) {
            enveloppeDocuSign.ajouterSignataire(utilisateur);
        }

        enveloppeDocuSign.setTemplateId("123456");

        EnvelopeDefinition enveloppe = enveloppeDocuSign.generer();

        assertEquals("123456", enveloppe.getTemplateId());

        final Recipients recipients = enveloppe.getRecipients();
        assertNull(recipients);


        final List<TemplateRole> participants = enveloppe.getTemplateRoles();
        assertEquals(2, participants.size());
        for (int i = 0; i < participants.size(); i++) {
            TemplateRole signer = participants.get(i);

            final List<SignHere> signHereTabs = signer.getTabs().getSignHereTabs();
            assertEquals(1, signHereTabs.size());
            final SignHere signHere = signHereTabs.get(0);
            assertEquals("1", signHere.getDocumentId());
            assertEquals(utilisateurs.get(i).getNom(), signHere.getAnchorString());

            assertTrue(Integer.valueOf(signHere.getAnchorXOffset()) > 0);
            assertEquals(0, Integer.valueOf(signHere.getAnchorYOffset()));

        }
      }

    @Test
    public void testEnveloppeSansSignataire() {

        final EnveloppeDocuSign enveloppeDocuSign = new EnveloppeDocuSign();
        EnvelopeDefinition enveloppe = enveloppeDocuSign.generer();

        assertNull(enveloppe.getRecipients());
//        final Recipients recipients = enveloppe.getRecipients();
//        assertNull(recipients.getCarbonCopies());
//
//        final List<Signer> participants = recipients.getSigners();
//        assertNull(recipients.getCarbonCopies());
    }

    @Test
    public void testInformationDuMail() {

        final String sujet = "Feuille d'émargement";
        final String emailBody = "Bonjour,\n   Merci de signer\nCordialement";

        final EnveloppeDocuSign enveloppeDocuSign = new EnveloppeDocuSign();
        enveloppeDocuSign.ajouterSignataire(utilisateur);
        enveloppeDocuSign.setEmailSujet(sujet);
        enveloppeDocuSign.setEmailContenu(emailBody);

        EnvelopeDefinition enveloppe = enveloppeDocuSign.generer();

        assertEquals(sujet, enveloppe.getEmailSubject());
        assertEquals(emailBody, enveloppe.getEmailBlurb());
    }

    @Test
    public void testDocument() {
        final EnveloppeDocuSign enveloppeDocuSign = new EnveloppeDocuSign();
        enveloppeDocuSign.setDocument("emargement.doc");

        EnvelopeDefinition enveloppe = enveloppeDocuSign.generer();

        final List<Document> documents = enveloppe.getDocuments();
        assertEquals(1, documents.size());
        final Document document = documents.get(0);
        assertEquals("1", document.getDocumentId());
        assertEquals("emargement.doc", document.getName());
        assertEquals("doc", document.getFileExtension());
    }
}
