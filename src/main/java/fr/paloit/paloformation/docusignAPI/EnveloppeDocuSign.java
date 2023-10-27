package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.model.*;
import fr.paloit.paloformation.model.Utilisateur;

import java.util.Arrays;

public class EnveloppeDocuSign {
    private Utilisateur utilisateur;
    private String emailSujet;
    private String emailContenu;

    public void ajouterSignataire(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public EnvelopeDefinition generer() {

        EnvelopeDefinition envelope = new EnvelopeDefinition();

        envelope.setEmailSubject(this.emailSujet);
        envelope.setEmailBlurb(this.emailContenu);
        envelope.setStatus("sent");

        // Création des zones de signature
        SignHere signHere = new SignHere();
        signHere.setDocumentId("1");
        signHere.setPageNumber("1");
        signHere.setXPosition("191");
        signHere.setYPosition("148");
        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(Arrays.asList(signHere));
        System.out.println("Create tabs object");

        // Liste des destinataires

        Recipients recipients = new Recipients();
        if (utilisateur != null) {
            Signer signer = creerSignataireDocuSign(utilisateur);
            signer.setTabs(tabs);
            recipients.addSignersItem(signer);
        }
            /*CarbonCopy cc = new CarbonCopy();
            cc.setEmail(ccEmail);
            cc.setName(ccName);
            cc.recipientId("2");*/

        //recipients.setCarbonCopies(Arrays.asList(cc));
        envelope.setRecipients(recipients);

        // Création du document
        Document document = new Document();

        document.setDocumentBase64("VGhhbmtzIGZvciByZXZpZXdpbmcgdGhpcyEKCldlJ2xsIG1vdmUgZm9yd2FyZCBhcyBzb29uIGFzIHdlIGhlYXIgYmFjay4=");
        document.setName("doc1.txt");
        document.setFileExtension("txt");
        document.setDocumentId("1");
        envelope.setDocuments(Arrays.asList(document));


        System.out.println("document");
        return envelope;
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
}
