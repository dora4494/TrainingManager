package fr.paloit.paloformation.service;

import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.repository.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormationService {

    @Autowired
    FormationRepository formationRepository;


    public Formation creerFormation(Formation formation) {
        Formation formationExistante = formationRepository.findByIntitule(formation.getIntitule());
        if (formationExistante == null) {
            formationRepository.save(formation);
            return new Formation(true, "La formation a été créée");
        } else {
            return new Formation(false, "La formation est déjà existante");
        }
    }


    public Iterable<Formation> listeFormations() {

        return formationRepository.findAll();
    }

    public void supprimerFormation(Formation formation) {
        formationRepository.delete(formation);
    }

    public Formation trouverFormationById(Long id) {
        return formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation introuvable avec l'ID : " + id));
    }


}
