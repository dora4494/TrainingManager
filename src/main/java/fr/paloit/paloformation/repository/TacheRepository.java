package fr.paloit.paloformation.repository;

import fr.paloit.paloformation.model.Tache;
import org.springframework.data.repository.CrudRepository;

public interface TacheRepository extends CrudRepository<Tache, Long> {
}
