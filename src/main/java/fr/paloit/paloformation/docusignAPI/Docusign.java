package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;
import com.docusign.esign.model.*;
import fr.paloit.paloformation.model.Utilisateur;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;


@Component
public class Docusign {

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

    public void envoyerEnveloppe(Utilisateur utilisateur) throws IOException {
        try {
            EnvelopeDefinition envelope = creerEnveloppe(utilisateur);
            EnvelopeSummary results = envoyerEnveloppe(envelope);

            System.out.println("Successfully sent envelope with envelopeId " + results.getEnvelopeId());
        } catch (ApiException apiException) {
            throw new RuntimeException("Exception sending envelop", apiException);
        } catch (Exception exception) {
            throw new RuntimeException("Unexpected exception sending envelop", exception);
        }
    }

    private String ajouterAccessTokenAuHeader(ApiClient apiClient) throws IOException {
        Properties prop = loadConfig();
        String clientId = prop.getProperty("clientId");
        try {
            this.accessToken = getAccessToken(prop, apiClient);
            System.out.println(accessToken);
            apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
            return accessToken;
        } catch (ApiException apiException) {
            if (apiException.getMessage().contains("consent_required")) {
                try {
                    System.out.println("Consent required, please provide consent in browser window and then run this app again.");
                    // TODO Il est probable que l'on fasse les choses différement sur l'application finale.
                    Desktop.getDesktop().browse(new URI("https://account-d.docusign.com/oauth/auth?response_type=code&scope=impersonation%20signature&client_id=" + clientId + "&redirect_uri=" + DevCenterPage));
                    return null;
                } catch (Exception desktopException) {
                    throw new RuntimeException("Exception while redirect to the browser for consent", desktopException);
                }
            } else {
                throw new RuntimeException("Exception setting access token", apiException);
            }
        }
    }

    protected Properties loadConfig() throws IOException {
        Properties prop = new Properties();
        String fileName = "app.config";
        FileInputStream fis = new FileInputStream(fileName);
        prop.load(fis);
        return prop;
    }


    protected EnvelopeSummary envoyerEnveloppe(EnvelopeDefinition envelope) throws IOException, ApiException {
        String accountId = getAccountId();
        EnvelopeSummary results = envoyerEnveloppe(apiClient, accountId, envelope);
        return results;
    }

    protected EnvelopeSummary envoyerEnveloppe(ApiClient apiClient, String accountId, EnvelopeDefinition envelope) throws ApiException {
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        return envelopesApi.createEnvelope(accountId, envelope);
    }


    private String getAccountId() throws IOException, ApiException {
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

    private String getAccessToken(Properties prop, ApiClient apiClient) throws IOException, ApiException {
        ArrayList<String> scopes = new ArrayList<String>();
        scopes.add("signature");
        scopes.add("impersonation");
        byte[] privateKeyBytes = Files.readAllBytes(Paths.get(prop.getProperty("rsaKeyFile")));
        OAuth.OAuthToken oAuthToken = apiClient.requestJWTUserToken(
                prop.getProperty("clientId"),
                prop.getProperty("userId"),
                scopes,
                privateKeyBytes,
                3600);
        return oAuthToken.getAccessToken();
    }

    public static EnvelopeDefinition creerEnveloppe(Utilisateur utilisateur) {
        // Create envelopeDefinition object
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please sign this document set");
        envelope.setStatus("sent");
        System.out.println("Create envelopeDefinition");
        // Create tabs object
        SignHere signHere = new SignHere();
        signHere.setDocumentId("1");
        signHere.setPageNumber("1");
        signHere.setXPosition("191");
        signHere.setYPosition("148");
        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(Arrays.asList(signHere));
        System.out.println("Create tabs object");

        // Set recipients
        Signer signer = new Signer();
        signer.setEmail(utilisateur.getMail());
        System.out.println(signer.getEmail());
        signer.setLastName(utilisateur.getNom());
        signer.setFirstName(utilisateur.getPrenom());
        signer.setName(utilisateur.getPrenom() + " " + utilisateur.getNom());
        signer.recipientId(utilisateur.getId().toString());
        signer.setTabs(tabs);
        System.out.println("etape 2");
            /*CarbonCopy cc = new CarbonCopy();
            cc.setEmail(ccEmail);
            cc.setName(ccName);
            cc.recipientId("2");*/

        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer));
        //recipients.setCarbonCopies(Arrays.asList(cc));
        envelope.setRecipients(recipients);
        System.out.println("recipients");
        // Add document
        Document document = new Document();
        document.setDocumentBase64("VGhhbmtzIGZvciByZXZpZXdpbmcgdGhpcyEKCldlJ2xsIG1vdmUgZm9yd2FyZCBhcyBzb29uIGFzIHdlIGhlYXIgYmFjay4=");
        document.setName("doc1.txt");
        document.setFileExtension("txt");
        document.setDocumentId("1");
        envelope.setDocuments(Arrays.asList(document));
        System.out.println("document");
        return envelope;
    }

}