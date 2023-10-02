package fr.paloit.paloformation.repository;

import fr.paloit.paloformation.model.ToDo;
import org.springframework.data.repository.CrudRepository;

public interface ToDoRepository extends CrudRepository<ToDo, Long> {
}
