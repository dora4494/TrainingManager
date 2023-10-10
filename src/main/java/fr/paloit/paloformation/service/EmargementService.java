package fr.paloit.paloformation.service;

import fr.paloit.paloformation.docusignAPI.Docusign;
import fr.paloit.paloformation.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface EmargementService {


    public void envoyerDemandeSignature(Iterable<Utilisateur> utilisateurs) throws IOException;

}
