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
        LocalDate dateCreationSession = session.getDateCreation();
        System.out.println(dateCreationSession);
        LocalDate dateLaPlusGrande = session.getDates().stream().max(Comparator.naturalOrder()).orElse(null);

        for (Tache tache : taches) {

            if (tache.getId() == 6L) { // Envoyer Feuille d'émargement
                for (LocalDate date : session.getDates()) {
                    ToDo toDo = new ToDo();
                    toDo.setTache(tache);
                    toDo.setSession(session);
                    toDo.setDate(date);
                    todosASauver.add(toDo);
                }

            } else {

                ToDo toDo = new ToDo();
                toDo.setTache(tache);
                toDo.setSession(session);

                if (tache.getId() == 3L) { // Inviter Participants
                    toDo.setDate(dateCreationSession.plusDays(7));
                } else if (tache.getId() == 4L) { // Créer feuille d'émargement
                    toDo.setDate(dateCreationSession.plusDays(9));
                } else if (tache.getId() == 5L) { // Créer l'attestation de formation
                    toDo.setDate(dateCreationSession.plusDays(9));
                } else if (tache.getId() == 7L) { // Envoyer le questionnaire
                    toDo.setDate(dateLaPlusGrande.plusDays(1));
                } else if (tache.getId() == 8L) { // Transmettre l'attestation de formation
                    toDo.setDate(dateLaPlusGrande.plusDays(1));
                } else {
                    toDo.setDate(dateCreationSession);
                }
                todosASauver.add(toDo);
            }
        }

        return todosASauver;
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
