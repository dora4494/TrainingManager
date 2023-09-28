package fr.paloit.paloformation.repository;

import fr.paloit.paloformation.model.Utilisateur;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
  public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long>{



}
