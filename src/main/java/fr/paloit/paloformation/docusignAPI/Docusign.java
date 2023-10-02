package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;
import com.docusign.esign.model.*;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;


public class Docusign {


//
//
//   public InformationsUtilisateur infosUtilisateur() {
//      Scanner scanner = new Scanner(System.in);
//      System.out.println("Welcome to the JWT Code example! ");
//      System.out.print("Enter the signer's email address: ");
//      String signerEmail = scanner.nextLine();
//      System.out.print("Enter the signer's name: ");
//      String signerName = scanner.nextLine();
//      System.out.print("Enter the carbon copy's email address: ");
//      String ccEmail = scanner.nextLine();
//      System.out.print("Enter the carbon copy's name: ");
//      String ccNom = scanner.nextLine();
//
//      return new InformationsUtilisateur(signerEmail, signerName, ccEmail, ccNom);
//   }
//
//public void envoyerEnveloppe() throws IOException {
//   Properties prop = new Properties();
//   String fileName = "app.config";
//   FileInputStream fis = new FileInputStream(fileName);
//  prop.load(fis);
//
// try {
//      // Get access token and accountId
//      ApiClient apiClient = new ApiClient("https://demo.docusign.net/restapi");
//      apiClient.setOAuthBasePath("account-d.docusign.com");
//      ArrayList<String> scopes = new ArrayList<String>();
//      scopes.add("signature");
//      scopes.add("impersonation");
//      System.out.println("bbbb");
//      byte[] privateKeyBytes = Files.readAllBytes(Paths.get(prop.getProperty("rsaKeyFile")));
//      System.out.println("cccc");
//      OAuth.OAuthToken oAuthToken = apiClient.requestJWTUserToken(
//              prop.getProperty("clientId"),
//              prop.getProperty("userId"),
//              scopes,
//              privateKeyBytes,
//              3600);
//      String accessToken = oAuthToken.getAccessToken();
//      System.out.println(accessToken);
//      OAuth.UserInfo userInfo = apiClient.getUserInfo(accessToken);
//      String accountId = userInfo.getAccounts().get(0).getAccountId();
//
//      // Create envelopeDefinition object
//      EnvelopeDefinition envelope = new EnvelopeDefinition();
//      envelope.setEmailSubject("Please sign this document set");
//      envelope.setStatus("sent");
//
//      // Create tabs object
//      SignHere signHere = new SignHere();
//      signHere.setDocumentId("1");
//      signHere.setPageNumber("1");
//      signHere.setXPosition("191");
//      signHere.setYPosition("148");
//      Tabs tabs = new Tabs();
//      tabs.setSignHereTabs(Arrays.asList(signHere));
//      // Set recipients
//      Signer signer = new Signer();
//      signer.setEmail(result.signerEmail);
//      signer.setName(result.signerName);
//      signer.recipientId("1");
//      signer.setTabs(tabs);
//            /*CarbonCopy cc = new CarbonCopy();
//            cc.setEmail(ccEmail);
//            cc.setName(ccName);
//            cc.recipientId("2");*/
//      Recipients recipients = new Recipients();
//      recipients.setSigners(Arrays.asList(signer));
//      //recipients.setCarbonCopies(Arrays.asList(cc));
//      envelope.setRecipients(recipients);
//
//      // Add document
//      Document document = new Document();
//      document.setDocumentBase64("VGhhbmtzIGZvciByZXZpZXdpbmcgdGhpcyEKCldlJ2xsIG1vdmUgZm9yd2FyZCBhcyBzb29uIGFzIHdlIGhlYXIgYmFjay4=");
//      document.setName("doc1.txt");
//      document.setFileExtension("txt");
//      document.setDocumentId("1");
//      envelope.setDocuments(Arrays.asList(document));
//
//      // Send envelope
//      apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
//      EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
//      EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelope);
//      System.out.println("Successfully sent envelope with envelopeId " + results.getEnvelopeId());
//   } catch (
//   ApiException exp) {
//      if (exp.getMessage().contains("consent_required")) {
//         try {
//            System.out.println("Consent required, please provide consent in browser window and then run this app again.");
//            Desktop.getDesktop().browse(new URI("https://account-d.docusign.com/oauth/auth?response_type=code&scope=impersonation%20signature&client_id=" + prop.getProperty("clientId") + "&redirect_uri=" + DevCenterPage));
//         } catch (Exception e) {
//            System.out.print("Error1!!!  ");
//            System.out.print(e.getMessage());
//         }
//      } else {
//         System.out.print("Error2!!!  ");
//         System.out.print(exp.getMessage());
//      }
//   } catch (Exception e) {
//      System.out.print("Error3!!!  ");
//      System.out.print(e.getMessage());
//   }
//
//
//}
//
//
//
//
//
//
//
//}
//
//class InformationsUtilisateur {
//   private String signerEmail;
//   private String signerName;
//   private String ccEmail;
//   private String ccNom;
//
//   public InformationsUtilisateur(String signerEmail, String signerName, String ccEmail, String ccNom) {
//      this.signerEmail = signerEmail;
//      this.signerName = signerName;
//      this.ccEmail = ccEmail;
//      this.ccNom = ccNom;
//   }
//
//   public String getSignerEmail() {
//      return signerEmail;
//   }
//
//   public void setSignerEmail(String signerEmail) {
//      this.signerEmail = signerEmail;
//   }
//
//   public String getSignerName() {
//      return signerName;
//   }
//
//   public void setSignerName(String signerName) {
//      this.signerName = signerName;
//   }
//
//   public String getCcEmail() {
//      return ccEmail;
//   }
//
//   public void setCcEmail(String ccEmail) {
//      this.ccEmail = ccEmail;
//   }
//
//   public String getCcNom() {
//      return ccNom;
//   }
//
//   public void setCcNom(String ccNom) {
//      this.ccNom = ccNom;
//   }


}
