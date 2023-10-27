package fr.paloit.paloformation.service;

import fr.paloit.paloformation.docusignAPI.Docusign;
import fr.paloit.paloformation.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


public interface EmargementService {

    interface FeuilleEmargement {

    }
    public void envoyerDemandeSignature(Iterable<Utilisateur> utilisateurs) throws IOException;
    public void envoyerDemandeSignature(Iterable<Utilisateur> utilisateurs, FeuilleEmargement feuilleEmargement) throws IOException;

}
