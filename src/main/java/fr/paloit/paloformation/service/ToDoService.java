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


        LocalDate dateCreationSession = session.getDateCreation();
        LocalDate dateLaPlusGrande = session.getDates().stream().max(Comparator.naturalOrder()).orElse(null);

        for (Tache tache : taches) {
            ToDo toDo = new ToDo();
            toDo.setTache(tache);
            toDo.setSession(session);
            toDo.setDate(dateCreationSession);
            toDoRepository.save(toDo);
        }


        ToDo inviterPart = new ToDo();
        inviterPart.setTache(taches.get(3)); // Inviter Participants
        inviterPart.setSession(session);
        inviterPart.setDate(dateCreationSession.plusDays(7));
        toDoRepository.save(inviterPart);

        ToDo creerFeuilleE = new ToDo();
        creerFeuilleE.setTache(taches.get(4)); // Créer feuille d'émargement
        creerFeuilleE.setSession(session);
        creerFeuilleE.setDate(dateCreationSession.plusDays(9));
        toDoRepository.save(creerFeuilleE);

        ToDo creerAttestionF = new ToDo();
        creerAttestionF.setTache(taches.get(5)); // Créer l'attestation de formation
        creerAttestionF.setSession(session);
        creerAttestionF.setDate(dateCreationSession.plusDays(9));
        toDoRepository.save(creerAttestionF);

        for (LocalDate dateDeSession : session.getDates()) {
            ToDo envoyerFeuilleE = new ToDo();
            envoyerFeuilleE.setTache(taches.get(6)); // Envoyer la feuille d'émargement
            envoyerFeuilleE.setSession(session);
            envoyerFeuilleE.setDate(dateDeSession);
            toDoRepository.save(envoyerFeuilleE);
        }

        ToDo envoyerQuest = new ToDo();
        envoyerQuest.setTache(taches.get(7)); // Envoyer le questionnaire
        envoyerQuest.setSession(session);
        envoyerQuest.setDate(dateLaPlusGrande.plusDays(1));
        toDoRepository.save(envoyerQuest);

        ToDo transmettreAttestationF = new ToDo();
        transmettreAttestationF.setTache(taches.get(8)); // Transmettre l'attestation de formation
        transmettreAttestationF.setSession(session);
        transmettreAttestationF.setDate(dateLaPlusGrande.plusDays(1));
        toDoRepository.save(transmettreAttestationF);
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
