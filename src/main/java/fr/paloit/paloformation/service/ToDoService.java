package fr.paloit.paloformation.service;

import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.Tache;
import fr.paloit.paloformation.model.ToDo;
import fr.paloit.paloformation.repository.SessionRepository;
import fr.paloit.paloformation.repository.TacheRepository;
import fr.paloit.paloformation.repository.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToDoService {

    @Autowired
    TacheRepository tacheRepository;

    @Autowired
    ToDoRepository toDoRepository;

    @Autowired
    SessionRepository sessionRepository;

    public void creerTodos(Session session) {
        Tache conventionFormation = tacheRepository.findById(1L).orElse(null);
        Tache feuilleEmargement = tacheRepository.findById(2L).orElse(null);
        List<ToDo> todos = getToDos(session, conventionFormation, feuilleEmargement);

        for (ToDo todo : todos) {
            toDoRepository.save(todo);
        }


    }


    private static List<ToDo> getToDos(Session session, Tache conventionFormation, Tache feuilleEmargement) {
        LocalDate conventionFormationDate = session.getDateDebut().minusDays(7);
        System.out.println("Date de début : " + session.getDateDebut());
        System.out.println("Date de début moins 7 jours : " + conventionFormationDate);

        if (conventionFormation == null || feuilleEmargement == null) {
            throw new RuntimeException("Erreur: tâches introuvables");
        }

        List<ToDo> todos = new ArrayList<>();
        ToDo conventionF = new ToDo();
        conventionF.setTache(conventionFormation);
        conventionF.setSession(session);
        conventionF.setDate(conventionFormationDate);
        todos.add(conventionF);

        ToDo feuilleE = new ToDo();
        feuilleE.setTache(feuilleEmargement);
        feuilleE.setSession(session);
        feuilleE.setDate(session.getDateDebut().minusDays(2));
        todos.add(feuilleE);
        return todos;
    }


    public ToDo trouverToDoById(Long id) {
        return toDoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session introuvable avec l'ID : " + id));
    }


    // Etat 1 = "A FAIRE" , Etat 2 : "FAIT"
    public void modifierEtat(ToDo todo) {
        if (todo.getEtat() == 2) {
            todo.setEtat(1);
        } else {
            todo.setEtat(2);
        }
        toDoRepository.save(todo);

    }

    /*
    // Etat 1 = "A FAIRE" , Etat 2 : "FAIT"
    public void etatFait(ToDo todo) {
        if (todo.getEtat() == 1) {
            todo.setEtat(2);
            toDoRepository.save(todo);
        }
    }
    */

}
