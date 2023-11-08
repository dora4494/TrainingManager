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
    DocuSignAdapter docuSign;

    public void envoyerDemandeSignature(Iterable<Utilisateur> utilisateurs) throws IOException {
        envoyerDemandeSignature(utilisateurs, null);
    }

    @Override
    public void envoyerDemandeSignature(Iterable<Utilisateur> utilisateurs, FeuilleEmargement feuilleEmargement) throws IOException {

        logger.info("Envoi d'une demande d'émargement pour: ");
        for (Utilisateur utilisateur : utilisateurs) {
            logger.info("  - "
                    + utilisateur.getPrenom()
                    + " " + utilisateur.getNom()
                    + "(" + utilisateur.getMail() + ")");
        }

        final EnveloppeDocuSign enveloppe = new EnveloppeDocuSign();
        enveloppe.setEmailSujet("Feuille d'émargement");
        for (Utilisateur utilisateur : utilisateurs) {
            enveloppe.ajouterSignataire(utilisateur);
        }
        enveloppe.setDocument((DocuSignFeuilleEmargement) feuilleEmargement);
        try {
            envoyerEnveloppe(enveloppe);
        } catch (ApiException e) {
            throw new RuntimeException(e);
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

}
