package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.model.*;
import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.EmargementService;

import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class EnveloppeDocuSign {
    private List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
    private String emailSujet;
    private String emailContenu;
    private String templateId;
    private String nomDocument;
    private byte[] documentBytes;

    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public String getEmailSujet() {
        return emailSujet;
    }

    public String getEmailContenu() {
        return emailContenu;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getNomDocument() {
        return nomDocument;
    }

    public void ajouterSignataire(Utilisateur utilisateur) {
        this.utilisateurs.add(utilisateur);
    }

    public void setDocument(String documentName) {
        setDocument(documentName, "Signature(s)");
    }

    public void setDocument(String nomDocument, String texteDocument) {
        setDocument(nomDocument, texteDocument.getBytes());
    }

    public void setDocument(String nomDocument, byte[] documentBytes) {
        this.nomDocument = nomDocument;
        this.documentBytes = documentBytes;
    }

    public void setDocument(EmargementService.FeuilleEmargement feuilleEmargement) {
        setDocument(feuilleEmargement.getNomFichier(), feuilleEmargement.getBytes());
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


                Recipients recipients = new Recipients();
                recipients.setSigners(utilisateurs.stream()
                        .map(signataire -> {
                            final Signer signer = creerSignataireDocuSign(signataire);
                            ajouterSignature(signer, signataire);
                            return signer;
                        })
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

        if (nomDocument != null) {
            Document document = new Document();

            document.setDocumentBase64(Base64.getEncoder().encodeToString(this.documentBytes));
            document.setName(nomDocument);
            document.setFileExtension(getExtension(nomDocument).orElse(null));
            document.setDocumentId("1");
            envelope.setDocuments(Arrays.asList(document));
        }

        return envelope;
    }


    private static String encodeFileToBase64(String document) {
        byte[] fileContent = document.getBytes();
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));

    }

    private static void ajouterSignature(TemplateRole templateRole, Utilisateur utilisateur) {
        SignHere signHere = getSignHere(utilisateur);

        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(Arrays.asList(signHere));
        templateRole.setTabs(tabs);
    }

    private static void ajouterSignature(Signer signer, Utilisateur signataire) {
        SignHere signHere = getSignHere(signataire);

        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(Arrays.asList(signHere));
        signer.setTabs(tabs);
    }

    private static SignHere getSignHere(Utilisateur signataire) {
        SignHere signHere = new SignHere();
        signHere.setDocumentId("1");
        signHere.setPageNumber("1");
        // TODO cela ajoute une signature en face de chaque nom. S'il apparait plusieurs fois, il y a plusieurs demandes de signature !!!
        signHere.setAnchorString(signataire.getPrenom() + " " + signataire.getNom());
        signHere.setAnchorUnits("pixels");
        signHere.setAnchorXOffset("100");
        signHere.setAnchorYOffset("0");
        signHere.setAnchorIgnoreIfNotPresent("true"); // Qu'est ce que cela fait si non trouvé ? Cela évite une erreur ?
        return signHere;
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

    public byte[] getDocumentBytes() {
        return this.documentBytes;
    }
}
