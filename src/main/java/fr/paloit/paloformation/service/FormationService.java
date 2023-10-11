package fr.paloit.paloformation.service;

import fr.paloit.paloformation.model.Formation;
import fr.paloit.paloformation.repository.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FormationService {

    @Autowired
    FormationRepository formationRepository;


    public void creerFormation(Formation formation) {
        if (formationRepository.findByIntitule(formation.getIntitule()) == null) {
            formationRepository.save(formation);
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
