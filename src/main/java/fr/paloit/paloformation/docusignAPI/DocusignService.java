package fr.paloit.paloformation.docusignAPI;

import fr.paloit.paloformation.model.Utilisateur;
import fr.paloit.paloformation.service.EmargementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DocusignService implements EmargementService {

    @Autowired
    Docusign docusign;

    public void envoyerDemandeSignature(Iterable<Utilisateur> utilisateurs) throws IOException {
        for (Utilisateur utilisateur : utilisateurs) {
            docusign.envoyerEnveloppe(utilisateur);
        }

    }


}
