package fr.paloit.paloformation.service;


import fr.paloit.paloformation.model.ToDo;
import fr.paloit.paloformation.repository.SessionRepository;
import fr.paloit.paloformation.repository.TacheRepository;
import fr.paloit.paloformation.repository.ToDoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ToDoServiceTest {

    @Autowired
    private ToDoService toDoService;

    @Autowired
    ToDoRepository toDoRepository;

    @Autowired
    TacheRepository tacheRepository;

    @Autowired
    SessionRepository sessionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testModifierEtatToDo() {
        ToDo toDo = new ToDo();
        assertEquals(ToDo.Etat.A_FAIRE, toDo.getEtat());

        toDoService.modifierEtat(toDo);
        assertEquals(ToDo.Etat.FAIT, toDo.getEtat());
        assertNotNull(toDo.getId());

        entityManager.flush(); // Pour forcer l'enregistrement en base.
        entityManager.clear(); // Pour forcer le rechargement depuis la base.

        toDo = toDoService.trouverToDoById(toDo.getId());
        assertEquals(ToDo.Etat.FAIT, toDo.getEtat());

        toDoService.modifierEtat(toDo);
        assertEquals(ToDo.Etat.A_FAIRE, toDo.getEtat());

        entityManager.flush();
        entityManager.clear();

        toDo = toDoService.trouverToDoById(toDo.getId());
        assertEquals(ToDo.Etat.A_FAIRE, toDo.getEtat());
    }
}
