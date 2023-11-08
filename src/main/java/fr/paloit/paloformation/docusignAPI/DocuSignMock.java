package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import fr.paloit.paloformation.model.Utilisateur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@Profile({"default", "!docusign"})
public class DocuSignMock implements DocuSignAdapter {
    Logger logger = LoggerFactory.getLogger(DocuSignMock.class);

    @Override
    public EnvelopeSummary envoyerEnveloppe(EnvelopeDefinition envelope) throws IOException, ApiException {
        return null;
    }


}