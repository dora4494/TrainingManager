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
import java.util.Comparator;
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

            LocalDate dateLaPlusPetite = session.getDates().stream().min(Comparator.naturalOrder()).orElse(null);


            if (conventionFormation == null || feuilleEmargement == null) {
                throw new RuntimeException("Erreur: tâches introuvables");
            }

            ToDo conventionF = new ToDo();
            conventionF.setTache(conventionFormation);
            conventionF.setSession(session);
            conventionF.setDate(dateLaPlusPetite.minusDays(7));

            ToDo feuilleE = new ToDo();
            feuilleE.setTache(feuilleEmargement);
            feuilleE.setSession(session);
            feuilleE.setDate(dateLaPlusPetite.minusDays(2));

            toDoRepository.save(conventionF);
            toDoRepository.save(feuilleE);
        }



    public ToDo trouverToDoById(Long id) {
        return toDoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session introuvable avec l'ID : " + id));
    }


    public Iterable<ToDo> listeToDo() {
        return toDoRepository.findAll();
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


}
