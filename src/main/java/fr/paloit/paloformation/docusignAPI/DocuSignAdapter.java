package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;

import java.io.IOException;

public interface DocuSignAdapter {
    EnvelopeSummary envoyerEnveloppe(EnvelopeDefinition generer) throws IOException, ApiException;
}
