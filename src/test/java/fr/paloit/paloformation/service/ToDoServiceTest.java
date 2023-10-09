package fr.paloit.paloformation.service;

import fr.paloit.paloformation.model.ToDo;
import fr.paloit.paloformation.repository.ToDoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class ToDoServiceTest {

    @Test
    public void todo(){
        System.out.println("ToDoServiceTest.todo a");
        Assertions.assertEquals(2, 3+1);

    }

    @Test
    public void todoa(){
        System.out.println("ToDoServiceTest.todo b");
        Assertions.assertEquals(2, 1+1);
        System.out.println("ToDoServiceTest.todo c");
    }

    @Test
    public void todob(){
        System.out.println("ToDoServiceTest.todo d");
        int x = 1/0;
        System.out.println("ToDoServiceTest.todo c");
    }

    @Test
    public void todoserv(){


        ToDo todo = new ToDo();
        Assertions.assertEquals(ToDo.Etat.A_FAIRE,todo.getEtatb());

        toDoService.modifierEtat(todo);

        Assertions.assertEquals(ToDo.Etat.FAIT,todo.getEtatb());

    }
    ToDoService toDoService;
    @BeforeEach
    public void initialisation() {
         toDoService= new ToDoService();
        ToDoRepository toDoRepository=  new ToDoRepository(){
          @Override
          public <S extends ToDo> S save(S entity) {
              return null;
          }

          @Override
          public <S extends ToDo> Iterable<S> saveAll(Iterable<S> entities) {
              return null;
          }

          @Override
          public Optional<ToDo> findById(Long aLong) {
              return Optional.empty();
          }

          @Override
          public boolean existsById(Long aLong) {
              return false;
          }

          @Override
          public Iterable<ToDo> findAll() {
              return null;
          }

          @Override
          public Iterable<ToDo> findAllById(Iterable<Long> longs) {
              return null;
          }

          @Override
          public long count() {
              return 0;
          }

          @Override
          public void deleteById(Long aLong) {

          }

          @Override
          public void delete(ToDo entity) {

          }

          @Override
          public void deleteAllById(Iterable<? extends Long> longs) {

          }

          @Override
          public void deleteAll(Iterable<? extends ToDo> entities) {

          }

          @Override
          public void deleteAll() {

          }
      };
        toDoService.toDoRepository = toDoRepository;

    }


}
