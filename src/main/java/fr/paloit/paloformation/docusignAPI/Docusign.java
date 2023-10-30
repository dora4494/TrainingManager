package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;
import com.docusign.esign.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


@Service
@Profile("docusign")
public class Docusign {

    @Autowired
    DocuSignConfig config;

    static String DevCenterPage = "https://developers.docusign.com/platform/auth/consent";

    private final ApiClient apiClient;
    private String accessToken;
    private String accountId;

    public Docusign() {
        this(new ApiClient("https://demo.docusign.net/restapi"));
    }

    public Docusign(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.apiClient.setOAuthBasePath("account-d.docusign.com");
    }

    public Docusign(ApiClient apiClient, DocuSignConfig docuSignConfig) {
        this(apiClient);
        config = docuSignConfig;
    }

    public void envoyerEnveloppeTemplate(EnvelopeDefinition envelope) throws IOException {
        try {
            // TODO Revoir la récupération des template et du accountId
            String accountId = getAccountId();
            final TemplatesApi templatesApi = new TemplatesApi(apiClient);
            final EnvelopeTemplateResults envelopeTemplateResults = templatesApi.listTemplates(accountId);

            // TODO Récupération du premier template trouvé: à revoir.
            envelope.setTemplateId(envelopeTemplateResults.getEnvelopeTemplates().get(0).getTemplateId());
            EnvelopeSummary results = envoyerEnveloppe(envelope);

            System.out.println("Successfully sent envelope with envelopeId " + results.getEnvelopeId());
        } catch (ApiException apiException) {
            throw new RuntimeException("Exception sending envelop", apiException);
        } catch (Exception exception) {
            throw new RuntimeException("Unexpected exception sending envelop", exception);
        }
    }

    private String ajouterAccessTokenAuHeader(ApiClient apiClient) throws IOException {
        try {
            this.accessToken = getAccessToken(apiClient, config.getClientId(), config.getUserId(), config.getRsaKeyFile());
            System.out.println(accessToken);
            apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
            return accessToken;
        } catch (ApiException apiException) {
            if (apiException.getMessage().contains("consent_required")) {
                try {
                    System.out.println("Consent required, please provide consent in browser window and then run this app again.");
                    // TODO Il est probable que l'on fasse les choses différement sur l'application finale.
                    Desktop.getDesktop().browse(new URI("https://account-d.docusign.com/oauth/auth?response_type=code&scope=impersonation%20signature&client_id=" + config.getClientId() + "&redirect_uri=" + DevCenterPage));
                    return null;
                } catch (Exception desktopException) {
                    throw new RuntimeException("Exception while redirect to the browser for consent", desktopException);
                }
            } else {
                throw new RuntimeException("Exception setting access token", apiException);
            }
        }
    }

    public EnvelopeSummary envoyerEnveloppe(EnvelopeDefinition envelope) throws IOException, ApiException {
        String accountId = getAccountId();
        EnvelopeSummary results = envoyerEnveloppe(apiClient, accountId, envelope);
        return results;
    }

    protected EnvelopeSummary envoyerEnveloppe(ApiClient apiClient, String accountId, EnvelopeDefinition envelope) throws ApiException {
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        return envelopesApi.createEnvelope(accountId, envelope);
    }


    public String getAccountId() throws IOException, ApiException {
        if (accountId == null) {
            String accessToken = ajouterAccessTokenAuHeader(apiClient);
            accountId = getAccountId(apiClient, accessToken);
        }
        return accountId;
    }

    private String getAccountId(ApiClient apiClient, String accessToken) throws ApiException {
        OAuth.UserInfo userInfo = apiClient.getUserInfo(accessToken);
        return userInfo.getAccounts().get(0).getAccountId();
    }

    private String getAccessToken(ApiClient apiClient, String clientId, String userId, String rsaKeyFile) throws IOException, ApiException {
        ArrayList<String> scopes = new ArrayList<String>();
        scopes.add("signature");
        scopes.add("impersonation");
        byte[] privateKeyBytes = Files.readAllBytes(Paths.get(rsaKeyFile));
        OAuth.OAuthToken oAuthToken = apiClient.requestJWTUserToken(
                clientId,
                userId,
                scopes,
                privateKeyBytes,
                3600);
        return oAuthToken.getAccessToken();
    }


}