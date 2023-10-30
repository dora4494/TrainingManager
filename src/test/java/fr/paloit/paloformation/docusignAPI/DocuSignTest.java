package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.Pair;
import com.docusign.esign.client.auth.OAuth;
import com.docusign.esign.model.*;
import fr.paloit.paloformation.model.Utilisateur;
import jakarta.ws.rs.core.GenericType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class DocuSignTest {

    @TempDir
    private Path tempDir;

    Path privateKeyFile = null;

    ApiClient mockApiClient = null;
    String myAccessToken = null;

    @BeforeEach
    public void creerFichierPrivateKey() throws IOException {
        privateKeyFile = tempDir.resolve("pseudo_private.key");
        privateKeyFile.toFile().createNewFile();
        assertTrue(Files.exists(privateKeyFile));
    }

    // Initialisation d'un bouchon pour apiClient.
    // Il permet de renvoyer des pseudo token d'accès et les infos utilisateurs.
    // Les appels d'api peuvent également être interceptés.
    @BeforeEach
    public void initMockApiClient() throws IOException, ApiException {
        mockApiClient = Mockito.spy(new ApiClient("https://demo.docusign.net/restapi"));
        myAccessToken = mockRequestJWTUserToken(mockApiClient);
        mockGetUserInfo(mockApiClient, myAccessToken);
    }

    @Test
    public void test_appel_authentification() throws ApiException, IOException {

        final Docusign docusign = new MockDocusign(mockApiClient);

//        final Utilisateur utilisateur = new Utilisateur(1L, "Sébastien", "Fauvel", "sfauvel@palo-it.com");
        final EnveloppeDocuSign enveloppeDocuSign = new EnveloppeDocuSign();
        docusign.envoyerEnveloppe(enveloppeDocuSign.generer());
        docusign.envoyerEnveloppe(enveloppeDocuSign.generer());

        // Vérification qu'il n'y ait qu'un seul appel pour récupérer les infos d'authentification
        Mockito.verify(mockApiClient).setOAuthBasePath("account-d.docusign.com");
        Mockito.verify(mockApiClient, Mockito.times(1)).setOAuthBasePath(Mockito.anyString());

        Mockito.verify(mockApiClient, Mockito.times(1)).requestJWTUserToken(
                Mockito.anyString(),Mockito.anyString(),Mockito.anyList(),Mockito.any(byte[].class),Mockito.anyLong());

        Mockito.verify(mockApiClient).addDefaultHeader("Authorization", "Bearer " + myAccessToken);
        Mockito.verify(mockApiClient, Mockito.times(1)).addDefaultHeader(Mockito.anyString(), Mockito.anyString());
    }


    /**
     * Ce test a servi dans une phase de refactoring pour s'assurer qu'il n'y avait pas de régression.
     * A voir s'il est toujours pertinent.
     */
    @Test
    public void testAppelApi() throws ApiException, IOException {

        final Utilisateur utilisateur = new Utilisateur(1L, "", "", "");
        final MockDocusign mockDocusign = new MockDocusign(mockApiClient);
        final EnvelopeDefinition envelopeDefinition = DocuSignService.creerEnveloppe(utilisateur).generer();
        mockDocusign.envoyerEnveloppe(envelopeDefinition);
        assertEquals(resultatDeReference(), mockDocusign.getFirstCall());
    }

    @Test
    public void testAjoutTemplateDansRequeteEmargement() throws ApiException, IOException {

        final Utilisateur utilisateur = new Utilisateur(1L, "", "", "");
        final MockDocusign mockDocusign = new MockDocusign(mockApiClient);

        EnvelopeTemplateResults envelopeTemplateResults = new EnvelopeTemplateResults();
        final EnvelopeTemplate envelopeTemplate = new EnvelopeTemplate();
        envelopeTemplate.setTemplateId("123456");
        envelopeTemplate.setName("MyTemplate");
        envelopeTemplateResults.addEnvelopeTemplatesItem(envelopeTemplate);

        mockDocusign.whenInvokePathEndsWith(invocation -> envelopeTemplateResults, "/templates");

        final EnveloppeDocuSign enveloppeDocuSign = new EnveloppeDocuSign();
        mockDocusign.envoyerEnveloppeTemplate(enveloppeDocuSign.generer());

        final String lastCall = mockDocusign.getLastCall();
        assertTrue(lastCall.contains("templateId: 123456"), "Valeur inattendu pour templateId: " + Arrays.stream(lastCall.split("\n")).filter(t -> t.contains("templateId:")).collect(Collectors.joining("\n")).trim()+ "\n");

    }




    // Outillage

    private static void mockGetUserInfo(ApiClient apiClient, String myAccessToken) throws ApiException {
        OAuth.Account account = new OAuth.Account().accountId("my_account_id");
        OAuth.UserInfo userInfo = new OAuth.UserInfo().addAccountsItem(account);
        Mockito.doReturn(userInfo).when(apiClient).getUserInfo(myAccessToken);
    }

    private static String mockRequestJWTUserToken(ApiClient apiClient) throws ApiException, IOException {
        final String myAccessToken = "my_token";
        OAuth.OAuthToken oAuthToken = new OAuth.OAuthToken().accessToken(myAccessToken);
        Mockito.doReturn(oAuthToken).when(apiClient).requestJWTUserToken(Mockito.anyString(),Mockito.anyString(),Mockito.anyList(),Mockito.any(byte[].class),Mockito.anyLong());
        return myAccessToken;
    }

    private class CallRecorder {

        private final List<String> calls = new ArrayList<>();

        public List<String> getCalls() {
            return calls;
        }

        private String getFirstCall() {
            return getCalls().get(0);
        }
        private String getLastCall() {
            final List<String> calls = getCalls();
            return calls.get(calls.size()-1);
        }
    }
    /**
     * Intercepte les appels à invokeAPI et retourne une enveloppe vide.
     */
    private static class DocSignInvokeAPIAnswer implements Answer<Object> {

        private final CallRecorder recorder;
        private final Function<InvocationOnMock, Object> responseBuilder;

        public DocSignInvokeAPIAnswer(CallRecorder recorder, Function<InvocationOnMock, Object> responseBuilder) {
            this.recorder = recorder;
            this.responseBuilder = responseBuilder;
        }
        
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            int index = 0;
            String path = invocation.getArgument(index++);
            String method = invocation.getArgument(index++);
            List<Pair> queryParams = invocation.getArgument(index++);
            List<Pair> collectionQueryParams = invocation.getArgument(index++);
            Object body = invocation.getArgument(index++);
            Map<String, String> headerParams = invocation.getArgument(index++);
            Map<String, Object> formParams = invocation.getArgument(index++);
            String accept = invocation.getArgument(index++);
            String contentType = invocation.getArgument(index++);
            String[] authNames = invocation.getArgument(index++);
            GenericType<Object> returnType = invocation.getArgument(index++);

            String result = "";
            result += "DocuSignTest.answer" + "\n";
            result += "path:" + path + "\n";
            result += "method:" + method + "\n";
            result += "queryParams:" + queryParams + "\n";
            result += "collectionQueryParams:" + collectionQueryParams + "\n";
            result += "body:" + body + "\n";
//            final EnvelopeDefinition body1 = (EnvelopeDefinition) body;
//            body1.toString();
            result += "headerParams:" + headerParams + "\n";
            result += "formParams:" + formParams + "\n";
            result += "accept:" + accept + "\n";
            result += "contentType:" + contentType + "\n";
            result += "authNames:" + Arrays.toString(authNames) + "\n";
            result += "returnType:" + returnType + "\n";
            recorder.calls.add(result);

            System.out.println("InvokeAPI is mocked. An empty EnvelopeSummary is returned");
            return responseBuilder.apply(invocation);
        }

    }

    // Cette référence a été enregistrer lors d'un premier appel pour avoir un élément de comparaison pendant la phase de réfactoring.
    // Il n'est pas sûre qu'il faille la garder
    public String resultatDeReference() {
        return "DocuSignTest.answer\n" +
                "path:/v2.1/accounts/my_account_id/envelopes\n" +
                "method:POST\n" +
                "queryParams:[]\n" +
                "collectionQueryParams:[]\n" +
                "body:class EnvelopeDefinition {\n" +
                "    accessControlListBase64: null\n" +
                "    accessibility: null\n" +
                "    allowComments: null\n" +
                "    allowMarkup: null\n" +
                "    allowReassign: null\n" +
                "    allowRecipientRecursion: null\n" +
                "    allowViewHistory: null\n" +
                "    anySigner: null\n" +
                "    asynchronous: null\n" +
                "    attachments: null\n" +
                "    attachmentsUri: null\n" +
                "    authoritativeCopy: null\n" +
                "    authoritativeCopyDefault: null\n" +
                "    autoNavigation: null\n" +
                "    brandId: null\n" +
                "    brandLock: null\n" +
                "    burnDefaultTabData: null\n" +
                "    certificateUri: null\n" +
                "    completedDateTime: null\n" +
                "    compositeTemplates: null\n" +
                "    copyRecipientData: null\n" +
                "    createdDateTime: null\n" +
                "    customFields: null\n" +
                "    customFieldsUri: null\n" +
                "    declinedDateTime: null\n" +
                "    deletedDateTime: null\n" +
                "    deliveredDateTime: null\n" +
                "    disableResponsiveDocument: null\n" +
                "    documentBase64: null\n" +
                "    documents: [class Document {\n" +
                "        applyAnchorTabs: null\n" +
                "        assignTabsToRecipientId: null\n" +
                "        display: null\n" +
                "        docGenFormFields: null\n" +
                "        documentBase64: U2lnbmF0dXJlKHMp\n" +
                "        documentFields: null\n" +
                "        documentId: 1\n" +
                "        encryptedWithKeyManager: null\n" +
                "        fileExtension: txt\n" +
                "        fileFormatHint: null\n" +
                "        htmlDefinition: null\n" +
                "        includeInDownload: null\n" +
                "        isDocGenDocument: null\n" +
                "        matchBoxes: null\n" +
                "        name: doc1.txt\n" +
                "        order: null\n" +
                "        pages: null\n" +
                "        password: null\n" +
                "        pdfFormFieldOption: null\n" +
                "        remoteUrl: null\n" +
                "        signerMustAcknowledge: null\n" +
                "        signerMustAcknowledgeUseAccountDefault: null\n" +
                "        tabs: null\n" +
                "        templateLocked: null\n" +
                "        templateRequired: null\n" +
                "        transformPdfFields: null\n" +
                "        uri: null\n" +
                "    }]\n" +
                "    documentsCombinedUri: null\n" +
                "    documentsUri: null\n" +
                "    emailBlurb: null\n" +
                "    emailSettings: null\n" +
                "    emailSubject: Feuille d'émargement\n" +
                "    enableWetSign: null\n" +
                "    enforceSignerVisibility: null\n" +
                "    envelopeAttachments: null\n" +
                "    envelopeCustomMetadata: null\n" +
                "    envelopeDocuments: null\n" +
                "    envelopeId: null\n" +
                "    envelopeIdStamping: null\n" +
                "    envelopeLocation: null\n" +
                "    envelopeMetadata: null\n" +
                "    envelopeUri: null\n" +
                "    eventNotification: null\n" +
                "    eventNotifications: null\n" +
                "    expireAfter: null\n" +
                "    expireDateTime: null\n" +
                "    expireEnabled: null\n" +
                "    externalEnvelopeId: null\n" +
                "    folders: null\n" +
                "    hasComments: null\n" +
                "    hasFormDataChanged: null\n" +
                "    hasWavFile: null\n" +
                "    holder: null\n" +
                "    initialSentDateTime: null\n" +
                "    is21CFRPart11: null\n" +
                "    isDynamicEnvelope: null\n" +
                "    isSignatureProviderEnvelope: null\n" +
                "    lastModifiedDateTime: null\n" +
                "    location: null\n" +
                "    lockInformation: null\n" +
                "    messageLock: null\n" +
                "    notification: null\n" +
                "    notificationUri: null\n" +
                "    password: null\n" +
                "    powerForm: null\n" +
                "    purgeCompletedDate: null\n" +
                "    purgeRequestDate: null\n" +
                "    purgeState: null\n" +
                "    recipients: class Recipients {\n" +
                "        agents: null\n" +
                "        carbonCopies: null\n" +
                "        certifiedDeliveries: null\n" +
                "        currentRoutingOrder: null\n" +
                "        editors: null\n" +
                "        errorDetails: null\n" +
                "        inPersonSigners: null\n" +
                "        intermediaries: null\n" +
                "        notaries: null\n" +
                "        participants: null\n" +
                "        recipientCount: null\n" +
                "        seals: null\n" +
                "        signers: [class Signer {\n" +
                "            accessCode: null\n" +
                "            accessCodeMetadata: null\n" +
                "            addAccessCodeToEmail: null\n" +
                "            additionalNotifications: null\n" +
                "            agentCanEditEmail: null\n" +
                "            agentCanEditName: null\n" +
                "            allowSystemOverrideForLockedRecipient: null\n" +
                "            autoNavigation: null\n" +
                "            autoRespondedReason: null\n" +
                "            bulkRecipientsUri: null\n" +
                "            bulkSendV2Recipient: null\n" +
                "            canSignOffline: null\n" +
                "            clientUserId: null\n" +
                "            completedCount: null\n" +
                "            consentDetailsList: null\n" +
                "            creationReason: null\n" +
                "            customFields: null\n" +
                "            declinedDateTime: null\n" +
                "            declinedReason: null\n" +
                "            defaultRecipient: null\n" +
                "            delegatedBy: null\n" +
                "            delegatedTo: null\n" +
                "            deliveredDateTime: null\n" +
                "            deliveryMethod: null\n" +
                "            deliveryMethodMetadata: null\n" +
                "            designatorId: null\n" +
                "            designatorIdGuid: null\n" +
                "            documentVisibility: null\n" +
                "            email: \n" +
                "            emailMetadata: null\n" +
                "            emailNotification: null\n" +
                "            emailRecipientPostSigningURL: null\n" +
                "            embeddedRecipientStartURL: null\n" +
                "            errorDetails: null\n" +
                "            excludedDocuments: null\n" +
                "            faxNumber: null\n" +
                "            faxNumberMetadata: null\n" +
                "            firstName: \n" +
                "            firstNameMetadata: null\n" +
                "            fullName: null\n" +
                "            fullNameMetadata: null\n" +
                "            idCheckConfigurationName: null\n" +
                "            idCheckConfigurationNameMetadata: null\n" +
                "            idCheckInformationInput: null\n" +
                "            identityVerification: null\n" +
                "            inheritEmailNotificationConfiguration: null\n" +
                "            isBulkRecipient: null\n" +
                "            isBulkRecipientMetadata: null\n" +
                "            lastName: \n" +
                "            lastNameMetadata: null\n" +
                "            lockedRecipientPhoneAuthEditable: null\n" +
                "            lockedRecipientSmsEditable: null\n" +
                "            name:  \n" +
                "            nameMetadata: null\n" +
                "            notaryId: null\n" +
                "            notarySignerEmailSent: null\n" +
                "            note: null\n" +
                "            noteMetadata: null\n" +
                "            offlineAttributes: null\n" +
                "            phoneAuthentication: null\n" +
                "            phoneNumber: null\n" +
                "            proofFile: null\n" +
                "            recipientAttachments: null\n" +
                "            recipientAuthenticationStatus: null\n" +
                "            recipientFeatureMetadata: null\n" +
                "            recipientId: 1\n" +
                "            recipientIdGuid: null\n" +
                "            recipientSignatureProviders: null\n" +
                "            recipientSuppliesTabs: null\n" +
                "            recipientType: null\n" +
                "            recipientTypeMetadata: null\n" +
                "            requireIdLookup: null\n" +
                "            requireIdLookupMetadata: null\n" +
                "            requireSignerCertificate: null\n" +
                "            requireSignOnPaper: null\n" +
                "            requireUploadSignature: null\n" +
                "            roleName: null\n" +
                "            routingOrder: null\n" +
                "            routingOrderMetadata: null\n" +
                "            sentDateTime: null\n" +
                "            signatureInfo: null\n" +
                "            signedDateTime: null\n" +
                "            signInEachLocation: null\n" +
                "            signInEachLocationMetadata: null\n" +
                "            signingGroupId: null\n" +
                "            signingGroupIdMetadata: null\n" +
                "            signingGroupName: null\n" +
                "            signingGroupUsers: null\n" +
                "            smsAuthentication: null\n" +
                "            socialAuthentications: null\n" +
                "            status: null\n" +
                "            statusCode: null\n" +
                "            suppressEmails: null\n" +
                "            tabs: class Tabs {\n" +
                "                approveTabs: null\n" +
                "                checkboxTabs: null\n" +
                "                commentThreadTabs: null\n" +
                "                commissionCountyTabs: null\n" +
                "                commissionExpirationTabs: null\n" +
                "                commissionNumberTabs: null\n" +
                "                commissionStateTabs: null\n" +
                "                companyTabs: null\n" +
                "                dateSignedTabs: null\n" +
                "                dateTabs: null\n" +
                "                declineTabs: null\n" +
                "                drawTabs: null\n" +
                "                emailAddressTabs: null\n" +
                "                emailTabs: null\n" +
                "                envelopeIdTabs: null\n" +
                "                firstNameTabs: null\n" +
                "                formulaTabs: null\n" +
                "                fullNameTabs: null\n" +
                "                initialHereTabs: null\n" +
                "                lastNameTabs: null\n" +
                "                listTabs: null\n" +
                "                notarizeTabs: null\n" +
                "                notarySealTabs: null\n" +
                "                noteTabs: null\n" +
                "                numberTabs: null\n" +
                "                numericalTabs: null\n" +
                "                phoneNumberTabs: null\n" +
                "                polyLineOverlayTabs: null\n" +
                "                prefillTabs: null\n" +
                "                radioGroupTabs: null\n" +
                "                signerAttachmentTabs: null\n" +
                "                signHereTabs: [class SignHere {\n" +
                "                    anchorAllowWhiteSpaceInCharacters: null\n" +
                "                    anchorAllowWhiteSpaceInCharactersMetadata: null\n" +
                "                    anchorCaseSensitive: null\n" +
                "                    anchorCaseSensitiveMetadata: null\n" +
                "                    anchorHorizontalAlignment: null\n" +
                "                    anchorHorizontalAlignmentMetadata: null\n" +
                "                    anchorIgnoreIfNotPresent: null\n" +
                "                    anchorIgnoreIfNotPresentMetadata: null\n" +
                "                    anchorMatchWholeWord: null\n" +
                "                    anchorMatchWholeWordMetadata: null\n" +
                "                    anchorString: null\n" +
                "                    anchorStringMetadata: null\n" +
                "                    anchorTabProcessorVersion: null\n" +
                "                    anchorTabProcessorVersionMetadata: null\n" +
                "                    anchorUnits: null\n" +
                "                    anchorUnitsMetadata: null\n" +
                "                    anchorXOffset: null\n" +
                "                    anchorXOffsetMetadata: null\n" +
                "                    anchorYOffset: null\n" +
                "                    anchorYOffsetMetadata: null\n" +
                "                    caption: null\n" +
                "                    captionMetadata: null\n" +
                "                    conditionalParentLabel: null\n" +
                "                    conditionalParentLabelMetadata: null\n" +
                "                    conditionalParentValue: null\n" +
                "                    conditionalParentValueMetadata: null\n" +
                "                    customTabId: null\n" +
                "                    customTabIdMetadata: null\n" +
                "                    documentId: 1\n" +
                "                    documentIdMetadata: null\n" +
                "                    errorDetails: null\n" +
                "                    formOrder: null\n" +
                "                    formOrderMetadata: null\n" +
                "                    formPageLabel: null\n" +
                "                    formPageLabelMetadata: null\n" +
                "                    formPageNumber: null\n" +
                "                    formPageNumberMetadata: null\n" +
                "                    handDrawRequired: null\n" +
                "                    height: null\n" +
                "                    heightMetadata: null\n" +
                "                    isSealSignTab: null\n" +
                "                    mergeField: null\n" +
                "                    mergeFieldXml: null\n" +
                "                    name: null\n" +
                "                    nameMetadata: null\n" +
                "                    optional: null\n" +
                "                    optionalMetadata: null\n" +
                "                    pageNumber: 1\n" +
                "                    pageNumberMetadata: null\n" +
                "                    recipientId: null\n" +
                "                    recipientIdGuid: null\n" +
                "                    recipientIdGuidMetadata: null\n" +
                "                    recipientIdMetadata: null\n" +
                "                    scaleValue: null\n" +
                "                    scaleValueMetadata: null\n" +
                "                    smartContractInformation: null\n" +
                "                    source: null\n" +
                "                    stamp: null\n" +
                "                    stampType: null\n" +
                "                    stampTypeMetadata: null\n" +
                "                    status: null\n" +
                "                    statusMetadata: null\n" +
                "                    tabGroupLabels: null\n" +
                "                    tabGroupLabelsMetadata: null\n" +
                "                    tabId: null\n" +
                "                    tabIdMetadata: null\n" +
                "                    tabLabel: null\n" +
                "                    tabLabelMetadata: null\n" +
                "                    tabOrder: null\n" +
                "                    tabOrderMetadata: null\n" +
                "                    tabType: null\n" +
                "                    tabTypeMetadata: null\n" +
                "                    templateLocked: null\n" +
                "                    templateLockedMetadata: null\n" +
                "                    templateRequired: null\n" +
                "                    templateRequiredMetadata: null\n" +
                "                    tooltip: null\n" +
                "                    toolTipMetadata: null\n" +
                "                    width: null\n" +
                "                    widthMetadata: null\n" +
                "                    xPosition: 191\n" +
                "                    xPositionMetadata: null\n" +
                "                    yPosition: 148\n" +
                "                    yPositionMetadata: null\n" +
                "                }]\n" +
                "                smartSectionTabs: null\n" +
                "                ssnTabs: null\n" +
                "                tabGroups: null\n" +
                "                textTabs: null\n" +
                "                titleTabs: null\n" +
                "                viewTabs: null\n" +
                "                zipTabs: null\n" +
                "            }\n" +
                "            templateLocked: null\n" +
                "            templateRequired: null\n" +
                "            totalTabCount: null\n" +
                "            userId: null\n" +
                "        }]\n" +
                "        witnesses: null\n" +
                "    }\n" +
                "    recipientsLock: null\n" +
                "    recipientsUri: null\n" +
                "    recipientViewRequest: null\n" +
                "    sender: null\n" +
                "    sentDateTime: null\n" +
                "    signerCanSignOnMobile: null\n" +
                "    signingLocation: null\n" +
                "    status: sent\n" +
                "    statusChangedDateTime: null\n" +
                "    statusDateTime: null\n" +
                "    templateId: null\n" +
                "    templateRoles: null\n" +
                "    templatesUri: null\n" +
                "    transactionId: null\n" +
                "    useDisclosure: null\n" +
                "    voidedDateTime: null\n" +
                "    voidedReason: null\n" +
                "    workflow: null\n" +
                "}\n" +
                "headerParams:{}\n" +
                "formParams:{}\n" +
                "accept:application/json\n" +
                "contentType:application/json\n" +
                "authNames:[docusignAccessCode]\n" +
                "returnType:GenericType{class com.docusign.esign.model.EnvelopeSummary}\n";
    }

    /**
     * Redéfinition de la classe Docusign pour les tests.
     * Le fichier de configuration n'est pas chargé mais remplacé par des valeurs en dur.
     * Les appels à invokeAPI sont interceptés pour renvoyer une enveloppe vide et enregistrer les valeurs envoyées.
     */
    private class MockDocusign extends Docusign {

        final CallRecorder recorder = new CallRecorder();
        private final ApiClient apiClient;

        public MockDocusign(ApiClient apiClient) throws ApiException {
            super(apiClient, new DocuSignConfig("clientId", "userId", privateKeyFile.toString()));
            this.apiClient = apiClient;

            mockDocSignInvokeAPI(invocation -> new EnvelopeSummary());
        }

        public void mockDocSignInvokeAPI(Function<InvocationOnMock, Object> responseBuilder) throws ApiException {
            final DocSignInvokeAPIAnswer answer = new DocSignInvokeAPIAnswer(recorder, responseBuilder);
            Mockito.doAnswer(answer)
                    .when(apiClient).invokeAPI(
                            Mockito.anyString(), // path,
                            Mockito.anyString(), // method,
                            Mockito.anyList(), // queryParams,
                            Mockito.anyList(), // collectionQueryParams,
                            Mockito.any(), // body,
                            Mockito.anyMap(), // eaderParams,
                            Mockito.anyMap(), // formParams,
                            Mockito.anyString(), // accept,
                            Mockito.anyString(), // contentType,
                            Mockito.any(), // String[] authNames,
                            Mockito.any() // returnType)
                    );
        }

        private void whenInvokePathEndsWith(Function<InvocationOnMock, Object> responseBuilder, String suffix) throws ApiException {
            final DocSignInvokeAPIAnswer answer = new DocSignInvokeAPIAnswer(recorder, responseBuilder);
            Mockito.doAnswer(answer)
                    .when(apiClient).invokeAPI(
                    Mockito.endsWith(suffix), // path,
                    Mockito.anyString(), // method,
                    Mockito.anyList(), // queryParams,
                    Mockito.anyList(), // collectionQueryParams,
                    Mockito.any(), // body,
                    Mockito.anyMap(), // eaderParams,
                    Mockito.anyMap(), // formParams,
                    Mockito.anyString(), // accept,
                    Mockito.anyString(), // contentType,
                    Mockito.any(), // String[] authNames,
                    Mockito.any() // returnType
            );
        }

        private String getFirstCall() {
            return recorder.getFirstCall();
        }
        private String getLastCall() {
            return recorder.getLastCall();
        }

    }
}
