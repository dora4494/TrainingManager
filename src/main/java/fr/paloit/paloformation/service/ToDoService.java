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
import java.util.*;

@Service
public class ToDoService {

    @Autowired
    TacheRepository tacheRepository;

    @Autowired
    ToDoRepository toDoRepository;

    @Autowired
    SessionRepository sessionRepository;


    public void creerTodos(Session session) {
        List<Tache> taches = (List<Tache>) tacheRepository.findAll();
        List<ToDo> todosASauver = creerTodos(session, taches);

        toDoRepository.saveAll(todosASauver);


    }

    List<ToDo> creerTodos(Session session, List<Tache> taches) {
        List<ToDo> todosASauver = new ArrayList<>();

        for (Tache tache : taches) {
            List<LocalDate> lstdates = listeDeDatesPourLesToDos(session, tache);
            for (LocalDate date : lstdates) {
                ToDo toDo = new ToDo();
                toDo.setTache(tache);
                toDo.setSession(session);
                toDo.setDate(date);
                todosASauver.add(toDo);
            }
        }
        return todosASauver;
    }

    private List<LocalDate> listeDeDatesPourLesToDos(Session session, Tache tache) {

        switch (tache.getType()) {
            case INVITER_PARTICIPANT:
                return List.of(session.getDateCreation().plusDays(7));

            case CREER_FEUILLE_EMARGEMENT:
                return List.of(session.getDateCreation().plusDays(9));

            case CREER_ATTESTATION_FORMATION:
                return List.of(session.getDateCreation().plusDays(9));

            case ENVOYER_FEUILLE_EMARGEMENT:
                return session.getDates().stream().toList();

            case ENVOYER_QUESTIONNAIRE:
                return List.of(session.getDateLaPlusGrande().plusDays(1));

            case TRANSMETTRE_ATTESTATION_FORMATION:
                return List.of(session.getDateLaPlusGrande().plusDays(1));

            default:
                return List.of(session.getDateCreation());
        }
    }

    public ToDo trouverToDoById(Long id) {
        return toDoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session introuvable avec l'ID : " + id));
    }


    public Iterable<ToDo> listeToDo() {
        return toDoRepository.findAll();
    }


    public void modifierEtat(ToDo todo) {
        if (todo.getEtat() == ToDo.Etat.FAIT) {
            todo.setEtat(ToDo.Etat.A_FAIRE);
        } else {
            todo.setEtat(ToDo.Etat.FAIT);
        }
        toDoRepository.save(todo);
    }

    public long nombreToDoAvecEcheanceDepassee() {
        List<ToDo> todos = (List<ToDo>) toDoRepository.findAll();
        long nombreDeToDos = todos.stream()
                .filter(todo -> todo.getDate().isBefore(LocalDate.now()) && todo.getEtat() == ToDo.Etat.A_FAIRE)
                .count();
        return nombreDeToDos;
    }


}
