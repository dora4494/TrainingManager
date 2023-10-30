package fr.paloit.paloformation.docusignAPI;

import com.docusign.esign.client.ApiException;
import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.EmargementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DocuSignService implements EmargementService {
    Logger logger = LoggerFactory.getLogger(DocuSignService.class);
    @Autowired
    Docusign docusign;

    public void envoyerDemandeSignature(Iterable<Utilisateur> utilisateurs) throws IOException {
        envoyerDemandeSignature(utilisateurs, null);
    }

    @Override
    public void envoyerDemandeSignature(Iterable<Utilisateur> utilisateurs, FeuilleEmargement feuilleEmargement) throws IOException {
        for (Utilisateur utilisateur : utilisateurs) {
            logger.info("Envoi d'une demande d'émargement pour: "
                    + utilisateur.getPrenom()
                    + " " + utilisateur.getNom()
                    + "(" + utilisateur.getMail() + ")");
            EnveloppeDocuSign envelope = creerEnveloppe(utilisateur);
            try {
                docusign.envoyerEnveloppe(envelope.generer());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }



    @Override
    public FeuilleEmargement getFeuilleEmargement(Session session) {
        return new DocuSignFeuilleEmargement();
    }

    public static EnveloppeDocuSign creerEnveloppe(Utilisateur utilisateur) {
        // Create envelopeDefinition object
        final EnveloppeDocuSign enveloppeDocuSign = new EnveloppeDocuSign();
        enveloppeDocuSign.setEmailSujet("Feuille d'émargement");
        enveloppeDocuSign.ajouterSignataire(utilisateur);
        enveloppeDocuSign.setDocument("doc1.txt");
        return enveloppeDocuSign;
    }
}
