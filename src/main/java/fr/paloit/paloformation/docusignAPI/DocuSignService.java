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
import java.util.stream.Collectors;

@Service
public class DocuSignService implements EmargementService {
    Logger logger = LoggerFactory.getLogger(DocuSignService.class);
    @Autowired
    DocuSign docuSign;

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
            EnveloppeDocuSign enveloppe = creerEnveloppe(utilisateur);
            enveloppe.setDocument((DocuSignFeuilleEmargement) feuilleEmargement);
            try {
                envoyerEnveloppe(enveloppe);
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void envoyerEnveloppe(EnveloppeDocuSign enveloppe) throws IOException, ApiException {
        docuSign.envoyerEnveloppe(enveloppe.generer());
    }


    @Override
    public FeuilleEmargement getFeuilleEmargement(Session session) {
        final DocuSignFeuilleEmargement feuilleEmargement = new DocuSignFeuilleEmargement();
        feuilleEmargement.setNomFichier("emargement_" + session.getFormation().getIntitule() + ".txt");

        String texte = String.join("\n",
                "Formation: " + session.getFormation().getIntitule(),
                "Formateur: " + session.getFormateur().getPrenom() + " " + session.getFormateur().getNom(),
                "\n\n",
                session.getParticipants().stream()
                        .map(utilisateur -> formatNomUtilisateur(utilisateur))
                        .collect(Collectors.joining("\n\n\n"))
        );
        feuilleEmargement.setTexteDocument(texte);

        return feuilleEmargement;
    }

    private static String formatNomUtilisateur(Utilisateur utilisateur) {
        return utilisateur.getPrenom() + " " + utilisateur.getNom();
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
