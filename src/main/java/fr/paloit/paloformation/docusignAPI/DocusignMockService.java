package fr.paloit.paloformation.docusignAPI;

import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.EmargementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Profile({"default", "!docusign"})
public class DocusignMockService implements EmargementService {
    Logger logger = LoggerFactory.getLogger(DocusignMockService.class);

    public void envoyerDemandeSignature(Iterable<Utilisateur> utilisateurs) throws IOException {
        envoyerDemandeSignature(utilisateurs, null);
    }

    @Override
    public void envoyerDemandeSignature(Iterable<Utilisateur> utilisateurs, FeuilleEmargement feuilleEmargement) throws IOException {
        logger.info("envoyerDemandeSignature");

        logger.info("Envoi d'une demande d'émargement pour:");
        for (Utilisateur utilisateur : utilisateurs) {
            logger.info("  - "
                    + utilisateur.getPrenom()
                    + " " + utilisateur.getNom()
                    + "(" +  utilisateur.getMail() + ")");
        }
    }

    @Override
    public FeuilleEmargement getFeuilleEmargement(Session session) {
        return new DocuSignFeuilleEmargement();
    }


}
