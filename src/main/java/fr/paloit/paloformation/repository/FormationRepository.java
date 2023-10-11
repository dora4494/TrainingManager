package fr.paloit.paloformation.repository;

import fr.paloit.paloformation.model.Formation;
import org.springframework.data.repository.CrudRepository;

public interface FormationRepository extends CrudRepository<Formation, Long> {
    Formation findByIntitule(String intitule);
}
