package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.Signer;
import fr.paloit.paloformation.model.Utilisateur;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EnveloppeDocuSignTest {
    final Utilisateur utilisateur = new Utilisateur(1L, "John", "Doe", "john.doe.test@palo-it.com");

    @Test
    public void testAjoutDUnSignataire() {
        final Utilisateur utilisateur = new Utilisateur(1L, "John", "Doe", "john.doe.test@palo-it.com");
        final EnveloppeDocuSign enveloppeDocuSign = new EnveloppeDocuSign();
        enveloppeDocuSign.ajouterSignataire(utilisateur);

        EnvelopeDefinition enveloppe = enveloppeDocuSign.generer();
//        EnvelopeDefinition enveloppe = Docusign.creerEnveloppe(utilisateur);

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

    @Test
    public void testEnveloppeSansSignataire() {

        final EnveloppeDocuSign enveloppeDocuSign = new EnveloppeDocuSign();
        EnvelopeDefinition enveloppe = enveloppeDocuSign.generer();

        final Recipients recipients = enveloppe.getRecipients();
        assertNull(recipients.getCarbonCopies());

        final List<Signer> participants = recipients.getSigners();
        assertNull(recipients.getCarbonCopies());
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
}
