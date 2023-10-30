package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.model.*;
import fr.paloit.paloformation.model.Utilisateur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.Base64;
import java.nio.file.Files;

public class EnveloppeDocuSign {
    private List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
    private String emailSujet;
    private String emailContenu;
    private String templateId;
    private String documentName;
    private String texteDocument;

    public void ajouterSignataire(Utilisateur utilisateur) {
        this.utilisateurs.add(utilisateur);
    }

    public void setDocument(String documentName) {
        setDocument(documentName, "Signature(s)");
    }

    public void setDocument(String documentName, String texteDocument) {
        this.documentName = documentName;
        this.texteDocument = texteDocument;
    }

    public EnvelopeDefinition generer() {

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setTemplateId(templateId);

        envelope.setEmailSubject(this.emailSujet);
        envelope.setEmailBlurb(this.emailContenu);
        envelope.setStatus("sent");

        // Liste des destinataires
        if (!utilisateurs.isEmpty()) {

            if (templateId == null) {
                // Création des zones de signature
                SignHere signHere = new SignHere();
                signHere.setDocumentId("1");
                signHere.setPageNumber("1");
                signHere.setXPosition("191");
                signHere.setYPosition("148");
                Tabs tabs = new Tabs();
                tabs.setSignHereTabs(Arrays.asList(signHere));

                Recipients recipients = new Recipients();
                recipients.setSigners(utilisateurs.stream()
                        .map(this::creerSignataireDocuSign)
                        .peek(signer -> signer.setTabs(tabs))
                        .collect(Collectors.toList()));
                envelope.setRecipients(recipients);
            } else {
                envelope.setTemplateRoles(utilisateurs.stream()
                        .map(utilisateur -> {
                            final TemplateRole templateRole = creerTemplateRole(utilisateur);
                            ajouterSignature(templateRole, utilisateur);
                            return templateRole;
                        })
                        .collect(Collectors.toList()));


                System.out.println("getTemplateRoles " + envelope.getTemplateRoles());
            }
        }

        if (documentName != null) {
            Document document = new Document();

            document.setDocumentBase64(Base64.getEncoder().encodeToString(texteDocument.getBytes()));
            //document.setDocumentBase64("VGhhbmtzIGZvciByZXZpZXdpbmcgdGhpcyEKCldlJ2xsIG1vdmUgZm9yd2FyZCBhcyBzb29uIGFzIHdlIGhlYXIgYmFjay4=");
            document.setName(documentName);
            document.setFileExtension(getExtension(documentName).orElse(null));
            document.setDocumentId("1");
            envelope.setDocuments(Arrays.asList(document));
        }

        return envelope;
    }

    private static String encodeFileToBase64(String document) {
        byte[] fileContent = document.getBytes();
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private static void ajouterSignature(TemplateRole templateRole, Utilisateur utilisateur) {
        SignHere signHere = new SignHere();
        signHere.setDocumentId("1");
        signHere.setPageNumber("1");
        signHere.setAnchorString(utilisateur.getNom());
        signHere.setAnchorUnits("pixels");
        signHere.setAnchorXOffset("100");
        signHere.setAnchorYOffset("0");
        signHere.setAnchorIgnoreIfNotPresent("true"); // Qu'est ce que cela fait si non trouvé ? Cela évite une erreur ?

        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(Arrays.asList(signHere));
        templateRole.setTabs(tabs);
    }

    private Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));

    }

    private static TemplateRole creerTemplateRole(Utilisateur utilisateur) {
        TemplateRole signer1 = new TemplateRole();
        signer1.setEmail(utilisateur.getMail());
        signer1.setName(utilisateur.getPrenom() + " " + utilisateur.getNom());
        signer1.setRoleName("signer");
        return signer1;
    }

    private Signer creerSignataireDocuSign(Utilisateur signataire) {
        Signer signer = new Signer();
        signer.setEmail(signataire.getMail());
        signer.setLastName(signataire.getNom());
        signer.setFirstName(signataire.getPrenom());
        signer.setName(signataire.getPrenom() + " " + signataire.getNom());
        signer.recipientId(signataire.getId().toString());
        return signer;
    }

    public void setEmailSujet(String sujet) {
        this.emailSujet = sujet;
    }

    public void setEmailContenu(String contenu) {
        this.emailContenu = contenu;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }


}
