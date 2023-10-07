package fr.paloit.paloformation.service;

import fr.paloit.paloformation.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.paloit.paloformation.model.Utilisateur;

import java.util.List;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public Iterable<Utilisateur> listeUtilisateurs() {

        return utilisateurRepository.findAll();
    }

    public void creerUtilisateur(Utilisateur utilisateur) {

        utilisateurRepository.save(utilisateur);
    }


    public void supprimerUtilisateur(Utilisateur utilisateur) {
        utilisateurRepository.delete(utilisateur);
    }

    public Utilisateur trouverUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));
    }

    public Iterable<Utilisateur> listeUtilisateursById(List<Long> ids) {
        return utilisateurRepository.findAllById(ids);
    }



}
