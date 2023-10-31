package fr.paloit.paloformation.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoTest {
    @Test
    public void testEtatTodo(){
        ToDo todo = new ToDo();

        //assertEquals(1L,todo.getEtat());
        assertEquals(ToDo.Etat.A_FAIRE,todo.getEtatb());

        todo.setEtatb(ToDo.Etat.FAIT);
        //assertEquals(2L, todo.getEtat());
        assertEquals(ToDo.Etat.FAIT,todo.getEtatb());

        todo.setEtatb(ToDo.Etat.A_FAIRE);
        //assertEquals(1L, todo.getEtat());
        assertEquals(ToDo.Etat.A_FAIRE,todo.getEtatb());
    }
}
