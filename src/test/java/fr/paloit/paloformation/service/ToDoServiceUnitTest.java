package fr.paloit.paloformation.service;


import fr.paloit.paloformation.model.SessionBuilder;
import fr.paloit.paloformation.model.Tache;
import fr.paloit.paloformation.model.ToDo;
import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;



public class ToDoServiceUnitTest {


    private ToDoService toDoService = new ToDoService();




    @ParameterizedTest(name="{0}: id={1},date={2}")
    @MethodSource
    public void testDateDesToDosCreees(String description,long id, LocalDate expected) {
        List<ToDo> listeToDos = toDoService.creerTodos(SessionBuilder.uneSession().get(), List.of(new Tache(id, null, new ArrayList<>())));
        assertEquals(1, listeToDos.size());
        assertEquals(expected, listeToDos.get(0).getDate());
    }


    @Test
    public void testFeuilleEmargement() {
        Set<LocalDate> dates = Set.of(
               LocalDate.of(2023, 11, 25),
              LocalDate.of(2023, 11, 26));
        List<ToDo> listeToDos = toDoService.creerTodos(SessionBuilder.uneSession().setDates(dates).get(), List.of(new Tache(7L, null, new ArrayList<>())));
        assertEquals(2, listeToDos.size());

        List<LocalDate> lstTodos = listeToDos.stream().map(ToDo::getDate).toList();
        MatcherAssert.assertThat(lstTodos, Matchers.containsInAnyOrder(dates.toArray()));
    }

    @Test
    public void testEnvoyerQuestionnaire() {
        Set<LocalDate> dates = Set.of(
                LocalDate.of(2023, 11, 25),
                LocalDate.of(2023, 11, 26));
        List<ToDo> listeToDos = toDoService.creerTodos(SessionBuilder.uneSession().setDates(dates).get(), List.of(new Tache(8L, null, new ArrayList<>())));
        assertEquals(1, listeToDos.size());
        assertEquals( LocalDate.of(2023, 11, 26+1), listeToDos.get(0).getDate());
    }


    @Test
    public void testEnvoyerAttestationFormation() {
        Set<LocalDate> dates = Set.of(
                LocalDate.of(2023, 11, 25),
                LocalDate.of(2023, 11, 26));
        List<ToDo> listeToDos = toDoService.creerTodos(SessionBuilder.uneSession().setDates(dates).get(), List.of(new Tache(9L, null, new ArrayList<>())));
        assertEquals(1, listeToDos.size());
        assertEquals( LocalDate.of(2023, 11, 26+1), listeToDos.get(0).getDate());
    }

   private static Stream<Arguments> testDateDesToDosCreees() {
        return Stream.of(
                Arguments.of("todo standard", 1L,LocalDate.now()),
                Arguments.of("bloquer agenda formateur", 2L,LocalDate.now()),
                Arguments.of("demander liste des participants", 3L,LocalDate.now()),
                Arguments.of("assigner role de co-animateur au formateur", 10L,LocalDate.now()),
                Arguments.of("invitation participants", 4L, LocalDate.now().plusDays(7)),
                Arguments.of("feuille émargement", 5L, LocalDate.now().plusDays(9)),
                Arguments.of("attestation de formation", 6L, LocalDate.now().plusDays(9)));

    }


}
