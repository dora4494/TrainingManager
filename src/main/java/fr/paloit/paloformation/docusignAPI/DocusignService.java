package fr.paloit.paloformation.docusignAPI;

import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.EmargementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DocusignService implements EmargementService {
    Logger logger = LoggerFactory.getLogger(DocusignService.class);
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
            docusign.envoyerEnveloppe(utilisateur);
        }
    }

    @Override
    public FeuilleEmargement getFeuilleEmargement(Session session) {
        return new DocuSignFeuilleEmargement();
    }
}
