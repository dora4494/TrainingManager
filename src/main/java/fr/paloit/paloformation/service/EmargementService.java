package fr.paloit.paloformation.service;

import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.Utilisateur;

import java.io.IOException;


public interface EmargementService {

    interface FeuilleEmargement {

    }
    public void envoyerDemandeSignature(Iterable<Utilisateur> utilisateurs) throws IOException;
    public void envoyerDemandeSignature(Iterable<Utilisateur> utilisateurs, FeuilleEmargement feuilleEmargement) throws IOException;

    public FeuilleEmargement getFeuilleEmargement(Session session);
}
