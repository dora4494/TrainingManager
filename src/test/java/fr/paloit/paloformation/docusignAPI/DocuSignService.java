package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.Signer;
import fr.paloit.paloformation.model.Utilisateur;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DocuSignService {


    @Test
    public void testCreerEnveloppe() {

        final Utilisateur utilisateur = new Utilisateur(1L, "John", "Doe", "john.doe.test@palo-it.com");
        EnvelopeDefinition enveloppe = DocusignService.creerEnveloppe(utilisateur).generer();

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
