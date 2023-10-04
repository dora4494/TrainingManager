package fr.paloit.paloformation;

import fr.paloit.paloformation.model.ToDo;
import fr.paloit.paloformation.repository.SessionRepository;
import fr.paloit.paloformation.repository.TacheRepository;
import fr.paloit.paloformation.repository.ToDoRepository;
import fr.paloit.paloformation.service.ToDoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


@SpringBootTest(classes = ToDoService.class)
public class ToDoServiceTest {

 @Autowired
    private ToDoService toDoService;

    @MockBean
    ToDoRepository toDoRepository;

    @MockBean
    TacheRepository tacheRepository;

    @MockBean
    SessionRepository sessionRepository;

    @Test
    public void todoserv(){


        ToDo todo = new ToDo();
        Assertions.assertEquals(ToDo.Etat.A_FAIRE,todo.getEtatb());

        toDoService.etatFait(todo);

        Assertions.assertEquals(ToDo.Etat.FAIT,todo.getEtatb());

    }


     /* @Test
  public void testCreerToDo() {
        ToDo toDo = new ToDo();
        toDo.setIntitule("test");

        when(toDoRepository.save(any(ToDo.class))).thenReturn(toDo);

        ToDo creee = toDoService.creerToDo(toDo);

        assertThat(creee).isNotNull();
        assertThat(creee.getIntitule()).isEqualTo(toDo.getIntitule());

    }
    */

}