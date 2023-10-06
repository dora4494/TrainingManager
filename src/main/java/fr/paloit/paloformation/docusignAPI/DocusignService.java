package fr.paloit.paloformation.docusignAPI;

import fr.paloit.paloformation.model.Utilisateur;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DocusignService {

    public void envoyerDemandeSignature(Utilisateur utilisateur) throws IOException {
        Docusign docusign = new Docusign();
        docusign.envoyerEnveloppe(utilisateur);

    }





}
