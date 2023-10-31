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
public class DocuSignMock extends DocuSign {
    Logger logger = LoggerFactory.getLogger(DocuSignMock.class);

    public DocuSignMock() {
    }

    public void envoyerEnveloppeTemplate(EnvelopeDefinition envelope) throws IOException {

    }

    public void envoyerEnveloppe(Utilisateur utilisateur) throws IOException {
        logger.debug("Envoi d'une demande d'émargement pour: "
                + utilisateur.getPrenom()
                + " " + utilisateur.getNom()
                + "(" + utilisateur.getMail() + ")");
    }

    private String ajouterAccessTokenAuHeader(ApiClient apiClient) throws IOException {
        return "";
    }

    public EnvelopeSummary envoyerEnveloppe(EnvelopeDefinition envelope) throws IOException, ApiException {
        return null;
    }

    protected EnvelopeSummary envoyerEnveloppe(ApiClient apiClient, String accountId, EnvelopeDefinition envelope) throws ApiException {
        return null;
    }


    public String getAccountId() throws IOException, ApiException {
        return null;
    }

    private String getAccountId(ApiClient apiClient, String accessToken) throws ApiException {
        return null;
    }

    private String getAccessToken(ApiClient apiClient, String clientId, String userId, String rsaKeyFile) throws IOException, ApiException {
        return null;
    }

    public static EnvelopeDefinition creerEnveloppe(Utilisateur utilisateur) {
        return null;
    }

}